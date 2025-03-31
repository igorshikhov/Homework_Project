package otus.project.feature.db

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import otus.project.feature.db.MarkerDao
import otus.project.feature.db.MarkerDatabase

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideMarkerDao(db : otus.project.feature.db.MarkerDatabase) : otus.project.feature.db.MarkerDao {
        return db.getDao()
    }

    @Singleton
    @Provides
    fun provideMarkerDatabase(@ApplicationContext context : Context) : otus.project.feature.db.MarkerDatabase {
        return otus.project.feature.db.MarkerDatabase.provideDatabase(context)
    }
}

