package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.APIResponseObjects.APIResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(application: Application): ViewModel() {
    private val dao = GamesDatabase.getDatabase(application.applicationContext).gameDao()
    private val _currentList: MutableStateFlow<List<Placemark>> = MutableStateFlow(emptyList())
    val currentList: StateFlow<List<Placemark>> = _currentList.asStateFlow()

    init {
        viewModelScope.launch {
            _currentList.value = convertResponseToPlacemarks(Requester.api.getPlacemarks())
        }

    }

    suspend fun convertResponseToPlacemarks(response: APIResponse): List<Placemark> {
        val list: MutableList<Placemark> = mutableListOf()
        response.forEach {item ->
            list.add(Placemark(
                id = item.id,
                description = item.description,
                name = item.name,
                tagList = item.tag_list,
                latitude = item.visual_center.latitude,
                longitude = item.visual_center.longitude
            ))
        }
        return list
    }
}