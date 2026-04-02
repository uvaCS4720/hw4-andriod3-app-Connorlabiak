package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PlacemarkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacemarks(games: List<Placemark>)

    @Query("SELECT * FROM placemark_table")
    suspend fun getPlacemarks(): List<Placemark>

}