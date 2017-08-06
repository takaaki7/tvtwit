package com.hubtele.android;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDex;
//import android.support.multidex.MultiDexApplication;

import com.deploygate.sdk.DeployGate;
//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.hubtele.android.componentbuilder.AppComponentBuilder;
import com.hubtele.android.internal.di.component.AppComponent;
import com.hubtele.android.internal.di.module.ChatStreamModule;
import com.hubtele.android.internal.di.module.RestModule;
import com.hubtele.android.util.ActivityLifecycleLogger;
import com.squareup.leakcanary.LeakCanary;

import timber.log.Timber;

/**
 * Created by nakama on 2015/10/27.
 */
public class MyApplication extends Application {
    public AppComponent component;
    private static Context context;
    private Tracker tracker;

    public static Context getContext() {
        return context;
    }

    private ActivityLifecycleCallbacks logger = new ActivityLifecycleLogger();

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.d("MyApplication onCreate");
        DeployGate.install(this);
        LeakCanary.install(this);
        component = DaggerComponentInitializer.init(this, new RestModule(), new ChatStreamModule());
//                DaggerAppComponent.builder()
//                .restModule(new RestModule())
//                .build();
        context = getApplicationContext();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            registerActivityLifecycleCallbacks(logger);
        }
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            tracker = analytics.newTracker(R.xml.global_tracker);
        }
        return tracker;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public final static class DaggerComponentInitializer {
        public static AppComponent init(MyApplication app, RestModule rest, ChatStreamModule chat) {
            return AppComponentBuilder.init(rest, chat);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unregisterActivityLifecycleCallbacks(logger);
    }
}
