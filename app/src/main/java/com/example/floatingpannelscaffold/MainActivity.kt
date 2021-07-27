package com.example.floatingpannelscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.floatingpannelscaffold.ui.*
import com.example.floatingpannelscaffold.ui.floatingpanelscaffold.*
import com.example.floatingpannelscaffold.ui.theme.FloatingPannelScaffoldTheme
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.MapView
import com.google.android.libraries.maps.model.LatLng
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  @ExperimentalAnimationApi
  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContent {
      FloatingPannelScaffoldTheme {
        FloatingPanelScaffoldBody()
      }
    }
  }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
@Preview
fun FloatingPanelScaffoldBody() {
  val scaffoldState = rememberFloatingPanelScaffoldState()
  val coroutineScope = rememberCoroutineScope()
  var sidePanelContent by rememberSaveable { mutableStateOf(SidePanelContent.Empty) }
  val isInListMode = rememberSaveable { mutableStateOf(false) }
  var sidePanelState by rememberSaveable { mutableStateOf(SidePanelValue.Closed) }
  if (isInListMode.value) {
    sidePanelState = SidePanelValue.Open
  } else if (sidePanelContent == SidePanelContent.Empty) {
    sidePanelState = SidePanelValue.Closed
  }

  FloatingPanelScaffold(
    scaffoldState = scaffoldState,
    sidePanelState = sidePanelState,
    isInListMode = isInListMode,
    modifier = Modifier.fillMaxSize(),
    bottomPanelContent = {
      BottomPannelContent(
        onExpandBottomClicked = {
          coroutineScope.launch {
            if (scaffoldState.bottomPanelState.isCollapsed) {
              scaffoldState.bottomPanelState.expand()
            } else {
              scaffoldState.bottomPanelState.hide()
            }
          }
        },
        onExpandSideClicked = {
          sidePanelState = if (sidePanelState == SidePanelValue.Closed) {
            SidePanelValue.Open
          } else {
            SidePanelValue.Closed
          }
        },
        onListModeClicked = {
          isInListMode.value = !isInListMode.value
        }
      )
    },
    bottomPanelModifier = Modifier
      .mediaQuery(
        comparator = Dimensions.Width lessThan 700.dp,
        modifier = Modifier
          .fillMaxWidth()
          .padding(top = 16.dp)
      )
      .mediaQuery(
        comparator = Dimensions.Width greaterThan 700.dp,
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .padding(16.dp)
      ) then if (isInListMode.value) Modifier.padding(end = 2.dp) else Modifier,
    bottomPanelShape = RoundedCornerShape(20.dp),
    bottomPanelPeekHeight = 80.dp,
    content = {
      CityMapView(latitude = "43.000000", longitude = "-75.000000")
    },
    sidePanelContent = {
      Box(modifier = Modifier.animateContentSize()) {
        when (sidePanelContent) {
          SidePanelContent.Empty -> SidePanelEmpty(
            modifier = Modifier
              .width(70.dp)
              .then(
                if (!isInListMode.value) Modifier.fillMaxHeight(0.5f)
                else Modifier.fillMaxHeight()
              )
          ) { sidePanelContent = SidePanelContent.List }
          SidePanelContent.List -> SidePanelList(
            modifier = Modifier
              .width(70.dp)
              .then(
                if (!isInListMode.value) Modifier.fillMaxHeight(0.5f)
                else Modifier.fillMaxHeight()
              )
          ) {
            sidePanelContent = SidePanelContent.Image
          }
          SidePanelContent.Image -> SidePanelImage(
            modifier = if (isInListMode.value) Modifier.fillMaxSize()
            else Modifier.size(250.dp, 250.dp)
          ) {
            sidePanelContent = SidePanelContent.List
          }
        }
      }
    },
    sidePanelModifier = if (isInListMode.value) Modifier.padding(top = 16.dp) else Modifier,
  )
}

@Composable
fun BottomPannelContent(
  onExpandBottomClicked: () -> Unit = {},
  onExpandSideClicked: () -> Unit = {},
  onListModeClicked: () -> Unit = {}
) {
  Column {
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
      TextButton(onClick = onExpandBottomClicked) {
        Text(text = "Expand Bottom", fontSize = 14.sp, maxLines = 1)
      }
      TextButton(onClick = onExpandSideClicked) {
        Text(text = "Expand Side", fontSize = 14.sp, maxLines = 1)
      }
    }
    TextButton(
      onClick = onListModeClicked,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.CenterHorizontally)
    ) {
      Text(text = "List Mode", fontSize = 14.sp, maxLines = 1)
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(50) {
        Text(
          text = "I am in bottom pannel",
          modifier = Modifier
            .height(height = 40.dp)
            .padding(horizontal = 16.dp)
        )
      }
    }
  }
}

@Composable
fun SidePanelList(modifier: Modifier = Modifier, onClick: () -> Unit) {
  LazyColumn(modifier) {
    items(30) { index ->
      Box(modifier = Modifier.fillMaxWidth()) {
        Image(
          imageVector = Icons.Filled.AddCircle,
          contentDescription = "add",
          modifier = Modifier
            .size(40.dp)
            .align(Alignment.Center)
            .clickable(onClick = onClick)
        )
      }
    }
  }
}

@Composable
fun SidePanelImage(modifier: Modifier = Modifier, onClick: () -> Unit) {
  Image(
    imageVector = Icons.Filled.ShoppingCart,
    contentDescription = "",
    modifier
      .clickable(onClick = onClick)
  )
}

@Composable
fun SidePanelEmpty(modifier: Modifier = Modifier, onClick: () -> Unit) {
  Box(modifier.clickable(onClick = onClick)) {
    Text(
      text = "Empty Side Panel",
      Modifier
        .fillMaxWidth()
        .align(Alignment.Center),
      textAlign = TextAlign.Center
    )
  }
}

@Composable
private fun CityMapView(latitude: String, longitude: String) {
  val mapView = rememberMapViewWithLifecycle()
  MapViewContainer(mapView, latitude, longitude)
}

@Composable
private fun MapViewContainer(
  map: MapView,
  latitude: String,
  longitude: String
) {
  val coroutineScope = rememberCoroutineScope()

  val cameraPosition = remember(latitude, longitude) {
    LatLng(latitude.toDouble(), longitude.toDouble())
  }

  LaunchedEffect(map) {
    val googleMap = map.awaitMap()
    googleMap.addMarker { position(cameraPosition) }
    googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition))
  }

  var zoom by rememberSaveable(map) { mutableStateOf(InitialZoom) }
  Box {
    AndroidView({ map }) { mapView ->
      coroutineScope.launch {
        val googleMap = mapView.awaitMap()
        googleMap.uiSettings.isZoomGesturesEnabled = true
        googleMap.uiSettings.isTiltGesturesEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        // Move camera to the same place to trigger the zoom update
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cameraPosition))
      }
    }
  }
}

@Composable
private fun MapControlButton(imageVector: ImageVector, onClick: () -> Unit) {
  FloatingActionButton(
    modifier = Modifier
      .size(56.dp)
      .padding(8.dp),
    backgroundColor = MaterialTheme.colors.onPrimary,
    contentColor = MaterialTheme.colors.primary,
    onClick = onClick
  ) {
    Icon(imageVector = imageVector, contentDescription = "")
  }
}


enum class SidePanelContent {
  List, Image, Empty
}
