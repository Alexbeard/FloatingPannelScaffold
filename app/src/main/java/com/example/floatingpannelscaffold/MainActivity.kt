package com.example.floatingpannelscaffold

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.example.floatingpannelscaffold.ui.screens.MainNavigation
import com.example.floatingpannelscaffold.ui.theme.FloatingPannelScaffoldTheme
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
  @ExperimentalAnimationApi
  @ExperimentalMaterialApi
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    WindowCompat.setDecorFitsSystemWindows(window, false)
    setContent {
      FloatingPannelScaffoldTheme {
        ProvideWindowInsets {
          TransparentStatusBarColor()
          Navigator(MainNavigation.IntroScreen) {
            SlideTransition(navigator = it) {
              it.Content()
            }
          }
        }
      }
    }
  }
}

@Composable
fun TransparentStatusBarColor() {
  val systemUiController = rememberSystemUiController()
  val useDarkIcons = MaterialTheme.colors.isLight
  SideEffect {
    systemUiController.setSystemBarsColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons
    )
    systemUiController.setNavigationBarColor(
      color = Color.Transparent,
      darkIcons = useDarkIcons,
    )
  }
}





