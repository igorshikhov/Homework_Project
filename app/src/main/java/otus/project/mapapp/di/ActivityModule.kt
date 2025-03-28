package otus.project.mapapp.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import otus.project.mapapp.db.MarkerDatabase
import otus.project.mapapp.db.provideDatabase
import javax.inject.Singleton
/*
@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Singleton
    @Provides
    fun provideMarkerDatabase(@ApplicationContext context : Context) : MarkerDatabase {
        return provideDatabase(context)
    }

//    @Singleton
//    @Provides
//    fun provideMarkerStore(@ApplicationContext context : Context) : MarkerStore {
//        return MarkerStore(context)
//    }
}
*/
