package sa.gov.moe.etraining.test.feature;

import sa.gov.moe.etraining.test.feature.data.Credentials;
import sa.gov.moe.etraining.test.feature.interactor.AppInteractor;
import org.junit.Test;

public class RegisterFeatureTest extends FeatureTest {
    @Test
    public void afterRegistering_withFreshCredentials_myCoursesScreenIsDisplayed() {
        new AppInteractor()
                .launchApp()
                .observeLandingScreen()
                .navigateToRegistrationScreen()
                .observeRegistrationScreen()
                .createAccount(Credentials.freshCredentials(environment.getConfig()))
                .observeMyCoursesScreen();
    }
}
