package sa.gov.moe.etraining.view;

import android.os.Bundle;

import com.google.inject.Inject;

import sa.gov.moe.etraining.base.BaseFragment;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.model.course.CourseComponent;
import sa.gov.moe.etraining.view.common.PageViewStateCallback;
import sa.gov.moe.etraining.view.common.RunnableCourseComponent;

public abstract class CourseUnitFragment extends BaseFragment implements PageViewStateCallback, RunnableCourseComponent {
    public interface HasComponent {
        CourseComponent getComponent();
        void navigateNextComponent();
        void navigatePreviousComponent();
    }

    protected CourseComponent unit;
    protected HasComponent hasComponentCallback;

    @Inject
    IEdxEnvironment environment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unit = getArguments() == null ? null :
                (CourseComponent) getArguments().getSerializable(Router.EXTRA_COURSE_UNIT);
    }

    @Override
    public void onPageShow() {

    }

    @Override
    public void onPageDisappear() {

    }

    @Override
    public CourseComponent getCourseComponent() {
        return unit;
    }

    @Override
    public abstract void run();

    public void setHasComponentCallback(HasComponent callback) {
        hasComponentCallback = callback;
    }
}
