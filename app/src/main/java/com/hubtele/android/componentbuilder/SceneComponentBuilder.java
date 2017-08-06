package com.hubtele.android.componentbuilder;

import com.hubtele.android.internal.di.component.AppComponent;
import com.hubtele.android.internal.di.component.DaggerSceneComponent;
import com.hubtele.android.internal.di.component.SceneComponent;
import com.hubtele.android.internal.di.module.RestRepositoryModule;

/**
 * Created by nakama on 2015/10/27.
 */
public class SceneComponentBuilder {
    public static SceneComponent init(AppComponent appComponent){
        return DaggerSceneComponent.builder()
                .restRepositoryModule(new RestRepositoryModule())
                .appComponent(appComponent)
                        .build();
    }
}
