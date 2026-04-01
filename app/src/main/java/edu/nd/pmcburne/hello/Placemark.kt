package edu.nd.pmcburne.hello

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placemark_table")
data class Placemark(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val description: String,
    val name: String,
    val tagList: List<String>,
    val latitude: Double,
    val longitude: Double
)
