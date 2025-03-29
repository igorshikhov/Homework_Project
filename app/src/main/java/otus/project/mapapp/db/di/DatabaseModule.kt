package otus.project.mapapp.db.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import otus.project.mapapp.db.MarkerDao
import otus.project.mapapp.db.MarkerDatabase

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMarkerDao(db : MarkerDatabase) : MarkerDao {
        return db.getDao()
    }

    @Singleton
    @Provides
    fun provideMarkerDatabase(@ApplicationContext context : Context) : MarkerDatabase {
        return MarkerDatabase.provideDatabase(context)
    }
}

