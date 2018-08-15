package sa.gov.moe.etraining.module.notification;

import java.util.List;

import sa.gov.moe.etraining.model.api.EnrolledCoursesResponse;

public class DummyNotificationDelegate implements NotificationDelegate {
    @Override
    public void unsubscribeAll() {

    }

    @Override
    public void resubscribeAll() {

    }

    @Override
    public void syncWithServerForFailure() {

    }

    @Override
    public void checkCourseEnrollment(List<EnrolledCoursesResponse> responses) {

    }

    @Override
    public void changeNotificationSetting(String courseId, String channelId, boolean subscribe) {

    }


    @Override
    public boolean isSubscribedByCourseId(String channel){
        return false;
    }

    @Override
    public void checkAppUpgrade(){}
}
