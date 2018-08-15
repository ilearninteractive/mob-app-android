package sa.gov.moe.etraining.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import sa.gov.moe.etraining.base.BaseFragmentActivity;
import sa.gov.moe.etraining.test.PresenterInjector;

public abstract class PresenterActivity<P extends Presenter<V>, V> extends BaseFragmentActivity {

    protected V view;

    protected P presenter;

    @NonNull
    abstract protected P createPresenter(@Nullable Bundle savedInstanceState);

    @NonNull
    abstract protected V createView(@Nullable Bundle savedInstanceState);

    @Override
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null == presenter) {
            presenter = getLastCustomNonConfigurationInstance();
            if (null == presenter) {
                if (getApplication() instanceof PresenterInjector) {
                    //noinspection unchecked
                    presenter = (P)((PresenterInjector) getApplication()).getPresenter();
                }
                if (null == presenter) {
                    presenter = createPresenter(savedInstanceState);
                }
            }
        }
        view = createView(savedInstanceState);
        presenter.attachView(view);
        super.setToolbarAsActionBar();
    }

    @SuppressWarnings("unchecked")
    @Override
    public final P getLastCustomNonConfigurationInstance() {
        return (P) super.getLastCustomNonConfigurationInstance();
    }

    @Override
    public final P onRetainCustomNonConfigurationInstance() {
        return presenter;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != presenter) {
            presenter.detachView();
            presenter.destroy();
        }
    }
}
