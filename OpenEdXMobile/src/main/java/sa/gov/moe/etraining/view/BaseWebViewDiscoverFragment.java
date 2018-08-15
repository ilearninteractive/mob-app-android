package sa.gov.moe.etraining.view;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.google.inject.Inject;

import sa.gov.moe.etraining.R;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.course.CourseAPI;
import sa.gov.moe.etraining.course.CourseService;
import sa.gov.moe.etraining.http.HttpStatus;
import sa.gov.moe.etraining.http.HttpStatusException;
import sa.gov.moe.etraining.http.notifications.FullScreenErrorNotification;
import sa.gov.moe.etraining.http.provider.OkHttpClientProvider;
import sa.gov.moe.etraining.interfaces.RefreshListener;
import sa.gov.moe.etraining.interfaces.WebViewStatusListener;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.util.WebViewUtil;
import sa.gov.moe.etraining.view.custom.EdxWebView;
import sa.gov.moe.etraining.view.custom.URLInterceptorWebViewClient;

public abstract class BaseWebViewDiscoverFragment extends OfflineSupportBaseFragment
        implements URLInterceptorWebViewClient.IActionListener, WebViewStatusListener, RefreshListener {
    protected final Logger logger = new Logger(getClass().getName());

    private EdxWebView webView;
    private ProgressBar progressWheel;

    protected FullScreenErrorNotification errorNotification;

    @Inject
    protected IEdxEnvironment environment;

    @Inject
    private CourseService courseService;

    @Inject
    private CourseAPI courseApi;

    @Inject
    private OkHttpClientProvider okHttpClientProvider;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = (EdxWebView) view.findViewById(R.id.webview);
        progressWheel = (ProgressBar) view.findViewById(R.id.loading_indicator);

        initWebView();
    }

    @Override
    public void onResume() {
        super.onResume();
        webView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        webView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        webView.destroy();
    }

    private void initWebView() {
        URLInterceptorWebViewClient client = new URLInterceptorWebViewClient(getActivity(), webView);

        // if all the links are to be treated as external
        client.setAllLinksAsExternal(isAllLinksExternal());

        client.setActionListener(this);
        client.setPageStatusListener(pageStatusListener);
    }

    /**
     * Loads the given URL into {@link #webView}.
     *
     * @param url The URL to load.
     */
    protected void loadUrl(@NonNull String url) {
        WebViewUtil.loadUrlBasedOnOsVersion(getContext(), webView, url, this, errorNotification,
                okHttpClientProvider, R.string.lbl_reload, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefresh();
                    }
                });
    }

    @Override
    public void showLoadingProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoadingProgress() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.GONE);
        }
    }

    @Override
    public void clearWebView() {
        WebViewUtil.clearWebviewHtml(webView);
    }

    @Override
    public void onClickCourseInfo(String pathId) {
        //If Path id is not null or empty then call CourseInfoActivity
        if (!TextUtils.isEmpty(pathId)) {
            logger.debug("PathId" + pathId);
            environment.getRouter().showCourseInfo(getActivity(), pathId);
        }
    }

    @Override
    public void onClickEnroll(String courseId, boolean emailOptIn) {
        //TODO: Implement this when we will stop using CourseInfoActivity & BaseWebViewFindCoursesActivity
    }

    /**
     * By default, all links will not be treated as external.
     * Depends on host, as long as the links have same host, they are treated as non-external links.
     *
     * @return
     */
    protected boolean isAllLinksExternal() {
        return false;
    }

    /**
     * See description of: {@link URLInterceptorWebViewClient.IPageStatusListener#onPageLoadProgressChanged(WebView, int)
     * IPageStatusListener#onPageLoadProgressChanged}.
     */
    protected void onWebViewLoadProgressChanged(int progress) {
    }

    /*
     * In order to avoid reflection issues of public functions in event bus especially those that
     * aren't available on a certain api level, this listener has been refactored to a class
     * variable which is better explained in following references:
     * https://github.com/greenrobot/EventBus/issues/149
     * http://greenrobot.org/eventbus/documentation/faq/
     */
    private URLInterceptorWebViewClient.IPageStatusListener pageStatusListener = new URLInterceptorWebViewClient.IPageStatusListener() {
        @Override
        public void onPageStarted() {
            showLoadingProgress();
        }

        @Override
        public void onPageFinished() {
            hideLoadingProgress();
        }

        @Override
        public void onPageLoadError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            errorNotification.showError(getContext(),
                    new HttpStatusException(Response.error(HttpStatus.SERVICE_UNAVAILABLE,
                            ResponseBody.create(MediaType.parse("text/plain"), description))),
                    R.string.lbl_reload, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onRefresh();
                        }
                    });
            clearWebView();
        }

        @Override
        @TargetApi(Build.VERSION_CODES.M)
        public void onPageLoadError(WebView view, WebResourceRequest request,
                                    WebResourceResponse errorResponse,
                                    boolean isMainRequestFailure) {
            if (isMainRequestFailure) {
                errorNotification.showError(getContext(),
                        new HttpStatusException(Response.error(errorResponse.getStatusCode(),
                                ResponseBody.create(MediaType.parse(errorResponse.getMimeType()),
                                        errorResponse.getReasonPhrase()))),
                        R.string.lbl_reload, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onRefresh();
                            }
                        });
                clearWebView();
            }
        }

        @Override
        public void onPageLoadProgressChanged(WebView view, int progress) {
            onWebViewLoadProgressChanged(progress);
        }
    };
}
