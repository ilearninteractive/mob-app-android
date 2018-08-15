package sa.gov.moe.etraining.test.http;

import sa.gov.moe.etraining.model.api.ProfileModel;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class SocialLoginTests extends HttpBaseTestCase {
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Test
    public void testGetProfile() throws Exception {
        ProfileModel profile = loginAPI.getProfile();
        assertNotNull(profile);
        assertNotNull("profile.email cannot be null", profile.email);
        print("finished getProfile");
    }
}
