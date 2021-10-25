package com.example.floatingpannelscaffold.ui.screens.sidepanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SidePanelImage(icon: ImageVector, onClick: () -> Unit) {
  Image(
    imageVector = icon,
    contentDescription = "",
    colorFilter = ColorFilter.tint(color = MaterialTheme.colors.onSurface),
    modifier = Modifier
      .fillMaxSize()
      .clickable(onClick = onClick)
  )
}

@Preview
@Composable
private fun SidePanelImagePreview() {
  SidePanelImage(Icons.Filled.ShoppingCart) {}
}