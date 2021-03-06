package com.robdir.themoviedb.di

import com.robdir.themoviedb.App
import com.robdir.themoviedb.di.module.ActivityBuilderModule
import com.robdir.themoviedb.di.module.AppModule
import com.robdir.themoviedb.di.module.MoviesModule
import com.robdir.themoviedb.di.module.NetworkModule
import com.robdir.themoviedb.di.module.PersistenceModule
import com.robdir.themoviedb.di.module.viewmodel.ViewModelModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule

@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuilderModule::class,
        AppModule::class,
        NetworkModule::class,
        ViewModelModule::class,
        MoviesModule::class,
        PersistenceModule::class
    ]
)
@AppScope
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<App>()
}
