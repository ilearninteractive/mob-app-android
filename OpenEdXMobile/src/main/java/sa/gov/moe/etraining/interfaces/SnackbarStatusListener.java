package sa.gov.moe.etraining.interfaces;

import sa.gov.moe.etraining.http.notifications.FullScreenErrorNotification;

/**
 * Provides callbacks to control the visibility of {@link android.support.design.widget.Snackbar Snackbar}.
 */
public interface SnackbarStatusListener {
    /**
     * Hide {@link android.support.design.widget.Snackbar Snackbar} if its being displayed.
     */
    void hideSnackBar();

    /**
     * Set the visibility of {@link android.support.design.widget.Snackbar Snackbar} based on the
     * visibility of {@link FullScreenErrorNotification FullScreenErrorNotification}.
     * <br/>
     * Note: At one time only one type of error i.e. either SnackBar or Full Screen error should be
     * visible on screen.
     *
     * @param fullScreenErrorVisibility Visibility of {@link FullScreenErrorNotification FullScreenErrorNotification}.
     */
    void resetSnackbarVisibility(boolean fullScreenErrorVisibility);
}
