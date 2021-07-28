package com.example.floatingpannelscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floatingpannelscaffold.ui.floatingpanelscaffold.*
import com.example.floatingpannelscaffold.ui.screens.*
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
      BottomPanelContent(
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
            modifier = SidePanelScreenConstraintsModifiers.CollapsedModifier(isInListMode.value)
          ) { sidePanelContent = SidePanelContent.List }
          SidePanelContent.List -> SidePanelList(
            modifier = SidePanelScreenConstraintsModifiers.CollapsedModifier(isInListMode.value)
          ) {
            sidePanelContent = SidePanelContent.Image
          }
          SidePanelContent.Image -> SidePanelImage(
            modifier = SidePanelScreenConstraintsModifiers.ExpandedModifier(isInListMode.value)
          ) {
            sidePanelContent = SidePanelContent.List
          }
        }
      }
    },
    sidePanelModifier = if (isInListMode.value) Modifier.padding(top = 16.dp) else Modifier,
  )
}

