package sa.gov.moe.etraining.view.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import sa.gov.moe.etraining.model.api.EnrolledCoursesResponse;
import sa.gov.moe.etraining.model.course.BlockType;
import sa.gov.moe.etraining.model.course.CourseComponent;
import sa.gov.moe.etraining.model.course.DiscussionBlockModel;
import sa.gov.moe.etraining.model.course.HtmlBlockModel;
import sa.gov.moe.etraining.model.course.VideoBlockModel;
import sa.gov.moe.etraining.util.Config;
import sa.gov.moe.etraining.view.CourseUnitDiscussionFragment;
import sa.gov.moe.etraining.view.CourseUnitEmptyFragment;
import sa.gov.moe.etraining.view.CourseUnitFragment;
import sa.gov.moe.etraining.view.CourseUnitMobileNotSupportedFragment;
import sa.gov.moe.etraining.view.CourseUnitOnlyOnYoutubeFragment;
import sa.gov.moe.etraining.view.CourseUnitVideoFragment;
import sa.gov.moe.etraining.view.CourseUnitWebViewFragment;

public class CourseUnitPagerAdapter extends FragmentStatePagerAdapter {
    private Config config;
    private List<CourseComponent> unitList;
    private EnrolledCoursesResponse courseData;
    private CourseUnitFragment.HasComponent callback;

    public CourseUnitPagerAdapter(FragmentManager manager,
                                  Config config,
                                  List<CourseComponent> unitList,
                                  EnrolledCoursesResponse courseData,
                                  CourseUnitFragment.HasComponent callback) {
        super(manager);
        this.config = config;
        this.unitList = unitList;
        this.courseData = courseData;
        this.callback = callback;
    }

    public CourseComponent getUnit(int pos) {
        if (pos >= unitList.size())
            pos = unitList.size() - 1;
        if (pos < 0)
            pos = 0;
        return unitList.get(pos);
    }

    /**
     * @return True if given unit is a video unit (not an only on YouTube unit)
     */
    public static boolean isCourseUnitVideo(CourseComponent unit) {
        return (unit instanceof VideoBlockModel && ((VideoBlockModel) unit).getData().encodedVideos.getPreferredVideoInfo() != null);
    }

    @Override
    public Fragment getItem(int pos) {
        CourseComponent unit = getUnit(pos);
        CourseUnitFragment unitFragment;
        //FIXME - for the video, let's ignore studentViewMultiDevice for now
        if (isCourseUnitVideo(unit)) {
            unitFragment = CourseUnitVideoFragment.newInstance((VideoBlockModel) unit, (pos < unitList.size()), (pos > 0));
        } else if (unit instanceof VideoBlockModel && ((VideoBlockModel) unit).getData().encodedVideos.getYoutubeVideoInfo() != null) {
            unitFragment = CourseUnitOnlyOnYoutubeFragment.newInstance(unit);
        } else if (config.isDiscussionsEnabled() && unit instanceof DiscussionBlockModel) {
            unitFragment = CourseUnitDiscussionFragment.newInstance(unit, courseData);
        } else if (!unit.isMultiDevice()) {
            unitFragment = CourseUnitMobileNotSupportedFragment.newInstance(unit);
        } else if (unit.getType() != BlockType.VIDEO &&
                unit.getType() != BlockType.HTML &&
                unit.getType() != BlockType.OTHERS &&
                unit.getType() != BlockType.DISCUSSION &&
                unit.getType() != BlockType.PROBLEM) {
            unitFragment = CourseUnitEmptyFragment.newInstance(unit);
        } else if (unit instanceof HtmlBlockModel) {
            unitFragment = CourseUnitWebViewFragment.newInstance((HtmlBlockModel) unit);
        }

        //fallback
        else {
            unitFragment = CourseUnitMobileNotSupportedFragment.newInstance(unit);
        }

        unitFragment.setHasComponentCallback(callback);

        return unitFragment;
    }

    @Override
    public int getCount() {
        return unitList.size();
    }
}
