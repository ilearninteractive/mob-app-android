package sa.gov.moe.etraining.task;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.google.inject.Inject;

import java.lang.ref.WeakReference;

import roboguice.util.RoboAsyncTask;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.http.callback.CallTrigger;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.util.images.ErrorUtils;
import sa.gov.moe.etraining.view.common.MessageType;
import sa.gov.moe.etraining.view.common.TaskMessageCallback;
import sa.gov.moe.etraining.view.common.TaskProcessCallback;
import sa.gov.moe.etraining.view.common.TaskProgressCallback;

/**
 * This class is deprecated. Issues in it's
 * implementation include the lack of a guarantee of the
 * result not being delivered to the callback method
 * after cancellation.
 *
 * New asynchronous HTTP request implementations should
 * consider using Retrofit's asynchronous API. If that's
 * not sufficient, or if the implementation is not of an
 * HTTP call, then AsyncTask or Loader implementations
 * should be considered instead.
 */
@Deprecated
public abstract class Task<T> extends RoboAsyncTask<T> {

    protected final Handler handler = new Handler();
    protected final Logger logger = new Logger(getClass().getName());

    @Nullable
    private WeakReference<TaskProgressCallback> progressCallback;

    @Nullable
    private WeakReference<TaskMessageCallback> messageCallback;

    private View progressView;

    @Inject
    protected IEdxEnvironment environment;

    private final CallTrigger callTrigger;

    public Task(Context context) {
        this(context, CallTrigger.LOADING_UNCACHED);
    }

    public Task(Context context, CallTrigger callTrigger) {
        super(context);

        if (context instanceof TaskProcessCallback) {
            setTaskProcessCallback((TaskProcessCallback) context);
        }
        this.callTrigger = callTrigger;
    }

    public void setProgressDialog(@Nullable View progressView) {
        this.progressView = progressView;
        if (progressView != null) {
            this.progressCallback = null;
        }
    }

    public void setTaskProcessCallback(@Nullable TaskProcessCallback callback) {
        setProgressCallback(callback);
        setMessageCallback(callback);
    }

    public void setProgressCallback(@Nullable TaskProgressCallback callback) {
        if (callback == null) {
            progressCallback = null;
        } else {
            progressCallback = new WeakReference<>(callback);
            progressView = null;
        }
    }

    public void setMessageCallback(@Nullable TaskMessageCallback callback) {
        messageCallback = callback == null ? null : new WeakReference<>(callback);
    }

    @Nullable
    private TaskProgressCallback getProgressCallback() {
        return progressCallback == null ? null : progressCallback.get();
    }

    @Nullable
    private TaskMessageCallback getMessageCallback() {
        return messageCallback == null ? null : messageCallback.get();
    }

    @Override
    protected void onPreExecute() {
        if (progressView != null) {
            progressView.setVisibility(View.VISIBLE);
        }
        final TaskProgressCallback callback = getProgressCallback();
        if (callback != null) {
            callback.startProcess();
        }
    }

    @Override
    protected void onFinally() {
        stopProgress();
    }

    protected void stopProgress() {
        if (progressView != null) {
            progressView.setVisibility(View.GONE);
        }
        final TaskProgressCallback callback = getProgressCallback();
        if (callback != null) {
            callback.finishProcess();
        }
    }

    @Override
    protected void onException(Exception ex) {
        final TaskMessageCallback callback = getMessageCallback();
        if (callback == null) {
            return;
        }

        callback.onMessage(getMessageType(), ErrorUtils.getErrorMessage(ex, callTrigger, context));
    }

    /**
     * @return The {@link MessageType} based on the {@link #callTrigger}.
     */
    private MessageType getMessageType() {
        switch (callTrigger) {
            case USER_ACTION:
                return MessageType.DIALOG;
            case LOADING_CACHED:
            case LOADING_UNCACHED:
            default:
                return MessageType.FLYIN_ERROR;
        }
    }
}
