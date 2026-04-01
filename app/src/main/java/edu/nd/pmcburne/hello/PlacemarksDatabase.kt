package edu.nd.pmcburne.hello

import android.content.Context
import androidx.room.*

@Database(entities = [Placemark::class], version = 1)
abstract class GamesDatabase: RoomDatabase() {
    abstract fun gameDao(): PlacemarkDAO

    companion object {
        @Volatile
        private var INSTANCE: GamesDatabase? = null

        fun getDatabase(context: Context): GamesDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GamesDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}