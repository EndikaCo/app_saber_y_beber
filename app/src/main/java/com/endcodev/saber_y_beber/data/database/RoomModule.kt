package com.endcodev.saber_y_beber.data.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    private const val DB = "challenge_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, RoomDB::class.java, DB).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideQuestDao(db: RoomDB) = db.getQuestDao()

    @Singleton
    @Provides
    fun provideChallengeDao(ab: RoomDB) = ab.getChallengeDao()

    @Singleton
    @Provides
    fun providePlayerDao(pd: RoomDB) = pd.getPlayerDao()
}