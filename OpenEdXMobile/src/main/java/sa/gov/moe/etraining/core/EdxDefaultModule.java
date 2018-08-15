package sa.gov.moe.etraining.core;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import de.greenrobot.event.EventBus;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import sa.gov.moe.etraining.authentication.LoginService;
import sa.gov.moe.etraining.course.CourseService;
import sa.gov.moe.etraining.discussion.DiscussionService;
import sa.gov.moe.etraining.discussion.DiscussionTextUtils;
import sa.gov.moe.etraining.http.provider.OkHttpClientProvider;
import sa.gov.moe.etraining.http.provider.RetrofitProvider;
import sa.gov.moe.etraining.http.serialization.ISO8601DateTypeAdapter;
import sa.gov.moe.etraining.http.serialization.JsonPageDeserializer;
import sa.gov.moe.etraining.http.util.CallUtil;
import sa.gov.moe.etraining.model.Page;
import sa.gov.moe.etraining.model.course.BlockData;
import sa.gov.moe.etraining.model.course.BlockList;
import sa.gov.moe.etraining.model.course.BlockType;
import sa.gov.moe.etraining.module.db.IDatabase;
import sa.gov.moe.etraining.module.db.impl.IDatabaseImpl;
import sa.gov.moe.etraining.module.download.IDownloadManager;
import sa.gov.moe.etraining.module.download.IDownloadManagerImpl;
import sa.gov.moe.etraining.module.notification.DummyNotificationDelegate;
import sa.gov.moe.etraining.module.notification.NotificationDelegate;
import sa.gov.moe.etraining.module.storage.IStorage;
import sa.gov.moe.etraining.module.storage.Storage;
import sa.gov.moe.etraining.user.UserService;
import sa.gov.moe.etraining.util.AppStoreUtils;
import sa.gov.moe.etraining.util.BrowserUtil;
import sa.gov.moe.etraining.util.Config;
import sa.gov.moe.etraining.util.MediaConsentUtils;

public class EdxDefaultModule extends AbstractModule {
    //if your module requires a context, add a constructor that will be passed a context.
    private Context context;

    //with RoboGuice 3.0, the constructor for AbstractModule will use an `Application`, not a `Context`
    public EdxDefaultModule(Context context) {
        this.context = context;
    }

    @Override
    public void configure() {
        Config config = new Config(context);

        bind(IDatabase.class).to(IDatabaseImpl.class);
        bind(IDownloadManager.class).to(IDownloadManagerImpl.class);

        bind(NotificationDelegate.class).to(DummyNotificationDelegate.class);

        bind(IEdxEnvironment.class).to(EdxEnvironment.class);

        bind(LinearLayoutManager.class).toProvider(LinearLayoutManagerProvider.class);

        bind(EventBus.class).toInstance(EventBus.getDefault());

        bind(Gson.class).toInstance(new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .registerTypeAdapterFactory(ISO8601DateTypeAdapter.FACTORY)
                .registerTypeAdapter(Page.class, new JsonPageDeserializer())
                .registerTypeAdapter(BlockData.class, new BlockData.Deserializer())
                .registerTypeAdapter(BlockType.class, new BlockType.Deserializer())
                .registerTypeAdapter(BlockList.class, new BlockList.Deserializer())
                .serializeNulls()
                .create());

        bind(OkHttpClientProvider.class).to(OkHttpClientProvider.Impl.class);
        bind(RetrofitProvider.class).to(RetrofitProvider.Impl.class);
        bind(OkHttpClient.class).toProvider(OkHttpClientProvider.Impl.class).in(Singleton.class);
        bind(Retrofit.class).toProvider(RetrofitProvider.Impl.class).in(Singleton.class);

        bind(LoginService.class).toProvider(LoginService.Provider.class).in(Singleton.class);
        bind(CourseService.class).toProvider(CourseService.Provider.class).in(Singleton.class);
        bind(DiscussionService.class).toProvider(DiscussionService.Provider.class).in(Singleton.class);
        bind(UserService.class).toProvider(UserService.Provider.class).in(Singleton.class);

        bind(IStorage.class).to(Storage.class);

        requestStaticInjection(CallUtil.class, BrowserUtil.class, MediaConsentUtils.class,
                DiscussionTextUtils.class, AppStoreUtils.class);
    }
}
