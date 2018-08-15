package sa.gov.moe.etraining.test;

import android.support.annotation.Nullable;

import sa.gov.moe.etraining.base.MainApplication;
import sa.gov.moe.etraining.view.Presenter;

public class EdxInstrumentationTestApplication extends MainApplication implements PresenterInjector {

    @Nullable
    private Presenter<?> nextPresenter = null;

    @Nullable
    @Override
    public Presenter<?> getPresenter() {
        try {
            return nextPresenter;
        } finally {
            nextPresenter = null;
        }
    }

    public void setNextPresenter(@Nullable Presenter<?> nextPresenter) {
        this.nextPresenter = nextPresenter;
    }
}
