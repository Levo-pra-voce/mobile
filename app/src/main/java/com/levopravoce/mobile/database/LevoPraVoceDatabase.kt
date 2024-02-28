package com.levopravoce.mobile.database;

import android.content.Context
import androidx.room.Database;
import androidx.room.Room
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters
import com.levopravoce.mobile.database.converter.Converters
import com.levopravoce.mobile.features.chat.data.MessageDao

import com.levopravoce.mobile.features.chat.data.entity.Message;

@Database(entities = [(Message::class)], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class LevoPraVoceDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {

        const val DATABASE_NAME = "levo_pra_voce_database"

        @Volatile
        private var INSTANCE: LevoPraVoceDatabase? = null

        fun getInstance(context: Context): LevoPraVoceDatabase {
            // only one thread of execution at a time can enter this block of code
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LevoPraVoceDatabase::class.java,
                        DATABASE_NAME
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}
