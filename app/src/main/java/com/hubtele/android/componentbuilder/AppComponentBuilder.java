package com.hubtele.android.componentbuilder;

import com.hubtele.android.internal.di.component.AppComponent;
import com.hubtele.android.internal.di.component.DaggerAppComponent;
import com.hubtele.android.internal.di.module.ChatStreamModule;
import com.hubtele.android.internal.di.module.RestModule;

/**
 * Created by nakama on 2015/11/04.
 */
public class AppComponentBuilder {

    public static AppComponent init() {
        return DaggerAppComponent.builder().restModule(new RestModule()).chatStreamModule(new ChatStreamModule()).build();
    }

    public static AppComponent init(RestModule restModule) {
        return DaggerAppComponent.builder().restModule(restModule)
                .build();
    }
    public static AppComponent init(RestModule restModule,ChatStreamModule chatStream) {
        return DaggerAppComponent.builder().restModule(restModule).chatStreamModule(chatStream)
                .build();
    }

    public static AppComponent init(ChatStreamModule chatStream) {
        return DaggerAppComponent.builder().chatStreamModule(chatStream)
                .build();
    }
}
