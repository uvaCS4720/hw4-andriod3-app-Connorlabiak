package edu.nd.pmcburne.hello

import android.content.Context
import androidx.room.*

@Database(entities = [Placemark::class], version = 1)
@TypeConverters(Converters::class)
abstract class PlacemarksDatabase: RoomDatabase() {
    abstract fun gameDao(): PlacemarkDAO

    companion object {
        @Volatile
        private var INSTANCE: PlacemarksDatabase? = null

        fun getDatabase(context: Context): PlacemarksDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PlacemarksDatabase::class.java,
                    "game_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}