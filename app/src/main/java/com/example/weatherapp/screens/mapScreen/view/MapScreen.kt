package com.example.weatherapp.screens.mapScreen.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
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
import com.example.weatherapp.data.model.FavCity
import com.example.weatherapp.screens.mapScreen.viewmodel.MapViewModel
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
    onClick: (Position) -> Unit,
    mapViewModel: MapViewModel,
    mode: Any
){
    val position by mapViewModel.selectedPosition.collectAsState()
    val address by mapViewModel.address.collectAsState()
    Box(modifier = Modifier.fillMaxSize()) {
        MapItem(onClick = {
            mapViewModel.onMapClick(it)
        })
        PlaceName( Modifier
            .align(Alignment.BottomCenter)
            .padding(16.dp),position, onClick = {

           /*     when (mode) {
                    "fav" -> mapViewModel.saveCityData(
                        FavCity(
                            address,
                            long = it.longitude,
                            lat = it.latitude,
                            temp = 0.0,
                            tempDescription = "",
                            countryCode = ""
                        )
                    )
                    "location" -> mapViewModel.saveForecastData(       FavCity(
                        address,
                        long = it.longitude,
                        lat = it.latitude,
                        temp = 0.0,
                        tempDescription = "",
                        countryCode = ""
                    ))
                }*/
                onClick(it)

        }, place = address)
    }
}
@Composable
fun PlaceName(modifier: Modifier= Modifier,mypostion: Position,onClick: (Position) -> Unit,place: String){



    Box (modifier.fillMaxWidth().clip(shape = RoundedCornerShape(15.dp)).background(color = colorResource(R.color.black))) {
        Column(modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {

            Text("Your Country choice is ")

            Text(place)
            Button(onClick = {onClick(mypostion)}) {
                Text("Add")
            }
        }

    }

}
@Composable
fun MapItem(onClick:(Position)->Unit) {
    val myLocation = Position(31.2357, 30.0444)
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
