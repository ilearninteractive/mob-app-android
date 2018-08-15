package sa.gov.moe.etraining.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import sa.gov.moe.etraining.R;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.event.NetworkConnectivityChangeEvent;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.model.api.EnrolledCoursesResponse;
import sa.gov.moe.etraining.util.FileUtil;
import sa.gov.moe.etraining.util.ResourceUtil;

public class CourseDatesFragment extends AuthenticatedWebViewFragment {
    private static final Logger logger = new Logger(CourseDatesFragment.class.getName());

    public static Bundle makeArguments(@NonNull Context context,
                                       @NonNull IEdxEnvironment environment,
                                       @NonNull EnrolledCoursesResponse courseData) {
        final StringBuilder courseInfoUrl = new StringBuilder(64);
        courseInfoUrl.append(environment.getConfig().getApiHostURL())
                .append("/courses/")
                .append(courseData.getCourse().getId())
                .append("/course/mobile_dates_fragment");
        String javascript;
        try {
            javascript = FileUtil.loadTextFileFromAssets(context, "js/filterHtml.js");
        } catch (IOException e) {
            logger.error(e);
            javascript = null;
        }
        if (!TextUtils.isEmpty(javascript)) {
            final CharSequence functionCall = ResourceUtil.getFormattedString(
                    "filterHtmlByClass('date-summary-container', '{not_found_message}');",
                    "not_found_message", context.getString(R.string.no_course_dates_to_display)
            );
            // Append function call in javascript
            javascript += functionCall;
        }
        return AuthenticatedWebViewFragment.makeArguments(courseInfoUrl.toString(), javascript, true);
    }

    public static CourseDatesFragment newInstance(@NonNull Context context,
                                                  @NonNull IEdxEnvironment environment,
                                                  @NonNull EnrolledCoursesResponse courseData) {
        final CourseDatesFragment fragment = new CourseDatesFragment();
        fragment.setArguments(makeArguments(context, environment, courseData));
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().registerSticky(this);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        OfflineSupportUtils.setUserVisibleHint(getActivity(), isVisibleToUser,
                authWebView != null && authWebView.isShowingError());
    }

    @SuppressWarnings("unused")
    public void onEvent(NetworkConnectivityChangeEvent event) {
        OfflineSupportUtils.onNetworkConnectivityChangeEvent(getActivity(), getUserVisibleHint(), authWebView.isShowingError());
    }

    @Override
    protected void onRevisit() {
        OfflineSupportUtils.onRevisit(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
    }
}
