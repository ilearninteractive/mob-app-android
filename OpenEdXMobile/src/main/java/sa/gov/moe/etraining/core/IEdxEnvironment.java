package sa.gov.moe.etraining.core;


import sa.gov.moe.etraining.module.analytics.AnalyticsRegistry;
import sa.gov.moe.etraining.module.db.IDatabase;
import sa.gov.moe.etraining.module.download.IDownloadManager;
import sa.gov.moe.etraining.module.notification.NotificationDelegate;
import sa.gov.moe.etraining.module.prefs.LoginPrefs;
import sa.gov.moe.etraining.module.prefs.UserPrefs;
import sa.gov.moe.etraining.module.storage.IStorage;
import sa.gov.moe.etraining.util.Config;
import sa.gov.moe.etraining.view.Router;

/**
 * TODO - we should decompose this class into environment setting and service provider settings.
 */
public interface IEdxEnvironment {

    IDatabase getDatabase();

    IStorage getStorage();

    IDownloadManager getDownloadManager();

    UserPrefs getUserPrefs();

    LoginPrefs getLoginPrefs();

    AnalyticsRegistry getAnalyticsRegistry();

    NotificationDelegate getNotificationDelegate();

    Router getRouter();

    Config getConfig();
}
