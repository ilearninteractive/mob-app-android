package sa.gov.moe.etraining.test.feature;

import android.support.test.runner.AndroidJUnit4;

import sa.gov.moe.etraining.base.MainApplication;
import sa.gov.moe.etraining.core.EdxEnvironment;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public abstract class FeatureTest {
    protected EdxEnvironment environment;

    @Before
    public void setup() {
        // Ensure we are not logged in
        final MainApplication application = MainApplication.instance();
        environment = application.getInjector().getInstance(EdxEnvironment.class);
        environment.getLoginPrefs().clear();
        environment.getAnalyticsRegistry().resetIdentifyUser();
    }
}
