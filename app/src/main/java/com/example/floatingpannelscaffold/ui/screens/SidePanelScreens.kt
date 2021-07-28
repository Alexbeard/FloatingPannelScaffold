package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow


sealed class SidePanelScreens : Screen {

  object SidePanelListScreen : SidePanelScreens() {

    @Composable
    override fun Content() {
      val navigator = LocalNavigator.currentOrThrow
      SidePanelList { navigator.push(SidePanelImageScreen) }
    }
  }

  object SidePanelImageScreen : SidePanelScreens() {

    @Composable
    override fun Content() {
      val navigator = LocalNavigator.currentOrThrow
      SidePanelImage { navigator.pop() }
    }
  }

  object SidePanelEmptyScreen : SidePanelScreens() {

    @Composable
    override fun Content() {
      val navigator = LocalNavigator.currentOrThrow
      SidePanelEmpty { navigator.push(SidePanelListScreen) }
    }
  }
}

@Composable
fun SidePanelList(onClick: () -> Unit) {
  LazyColumn {
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
fun SidePanelImage(onClick: () -> Unit) {
  Image(
    imageVector = Icons.Filled.ShoppingCart,
    contentDescription = "",
    Modifier
      .fillMaxSize()
      .clickable(onClick = onClick)
  )
}

@Composable
fun SidePanelEmpty(onClick: () -> Unit) {
  Box(Modifier.clickable(onClick = onClick).fillMaxSize()) {
    Text(
      text = "Empty Side Panel",
      Modifier
        .fillMaxWidth()
        .align(Alignment.Center),
      textAlign = TextAlign.Center
    )
  }
}

@Preview
@Composable
private fun SidePanelListPreview() {
  Box(
    modifier = Modifier
      .size(100.dp, 300.dp)
      .background(Color.White),
    contentAlignment = Alignment.Center
  ) {
    SidePanelList() {}
  }
}

@Preview
@Composable
private fun SidePanelImagePreview() {
  SidePanelImage {}
}

@Preview
@Composable
private fun SidePanelEmptyPreview() {
  Box(
    modifier = Modifier
      .size(100.dp, 300.dp)
      .background(Color.White),
    contentAlignment = Alignment.Center
  ) {
    SidePanelEmpty {}
  }
}

fun SidePanelScreens.modifier(isInListMode: Boolean): Modifier {
  return when (this) {
    is SidePanelScreens.SidePanelEmptyScreen -> {
      SidePanelScreenConstraintsModifiers.CollapsedModifier(isInListMode = isInListMode)
    }
    is SidePanelScreens.SidePanelListScreen -> {
      SidePanelScreenConstraintsModifiers.CollapsedModifier(isInListMode = isInListMode)
    }
    is SidePanelScreens.SidePanelImageScreen -> {
      SidePanelScreenConstraintsModifiers.ExpandedModifier(isInListMode = isInListMode)
    }
  }
}

object SidePanelScreenConstraintsModifiers {
  fun CollapsedModifier(isInListMode: Boolean) = Modifier
    .width(70.dp)
    .then(
      if (!isInListMode) Modifier.fillMaxHeight(0.5f)
      else Modifier.fillMaxHeight()
    )

  fun ExpandedModifier(isInListMode: Boolean) = if (isInListMode)
    Modifier.fillMaxSize()
  else
    Modifier.size(250.dp, 250.dp)
}