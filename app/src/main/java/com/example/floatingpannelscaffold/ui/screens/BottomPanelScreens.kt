package com.example.floatingpannelscaffold.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

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

  object BottomPanelFilterScreen : BottomPanelScreens() {

    @Composable
    override fun Content() {
      BottomPanelFilterContent()
    }
  }
}

@Composable
fun BottomPanelMainContent(
  onExpandBottomClicked: () -> Unit = {},
  onExpandSideClicked: () -> Unit = {},
  onListModeClicked: () -> Unit = {},
) {
  val navigator = LocalNavigator.currentOrThrow
  Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
      TextButton(onClick = onExpandBottomClicked) {
        Text(text = "Expand Bottom", fontSize = 14.sp, maxLines = 1)
      }
      TextButton(onClick = onExpandSideClicked) {
        Text(text = "Expand Side", fontSize = 14.sp, maxLines = 1)
      }
    }
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
      TextButton(
        onClick = onListModeClicked,
        modifier = Modifier
      ) {
        Text(text = "List Mode", fontSize = 14.sp, maxLines = 1)
      }
      TextButton(
        modifier = Modifier,
        onClick = {
          navigator.push(BottomPanelScreens.BottomPanelFilterScreen)
        }
      ) {
        Text(text = "Second Pannel", fontSize = 14.sp, maxLines = 1)
      }
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(50) {
        Text(
          text = "I am in bottom pannel",
          modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
            .padding(horizontal = 16.dp),
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@Composable
fun BottomPanelFilterContent() {
  val navigator = LocalNavigator.currentOrThrow
  Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(16.dp))
    IconButton(onClick = { navigator.pop() }) {
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Arrow")
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(50) {
        Text(
          text = "I am in second bottom pannel",
          modifier = Modifier
            .fillMaxWidth()
            .height(height = 40.dp)
            .padding(horizontal = 16.dp),
          textAlign = TextAlign.Center
        )
      }
    }
  }
}