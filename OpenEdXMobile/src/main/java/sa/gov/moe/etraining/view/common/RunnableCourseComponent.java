package sa.gov.moe.etraining.view.common;

import sa.gov.moe.etraining.model.course.CourseComponent;

/**
 * Created by hanning on 6/9/15.
 */
public interface RunnableCourseComponent extends Runnable{
    CourseComponent getCourseComponent();
}
