package sa.gov.moe.etraining.base;

import android.app.Activity;
import android.os.Bundle;

import de.greenrobot.event.EventBus;
import roboguice.fragment.RoboFragment;
import sa.gov.moe.etraining.event.NewRelicEvent;

public class BaseFragment extends RoboFragment {
    private boolean isFirstVisit = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().post(new NewRelicEvent(getClass().getSimpleName()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstVisit) {
            isFirstVisit = false;
        } else {
            onRevisit();
        }
    }

    /**
     * Called when a Fragment is re-displayed to the user (the user has navigated back to it).
     * Defined to mock the behavior of {@link Activity#onRestart() Activity.onRestart} function.
     */
    protected void onRevisit() {
    }
}
