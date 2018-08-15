package sa.gov.moe.etraining.base;


import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.bumptech.glide.Glide;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.newrelic.agent.android.NewRelic;

import sa.gov.moe.etraining.BuildConfig;
import sa.gov.moe.etraining.R;

import java.io.InputStream;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;
import io.branch.referral.Branch;
import io.fabric.sdk.android.Fabric;
import roboguice.RoboGuice;
import sa.gov.moe.etraining.core.EdxDefaultModule;
import sa.gov.moe.etraining.core.IEdxEnvironment;
import sa.gov.moe.etraining.event.AppUpdatedEvent;
import sa.gov.moe.etraining.event.NewRelicEvent;
import sa.gov.moe.etraining.http.provider.OkHttpClientProvider;
import sa.gov.moe.etraining.logger.Logger;
import sa.gov.moe.etraining.module.analytics.AnalyticsRegistry;
import sa.gov.moe.etraining.module.analytics.AnswersAnalytics;
import sa.gov.moe.etraining.module.analytics.FirebaseAnalytics;
import sa.gov.moe.etraining.module.analytics.SegmentAnalytics;
import sa.gov.moe.etraining.module.prefs.PrefManager;
import sa.gov.moe.etraining.module.storage.IStorage;
import sa.gov.moe.etraining.receivers.NetworkConnectivityReceiver;
import sa.gov.moe.etraining.util.Config;
import sa.gov.moe.etraining.util.NetworkUtil;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * This class initializes the modules of the app based on the configuration.
 */
public abstract class MainApplication extends MultiDexApplication {

    protected final Logger logger = new Logger(getClass().getName());

    public static MainApplication application;

    public static final MainApplication instance() {
        return application;
    }

    private Injector injector;

    @Inject
    protected Config config;

    @Inject
    protected AnalyticsRegistry analyticsRegistry;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * Initializes the request manager, image cache,
     * all third party integrations and shared components.
     */
    private void init() {
        application = this;
        injector = RoboGuice.getOrCreateBaseApplicationInjector((Application) this, RoboGuice.DEFAULT_STAGE,
                (Module) RoboGuice.newDefaultRoboModule(this), (Module) new EdxDefaultModule(this));

        injector.injectMembers(this);

        // initialize Fabric
        if (config.getFabricConfig().isEnabled() && !BuildConfig.DEBUG) {
            Fabric.with(this, config.getFabricConfig().getKitsConfig().getEnabledKits());

            if (config.getFabricConfig().getKitsConfig().isCrashlyticsEnabled()) {
                EventBus.getDefault().register(new CrashlyticsCrashReportObserver());
            }

            if (config.getFabricConfig().getKitsConfig().isAnswersEnabled())  {
                analyticsRegistry.addAnalyticsProvider(injector.getInstance(AnswersAnalytics.class));
            }
        }

        if (config.getNewRelicConfig().isEnabled()) {
            EventBus.getDefault().register(new NewRelicObserver());
        }

        // initialize NewRelic with crash reporting disabled
        if (config.getNewRelicConfig().isEnabled()) {
            //Crash reporting for new relic has been disabled
            NewRelic.withApplicationToken(config.getNewRelicConfig().getNewRelicKey())
                    .withCrashReportingEnabled(false)
                    .start(this);
        }

        // Add Segment as an analytics provider if enabled in the config
        if (config.getSegmentConfig().isEnabled())  {
            analyticsRegistry.addAnalyticsProvider(injector.getInstance(SegmentAnalytics.class));
        }

        // Add Firebase as an analytics provider if enabled in the config
        if (config.isFirebaseEnabled())  {
            analyticsRegistry.addAnalyticsProvider(injector.getInstance(FirebaseAnalytics.class));
        }

        registerReceiver(new NetworkConnectivityReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(new NetworkConnectivityReceiver(), new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));

        // initialize Facebook SDK
        boolean isOnZeroRatedNetwork = NetworkUtil.isOnZeroRatedNetwork(getApplicationContext(), config);
        if (!isOnZeroRatedNetwork
                && config.getFacebookConfig().isEnabled()) {
            com.facebook.Settings.setApplicationId(config.getFacebookConfig().getFacebookAppId());
        }

        checkIfAppVersionUpgraded(this);

        // Register Font Awesome module in android-iconify library
        Iconify.with(new FontAwesomeModule());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        // Init Branch
        if (Config.FabricBranchConfig.isBranchEnabled(config.getFabricConfig())) {
            Branch.getAutoInstance(this);
        }

        // Force Glide to use our version of OkHttp which now supports TLS 1.2 out-of-the-box for
        // Pre-Lollipop devices
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Glide.get(this).register(GlideUrl.class, InputStream.class,
                    new OkHttpUrlLoader.Factory(injector.getInstance(OkHttpClientProvider.class).get()));
        }
    }

    private void checkIfAppVersionUpgraded(Context context) {
        PrefManager.AppInfoPrefManager prefManager = new PrefManager.AppInfoPrefManager(context);
        long previousVersionCode = prefManager.getAppVersionCode();
        final long curVersionCode = BuildConfig.VERSION_CODE;
        if (previousVersionCode < 0) {
            // App opened first time after installation
            // Save version code and name in preferences
            prefManager.setAppVersionCode(curVersionCode);
            prefManager.setAppVersionName(BuildConfig.VERSION_NAME);
            logger.debug("App opened first time, VersionCode:"+curVersionCode);
        } else if (previousVersionCode < curVersionCode) {
            final String previousVersionName = prefManager.getAppVersionName();
            // Update version code and name in preferences
            prefManager.setAppVersionCode(curVersionCode);
            prefManager.setAppVersionName(BuildConfig.VERSION_NAME);
            logger.debug("App updated, VersionCode:"+previousVersionCode+"->"+curVersionCode);
            // App updated
            onAppUpdated(previousVersionCode, curVersionCode, previousVersionName, BuildConfig.VERSION_NAME);
        }
    }

    private void onAppUpdated(final long previousVersionCode, final long curVersionCode,
                             final String previousVersionName, final String curVersionName) {
        // Try repair of download data on updating of app version
        injector.getInstance(IStorage.class).repairDownloadCompletionData();
        // Fire app updated event
        EventBus.getDefault().postSticky(new AppUpdatedEvent(previousVersionCode, curVersionCode,
                previousVersionName, curVersionName));
    }

    public static class CrashlyticsCrashReportObserver {
        @SuppressWarnings("unused")
        public void onEventMainThread(Logger.CrashReportEvent e) {
            CrashlyticsCore.getInstance().logException(e.getError());
        }
    }

    public static class NewRelicObserver {
        @SuppressWarnings("unused")
        public void onEventMainThread(NewRelicEvent e) {
            NewRelic.setInteractionName("Display " + e.getScreenName());
        }
    }

    public Injector getInjector() {
        return injector;
    }

    @NonNull
    public static IEdxEnvironment getEnvironment(@NonNull Context context) {
        return RoboGuice.getInjector(context.getApplicationContext()).getInstance(IEdxEnvironment.class);
    }
}
