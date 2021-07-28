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


enum class SidePanelContent {
  List, Image, Empty
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

@Preview
@Composable
private fun SidePanelListPreview() {
  Box(
    modifier = Modifier
      .size(100.dp, 300.dp)
      .background(Color.White),
    contentAlignment = Alignment.Center
  ) {
    SidePanelList(SidePanelScreenConstraintsModifiers.CollapsedModifier(true)) {}
  }
}

@Preview
@Composable
private fun SidePanelImagePreview() {
  SidePanelImage(SidePanelScreenConstraintsModifiers.ExpandedModifier(false)) {}
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
    SidePanelEmpty(SidePanelScreenConstraintsModifiers.CollapsedModifier(true)) {}
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