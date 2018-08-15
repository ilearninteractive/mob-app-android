package sa.gov.moe.etraining.test.screenshot.test;

import sa.gov.moe.etraining.view.DiscoveryLaunchActivity;
import sa.gov.moe.etraining.view.DiscoveryLaunchPresenter;
import sa.gov.moe.etraining.view.PresenterActivityScreenshotTest;
import org.junit.Test;

public class DiscoveryLaunchScreenshotTests extends PresenterActivityScreenshotTest<DiscoveryLaunchActivity, DiscoveryLaunchPresenter, DiscoveryLaunchPresenter.ViewInterface> {

    @Test
    public void testScreenshot_withCourseDiscoveryDisabled() {
        view.setEnabledButtons(false);
    }

    @Test
    public void testScreenshot_withCourseDiscoveryEnabled() {
        view.setEnabledButtons(true);
    }
}
