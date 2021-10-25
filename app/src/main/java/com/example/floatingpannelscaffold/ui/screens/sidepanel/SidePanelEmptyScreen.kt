package com.example.floatingpannelscaffold.ui.screens.sidepanel

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun SidePanelEmpty(onClick: () -> Unit) {
  Box(
    Modifier
      .clickable(onClick = onClick)
      .fillMaxSize()
  ) {
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