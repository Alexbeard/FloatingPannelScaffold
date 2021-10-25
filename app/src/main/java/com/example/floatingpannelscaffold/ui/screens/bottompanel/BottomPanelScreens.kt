package com.example.floatingpannelscaffold.ui.screens.bottompanel

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import com.example.floatingpannelscaffold.ui.models.User

sealed class BottomPanelScreens : Screen {
  class BottomPanelMainScreen(
    val onExpandBottomClicked: () -> Unit = {},
    val onExpandSideClicked: () -> Unit = {},
    val onListModeClicked: () -> Unit = {},
  ) : BottomPanelScreens() {

    @Composable
    override fun Content() {
      BottomPanelMainContent(
        onExpandBottomClicked,
        onExpandSideClicked,
        onListModeClicked,
      )
    }
  }

  data class BottomPanelUserScreen(val user: User) : BottomPanelScreens() {
    @Composable
    override fun Content() {
      BottomPanelUserContent(user)
    }
  }
}



