package com.example.weatherapp.screens.map.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.screens.map.view.components.PlaceName
import com.example.weatherapp.screens.map.viewmodel.MapViewModel
import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.rememberCameraState
import org.maplibre.compose.expressions.dsl.const
import org.maplibre.compose.expressions.dsl.image
import org.maplibre.compose.expressions.value.SymbolAnchor
import org.maplibre.compose.layers.SymbolLayer
import org.maplibre.compose.map.GestureOptions
import org.maplibre.compose.map.MapOptions
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.OrnamentOptions
import org.maplibre.compose.sources.GeoJsonData
import org.maplibre.compose.sources.rememberGeoJsonSource
import org.maplibre.compose.style.BaseStyle
import org.maplibre.compose.style.rememberStyleState
import org.maplibre.compose.util.ClickResult
import org.maplibre.spatialk.geojson.Position
@Composable
fun FullUi(
    modifier: Modifier = Modifier,
    onClick: (Position, String) -> Unit,
    mapViewModel: MapViewModel,
    mode: String
) {
    val mapState by mapViewModel.mapState.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {

        if (mapState !is MapViewModel.MapState.Loading) {
            MapItem(
                onClick = { mapViewModel.onMapClick(it) },
                initialPosition = when (mapState) {
                    is MapViewModel.MapState.LocationSelected ->
                        (mapState as MapViewModel.MapState.LocationSelected).position
                    else -> Position(31.2357, 30.0444)
                }
            )
        }

        when (mapState) {
            is MapViewModel.MapState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colorResource(R.color.darkBlue)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = colorResource(R.color.blue))
                }
            }

            is MapViewModel.MapState.Idle -> {  }

            is MapViewModel.MapState.LocationSelected -> {
                val state = mapState as MapViewModel.MapState.LocationSelected
                PlaceName(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    mypostion = state.position,
                    place = state.address,
                    onClick = { onClick(state.position, state.address) }
                )
            }

            is MapViewModel.MapState.Error -> {
                Text(
                    text = "Unknown Location",
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun MapItem(onClick: (Position) -> Unit, initialPosition: Position) {
    val myLocation =initialPosition
    var markerPosition by remember { mutableStateOf(myLocation) }
    val pinIcon = painterResource(R.drawable.baseline_location_pin_24)

    val cameraState = rememberCameraState(
        CameraPosition(target = myLocation, zoom = 12.0)
    )

    MaplibreMap(
        modifier = Modifier.fillMaxSize(),
        baseStyle = BaseStyle.Uri("https://tiles.openfreemap.org/styles/liberty"),
        cameraState = cameraState,
        styleState = rememberStyleState(),
        onMapClick = { position, _ ->
            markerPosition = position
            onClick(position)
            ClickResult.Consume
        },
        options = MapOptions(
            ornamentOptions = OrnamentOptions.AllDisabled,
            gestureOptions = GestureOptions(
                isTiltEnabled = true,
                isZoomEnabled = true,
                isRotateEnabled = true,
                isScrollEnabled = true,
            ),
        ),
    ) {
        val selectedSource = rememberGeoJsonSource(
            data = GeoJsonData.JsonString(
                """
                {
                  "type": "Feature",
                  "geometry": { "type": "Point", "coordinates": [${markerPosition.longitude}, ${markerPosition.latitude}] },
                  "properties": {}
                }
                """.trimIndent()
            )
        )

        SymbolLayer(
            id = "selected-pin",
            source = selectedSource,
            iconImage = image(pinIcon, drawAsSdf = true),
            iconColor = const(Color.Red),
            iconSize = const(1.5f),
            iconAnchor = const(SymbolAnchor.Bottom),
            iconAllowOverlap = const(true),
        )
    }
}
