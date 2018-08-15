package sa.gov.moe.etraining.profiles;

import android.databinding.DataBindingUtil;

import sa.gov.moe.etraining.databinding.FragmentUserProfileBinding;
import sa.gov.moe.etraining.databinding.FragmentUserProfileBioBinding;
import sa.gov.moe.etraining.view.PresenterFragmentTest;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.verify;

public class UserProfileBioFragmentTest extends PresenterFragmentTest<UserProfileBioFragment, UserProfileBioPresenter, UserProfileBioPresenter.ViewInterface> {

    FragmentUserProfileBioBinding binding;

    @Before
    public void before() {
        startFragment(UserProfileBioFragment.newInstance());
        binding = DataBindingUtil.getBinding(fragment.getView());
        assertThat(binding).isNotNull();
    }
    @Test
    public void click_onParentalConsentEditProfileButton_callsEditProfile() {
        binding.parentalConsentEditProfileButton.performClick();
        verify(presenter).onEditProfile();
    }

    @Test
    public void click_onIncompleteEditProfileButton_callsEditProfile() {
        binding.incompleteEditProfileButton.performClick();
        verify(presenter).onEditProfile();
    }

}
