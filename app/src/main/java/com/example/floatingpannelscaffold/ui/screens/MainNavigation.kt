package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen

sealed class MainNavigation : Screen {
  object IntroScreen : MainNavigation() {
    @Composable
    override fun Content() {
      IntroBody()
    }
  }

  object Map : MainNavigation() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    @Composable
    override fun Content() {
      MapBody()
    }
  }
}