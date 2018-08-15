package sa.gov.moe.etraining.view;

import android.os.Bundle;

import sa.gov.moe.etraining.R;

import de.greenrobot.event.EventBus;
import sa.gov.moe.etraining.base.BaseSingleFragmentActivity;
import sa.gov.moe.etraining.http.notifications.FullScreenErrorNotification;
import sa.gov.moe.etraining.http.notifications.SnackbarErrorNotification;
import sa.gov.moe.etraining.interfaces.RefreshListener;
import sa.gov.moe.etraining.interfaces.SnackbarStatusListener;
import sa.gov.moe.etraining.util.NetworkUtil;

/**
 * Provides support for offline mode handling within an Activity.
 * <br/>
 * Ensures that no two types of errors appear at the same time in an Activity e.g. if
 * {@link FullScreenErrorNotification} is already appearing in an activity
 * {@link SnackbarErrorNotification} should never appear until and unless the
 * {@link FullScreenErrorNotification} is hidden.
 */
public abstract class OfflineSupportBaseActivity extends BaseSingleFragmentActivity
        implements SnackbarStatusListener, RefreshListener {

    private SnackbarErrorNotification snackbarErrorNotification;
    private boolean isFullScreenErrorVisible = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snackbarErrorNotification = new SnackbarErrorNotification(findViewById(R.id.coordinator_layout));
    }

    @Override
    public void hideSnackBar() {
        snackbarErrorNotification.hideError();
    }

    @Override
    public void resetSnackbarVisibility(boolean fullScreenErrorVisibility) {
        this.isFullScreenErrorVisible = fullScreenErrorVisibility;
        final boolean isNetworkConnected = NetworkUtil.isConnected(this);
        if (fullScreenErrorVisibility || isNetworkConnected) {
            snackbarErrorNotification.hideError();
        } else if (!isNetworkConnected) {
            snackbarErrorNotification.showOfflineError(this);
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        if (NetworkUtil.isConnected(this)) {
            snackbarErrorNotification.hideError();
        } else if (!isFullScreenErrorVisible) {
            snackbarErrorNotification.showOfflineError(this);
        }
    }

    @Override
    public void onRefresh() {
        EventBus.getDefault().post(getRefreshEvent());
    }

    /**
     * Provides an object that we will be needing to fire on {@link EventBus} whenever some screen state
     * needs to be refreshed.
     *
     * @return The event object.
     */
    public abstract Object getRefreshEvent();
}
