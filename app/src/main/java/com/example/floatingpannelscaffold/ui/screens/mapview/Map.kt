package com.example.floatingpannelscaffold.ui.screens.mapview

import androidx.compose.foundation.layout.Box
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MapStyleOptions
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

@Composable
fun CityMapView(latitude: String, longitude: String) {
  val mapView = rememberMapViewWithLifecycle()
  MapViewContainer(mapView, latitude, longitude)
}

@Composable
private fun MapViewContainer(
  map: MapView,
  latitude: String,
  longitude: String
) {
  val context = LocalContext.current
  val useDarkMap = !MaterialTheme.colors.isLight
  val coroutineScope = rememberCoroutineScope()
  val cameraPosition = remember(latitude, longitude) {
    LatLng(latitude.toDouble(), longitude.toDouble())
  }

  LaunchedEffect(map) {
    val googleMap = map.awaitMap()
    googleMap.addMarker { position(cameraPosition) }
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition))
  }

  Box {
    AndroidView({ map }) { mapView ->
      coroutineScope.launch {
        val googleMap = mapView.awaitMap()
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        if (useDarkMap) {
          runCatching {
            googleMap.setMapStyle(
              MapStyleOptions.loadRawResourceStyle(
                context,
                com.example.floatingpannelscaffold.R.raw.map_dark_style
              )
            )
          }
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition))
      }
    }
  }
}