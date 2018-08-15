package sa.gov.moe.etraining.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import sa.gov.moe.etraining.R;

import sa.gov.moe.etraining.event.ProfilePhotoUpdatedEvent;

public class UserProfileUtils {
    public static void loadProfileImage(@NonNull Context context,
                                        @NonNull ProfilePhotoUpdatedEvent event,
                                        @NonNull ImageView view) {
        if (null == event.getUri()) {
            Glide.with(context)
                    .load(R.drawable.profile_photo_placeholder)
                    .into(view);
        } else {
            Glide.with(context)
                    .load(event.getUri())
                    .skipMemoryCache(true) // URI is re-used in subsequent events; disable caching
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(view);
        }
    }
}
