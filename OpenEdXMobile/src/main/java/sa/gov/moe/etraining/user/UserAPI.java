package sa.gov.moe.etraining.user;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.webkit.MimeTypeMap;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.File;

import de.greenrobot.event.EventBus;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import sa.gov.moe.etraining.event.AccountDataLoadedEvent;
import sa.gov.moe.etraining.http.callback.CallTrigger;
import sa.gov.moe.etraining.http.callback.ErrorHandlingCallback;
import sa.gov.moe.etraining.http.notifications.ErrorNotification;
import sa.gov.moe.etraining.module.prefs.LoginPrefs;
import sa.gov.moe.etraining.view.common.TaskMessageCallback;
import sa.gov.moe.etraining.view.common.TaskProgressCallback;

@Singleton
public class UserAPI {
    @Inject
    private UserService userService;

    public static class AccountDataUpdatedCallback extends ErrorHandlingCallback<Account> {
        @Inject
        private LoginPrefs loginPrefs;
        @NonNull
        private final String username;

        public AccountDataUpdatedCallback(@NonNull final Context context,
                                          @NonNull final String username,
                                          @Nullable final ErrorNotification errorNotification) {
            this(context, username, null, errorNotification);
        }

        public AccountDataUpdatedCallback(@NonNull final Context context,
                                          @NonNull final String username,
                                          @Nullable final TaskProgressCallback progressCallback,
                                          @Nullable final ErrorNotification errorNotification) {
            super(context, progressCallback, errorNotification);
            this.username = username;
        }

        //TODO: Remove this legacy code starting from here, when modern error design has been implemented on all screens i.e. SnackBar, FullScreen and Dialog based errors.
        public AccountDataUpdatedCallback(@NonNull final Context context,
                                          @NonNull final String username,
                                          @Nullable final TaskProgressCallback progressCallback,
                                          @Nullable TaskMessageCallback messageCallback,
                                          @Nullable CallTrigger callTrigger) {
            super(context, progressCallback, messageCallback, callTrigger);
            this.username = username;
        }
        // LEGACY CODE ENDS HERE, all occurrences of this constructor should also be updated in future

        @Override
        protected void onResponse(@NonNull final Account account) {
            EventBus.getDefault().post(new AccountDataLoadedEvent(account));
            // Store the logged in user's ProfileImage
            loginPrefs.setProfileImage(username, account.getProfileImage());
        }
    }

    public Call<ResponseBody> setProfileImage(@NonNull String username, @NonNull final File file) {
        final String mimeType = "image/jpeg";
        return userService.setProfileImage(
                username,
                "attachment;filename=filename." + MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType),
                RequestBody.create(MediaType.parse(mimeType), file));
    }
}
