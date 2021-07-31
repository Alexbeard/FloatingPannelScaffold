package com.example.floatingpannelscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import com.example.floatingpannelscaffold.ui.floatingpanelscaffold.*
import com.example.floatingpannelscaffold.ui.screens.BottomPanelScreens
import com.example.floatingpannelscaffold.ui.screens.CityMapView
import com.example.floatingpannelscaffold.ui.screens.SidePanelScreens
import com.example.floatingpannelscaffold.ui.screens.modifier
import com.example.floatingpannelscaffold.ui.theme.FloatingPannelScaffoldTheme
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
  val coroutineScope = rememberCoroutineScope()
  val scaffoldState = rememberFloatingPanelScaffoldState()
  val isInListMode = rememberSaveable { mutableStateOf(false) }
  var sidePanelState by rememberSaveable { mutableStateOf(SidePanelValue.Closed) }
  var sidePanelScreen: SidePanelScreens by rememberSaveable { mutableStateOf(SidePanelScreens.SidePanelEmptyScreen) }

  if (isInListMode.value) {
    sidePanelState = SidePanelValue.Open
  } else if (sidePanelScreen is SidePanelScreens.SidePanelEmptyScreen) {
    sidePanelState = SidePanelValue.Closed
  }

  FloatingPanelScaffold(
    scaffoldState = scaffoldState,
    sidePanelState = sidePanelState,
    isInListMode = isInListMode,
    modifier = Modifier.fillMaxSize(),
    bottomPanelContent = {
      Navigator(BottomPanelScreens.BottomPanelMainScreen(
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
          sidePanelState = !sidePanelState
        },
        onListModeClicked = {
          isInListMode.value = !isInListMode.value
        }
      )) { bottomPanelNavigator ->
        Crossfade(targetState = bottomPanelNavigator.last) { screen ->
          screen.Content()
        }
      }
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
      Navigator(sidePanelScreen) { sidePanelNavigator ->
        sidePanelScreen = sidePanelNavigator.last as SidePanelScreens
        Box(
          modifier = sidePanelScreen
            .modifier(isInListMode.value)
            .animateContentSize()
        ) {
          sidePanelScreen.Content()
        }
      }
    },
    sidePanelModifier = if (isInListMode.value) Modifier.padding(top = 16.dp) else Modifier,
  )
}



