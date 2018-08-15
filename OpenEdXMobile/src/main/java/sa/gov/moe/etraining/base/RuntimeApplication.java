package sa.gov.moe.etraining.base;

import javax.inject.Inject;

import sa.gov.moe.etraining.view.ExtensionRegistry;

/**
 * Put any custom application configuration here.
 * This file will not be edited by edX unless absolutely necessary.
 */
public class RuntimeApplication extends MainApplication {

    @SuppressWarnings("unused")
    @Inject
    ExtensionRegistry extensionRegistry;

    @Override
    public void onCreate() {
        super.onCreate();
        // If you have any custom extensions, add them here. For example:
        // extensionRegistry.forType(SettingsExtension.class).add(new MyCustomSettingsExtension());
    }
}
