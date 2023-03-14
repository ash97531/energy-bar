package com.example.energybar

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Energy::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun energyDao(): EnergyDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            
            //hello world rh
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase :: class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance

            }

        }
    }
}