package sa.gov.moe.etraining.view.common;

import android.support.annotation.NonNull;

public interface TaskMessageCallback {
    void onMessage(@NonNull MessageType messageType, @NonNull String message);
}
