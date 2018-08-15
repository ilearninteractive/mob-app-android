package sa.gov.moe.etraining.base;

import android.content.Context;
import android.os.Bundle;

import de.greenrobot.event.EventBus;
import sa.gov.moe.etraining.event.NewRelicEvent;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public abstract class BaseAppActivity extends RoboAppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().post(new NewRelicEvent(getClass().getSimpleName()));
    }
}
