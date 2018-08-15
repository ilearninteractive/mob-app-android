package sa.gov.moe.etraining.util;

import android.content.Context;

import com.google.inject.Inject;

import okhttp3.Request;
import okhttp3.Response;
import roboguice.RoboGuice;
import sa.gov.moe.etraining.http.HttpStatusException;
import sa.gov.moe.etraining.http.provider.OkHttpClientProvider;
import sa.gov.moe.etraining.logger.Logger;

public abstract class TranscriptDownloader implements Runnable {

    private String srtUrl;
    @Inject
    private OkHttpClientProvider okHttpClientProvider;
    private final Logger logger = new Logger(TranscriptDownloader.class.getName());

    public TranscriptDownloader(Context context, String url) {
        this.srtUrl = url;
        RoboGuice.getInjector(context).injectMembers(this);
    }

    @Override
    public void run() {
        try {
            final Response response = okHttpClientProvider.getWithOfflineCache()
                    .newCall(new Request.Builder()
                            .url(srtUrl)
                            .get()
                            .build())
                    .execute();
            if (!response.isSuccessful()) {
                throw new HttpStatusException(response);
            }
            onDownloadComplete(response.body().string());
        } catch (Exception localException) {
            handle(localException);
            logger.error(localException);
        }
    }

    public abstract void handle(Exception ex);

    public abstract void onDownloadComplete(String response);
}
