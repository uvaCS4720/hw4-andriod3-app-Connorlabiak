package edu.nd.pmcburne.hello

import androidx.room.Insert
import androidx.room.OnConflictStrategy

interface PlacemarkDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlacemarks(games: List<Placemark>)
}