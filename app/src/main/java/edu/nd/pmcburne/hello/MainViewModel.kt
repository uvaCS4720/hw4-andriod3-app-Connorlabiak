package edu.nd.pmcburne.hello

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import edu.nd.pmcburne.hello.APIResponseObjects.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class MainViewModel(application: Application): AndroidViewModel(application) {
    private val dao = PlacemarksDatabase.getDatabase(application.applicationContext).gameDao()
    private val _currentList: MutableStateFlow<List<Placemark>> = MutableStateFlow(emptyList())
    val currentList: StateFlow<List<Placemark>> = _currentList.asStateFlow()

    private val _tags: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val tags: StateFlow<List<String>> = _tags.asStateFlow()
    private val _expanded = MutableStateFlow(false)
    val expanded: StateFlow<Boolean> = _expanded.asStateFlow()
    fun toggleExpand() {
        _expanded.value = !_expanded.value
    }


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _currentList.value = convertResponseToPlacemarks(Requester.api.getPlacemarks())
            _tags.value = getAllTags(_currentList.value)
            dao.insertPlacemarks(_currentList.value)
        }
    }

    fun convertResponseToPlacemarks(response: APIResponse): List<Placemark> {
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

    fun getAllTags(tags: List<Placemark>): List<String> {
        val set: MutableSet<String> = mutableSetOf()
        tags.forEach {item -> item.tagList.forEach {tag -> set.add(tag.replaceFirstChar { it.uppercase() })}}
        return set.toList().sorted()
    }

    fun filterByTag(tag: String) {
        viewModelScope.launch {
            _currentList.value = dao.getPlacemarks().filter { item -> item.tagList.contains(tag) }
        }
    }
}