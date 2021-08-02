package com.example.floatingpannelscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.floatingpannelscaffold.ui.floatingpanelscaffold.*
import com.example.floatingpannelscaffold.ui.screens.BottomPanelScreens
import com.example.floatingpannelscaffold.ui.screens.CityMapView
import com.example.floatingpannelscaffold.ui.screens.SidePanelScreens
import com.example.floatingpannelscaffold.ui.screens.modifier
import com.example.floatingpannelscaffold.ui.theme.FloatingPannelScaffoldTheme
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
  @ExperimentalAnimationApi
  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      FloatingPannelScaffoldTheme {
        ProvideWindowInsets {
          MainBody()
        }
      }
    }
  }


}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainBody() {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = MaterialTheme.colors.isLight
  SideEffect {
    systemUiController.setSystemBarsColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons
    )
  }
  FloatingPanelScaffoldBody()
}


@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun FloatingPanelScaffoldBody() {
  val insets = LocalWindowInsets.current
  val statusBarsTop = with(LocalDensity.current) { insets.statusBars.top.toDp() }

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
        SlideTransition(navigator = bottomPanelNavigator) {
          it.Content()
        }
      }
    },
    bottomPanelModifier = Modifier
      .mediaQuery(
        comparator = Dimensions.Width lessThan 700.dp,
        modifier = Modifier
          .fillMaxWidth()
          .statusBarsPadding()
      )
      .mediaQuery(
        comparator = Dimensions.Width greaterThan 700.dp,
        modifier = Modifier
          .fillMaxWidth(0.4f)
          .statusBarsPadding()
          .padding(bottom = 8.dp, start = 8.dp)
      ) then if (isInListMode.value) Modifier.padding(end = 2.dp) else Modifier,
    bottomPanelShape = RoundedCornerShape(20.dp),
    bottomPanelPeekHeight = 80.dp + statusBarsTop,
    content = {
      CityMapView(latitude = "43.000000", longitude = "-75.000000")
    },
    sidePanelContent = {
      Navigator(sidePanelScreen) { sidePanelNavigator ->
        sidePanelScreen = sidePanelNavigator.lastItem as SidePanelScreens
        SlideTransition(navigator = sidePanelNavigator) {
          Box(
            modifier = sidePanelScreen
              .modifier(isInListMode.value)
              .animateContentSize()
          ) {
            it.Content()
          }
        }
      }
    },
    sidePanelModifier = if (isInListMode.value) Modifier.statusBarsPadding() else Modifier,
  )
}



