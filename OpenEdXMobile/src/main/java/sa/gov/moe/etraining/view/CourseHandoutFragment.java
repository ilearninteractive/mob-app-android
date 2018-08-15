package sa.gov.moe.etraining.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Xml.Encoding;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.google.inject.Inject;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import sa.gov.moe.etraining.R;

import de.greenrobot.event.EventBus;
import okhttp3.Request;
import roboguice.inject.InjectExtra;
import roboguice.inject.InjectView;
import sa.gov.moe.etraining.base.BaseFragment;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.event.NetworkConnectivityChangeEvent;
import sa.gov.moe.etraining.http.callback.ErrorHandlingOkCallback;
import sa.gov.moe.etraining.http.notifications.FullScreenErrorNotification;
import sa.gov.moe.etraining.http.notifications.SnackbarErrorNotification;
import sa.gov.moe.etraining.http.provider.OkHttpClientProvider;
import sa.gov.moe.etraining.interfaces.RefreshListener;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.model.api.EnrolledCoursesResponse;
import sa.gov.moe.etraining.model.api.HandoutModel;
import sa.gov.moe.etraining.module.analytics.Analytics;
import sa.gov.moe.etraining.module.analytics.AnalyticsRegistry;
import sa.gov.moe.etraining.util.NetworkUtil;
import sa.gov.moe.etraining.util.WebViewUtil;
import sa.gov.moe.etraining.view.custom.URLInterceptorWebViewClient;

public class CourseHandoutFragment extends BaseFragment implements RefreshListener {
    protected final Logger logger = new Logger(getClass().getName());

    @InjectExtra(Router.EXTRA_COURSE_DATA)
    private EnrolledCoursesResponse courseData;

    @Inject
    private AnalyticsRegistry analyticsRegistry;

    @Inject
    private IEdxEnvironment environment;

    @Inject
    private OkHttpClientProvider okHttpClientProvider;

    @InjectView(R.id.webview)
    private WebView webview;

    private FullScreenErrorNotification errorNotification;

    private SnackbarErrorNotification snackbarErrorNotification;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analyticsRegistry.trackScreenView(Analytics.Screens.COURSE_HANDOUTS, courseData.getCourse().getId(), null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_handout, container, false);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        errorNotification = new FullScreenErrorNotification(webview);
        snackbarErrorNotification = new SnackbarErrorNotification(webview);
        new URLInterceptorWebViewClient(getActivity(), webview).setAllLinksAsExternal(true);
        loadData();
    }

    private void loadData() {
        okHttpClientProvider.getWithOfflineCache().newCall(new Request.Builder()
                .url(courseData.getCourse().getCourse_handouts())
                .get()
                .build())
                .enqueue(new ErrorHandlingOkCallback<HandoutModel>(getActivity(),
                        HandoutModel.class, errorNotification, snackbarErrorNotification, this) {
                    @Override
                    protected void onResponse(@NonNull final HandoutModel result) {
                        if (getActivity() == null) {
                            return;
                        }

                        if (!TextUtils.isEmpty(result.handouts_html)) {
                            populateHandouts(result);
                        } else {
                            errorNotification.showError(R.string.no_handouts_to_display,
                                    FontAwesomeIcons.fa_exclamation_circle, 0, null);
                        }
                    }

                    @Override
                    protected void onFailure(@NonNull final Throwable error) {
                        super.onFailure(error);

                        if (getActivity() == null) {
                            return;
                        }
                    }

                    @Override
                    protected void onFinish() {
                        if (!EventBus.getDefault().isRegistered(CourseHandoutFragment.this)) {
                            EventBus.getDefault().registerSticky(CourseHandoutFragment.this);
                        }
                    }
                });
    }

    private void populateHandouts(HandoutModel handout) {
        hideErrorMessage();

        StringBuilder buff = WebViewUtil.getIntialWebviewBuffer(getActivity(), logger);

        buff.append("<body>");
        buff.append("<div class=\"header\">");
        buff.append(handout.handouts_html);
        buff.append("</div>");
        buff.append("</body>");

        webview.clearCache(true);
        webview.loadDataWithBaseURL(environment.getConfig().getApiHostURL(), buff.toString(),
                "text/html", Encoding.UTF_8.toString(), null);

    }

    private void hideErrorMessage() {
        webview.setVisibility(View.VISIBLE);
        errorNotification.hideError();
    }

    @SuppressWarnings("unused")
    public void onEventMainThread(NetworkConnectivityChangeEvent event) {
        if (!NetworkUtil.isConnected(getContext())) {
            if (!errorNotification.isShowing()) {
                snackbarErrorNotification.showOfflineError(this);
            }
        }
    }

    @Override
    public void onRefresh() {
        errorNotification.hideError();
        loadData();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRevisit() {
        if (NetworkUtil.isConnected(getActivity())) {
            snackbarErrorNotification.hideError();
        }
    }
}
