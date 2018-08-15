package sa.gov.moe.etraining.util.observer;

import android.support.annotation.NonNull;

public interface Observable<T> {
    @NonNull
    Subscription subscribe(@NonNull final Observer<T> observer);
}
