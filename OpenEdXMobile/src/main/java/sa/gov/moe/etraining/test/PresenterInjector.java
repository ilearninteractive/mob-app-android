package sa.gov.moe.etraining.test;

import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import sa.gov.moe.etraining.view.Presenter;

@VisibleForTesting
public interface PresenterInjector {
    @Nullable
    Presenter<?> getPresenter();
}
