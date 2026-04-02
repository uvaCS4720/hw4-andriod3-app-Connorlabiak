package edu.nd.pmcburne.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import edu.nd.pmcburne.hello.ui.theme.MyApplicationTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollSource.Companion.SideEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModelProvider
import com.google.maps.android.compose.MarkerState

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Menu(
                            tags = viewModel.tags.collectAsState().value,
                            modifier = Modifier.fillMaxWidth(),
                            expanded = viewModel.expanded.collectAsState().value,
                            onToggleExpand = {viewModel.toggleExpand()},
                            onChooseTag = {viewModel.filterByTag(it.lowercase())}
                        )
                    }
                ) { innerPadding ->
                    MainScreen(
                        list = viewModel.currentList.collectAsState().value,
                        modifier = Modifier.padding(innerPadding).fillMaxSize())
                }
            }
        }
    }


}

@Composable
fun MainScreen(list: List<Placemark>, modifier: Modifier = Modifier) {
    val uva = LatLng(38.03180, -78.51051)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(uva, 15f)
    }
    val selectedMarkerName = rememberSaveable { mutableStateOf<String?>(null) }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        list.forEach { item ->
            val markerState = rememberUpdatedMarkerState(
                position = LatLng(item.latitude, item.longitude)
            )
            if (selectedMarkerName.value == item.name) {
                SideEffect {
                    markerState.showInfoWindow()
                }
            }

            Marker(
                state = markerState,
                title = item.name,
                snippet = item.description,
                onClick = {
                    selectedMarkerName.value = item.name
                    false
                }
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Menu(
    modifier: Modifier,
    tags: List<String>,
    expanded: Boolean,
    onToggleExpand: () -> (Unit),
    onChooseTag: (String) -> (Unit)
    ) {
    TopAppBar(
        //TOPAPPBAR STYLING DONE WITH AI. REST DONE BY ME.
        title =  {
            Text(buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color(0xFFE57200))) { // UVA Orange
                    append("UVA")
                }
                withStyle(style = SpanStyle(color = Color(0xFF232D4B))) { // UVA Blue
                    append("Maps")
                }
            })
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFFF8F9FA)
        ),
        modifier = modifier,
        actions = {
            Box {
                IconButton(onClick = {onToggleExpand()}) {
                    Icon(
                        painter = painterResource(R.drawable.menu_24px),
                        contentDescription = "Show menu",
                        tint = Color(0xFF232D4B)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {onToggleExpand()},
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .heightIn(max = 400.dp)
                        .background(Color.White)
                ) {
                    tags.forEach {item ->
                        DropdownMenuItem(
                            text = {Text(item)},
                            onClick = {onChooseTag(item)},
                            colors = MenuDefaults.itemColors(
                                textColor = Color(0xFF232D4B) // Blue text for items
                            )
                        )
                    }
                }
            }
        }
    )
}