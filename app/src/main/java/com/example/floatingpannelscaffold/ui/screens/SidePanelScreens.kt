package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.floatingpannelscaffold.ui.models.DefaultStatuses
import com.example.floatingpannelscaffold.ui.models.Status


sealed class SidePanelScreens : Screen {

  object SidePanelListScreen : SidePanelScreens() {

    @Composable
    override fun Content() {
      val navigator = LocalNavigator.currentOrThrow
      SidePanelList(DefaultStatuses) {
        navigator.push(SidePanelImageScreen(it))
      }
    }
  }

  data class SidePanelImageScreen(val icon: ImageVector) : SidePanelScreens() {

    @Composable
    override fun Content() {
      val navigator = LocalNavigator.currentOrThrow
      SidePanelImage(icon) { navigator.pop() }
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
fun SidePanelList(items: List<Status>, onClick: (ImageVector) -> Unit) {
  LazyColumn {
    items(items) {
      Spacer(modifier = Modifier.height(16.dp))
      StatusAction(
        icon = it.icon,
        backgroundColor = it.backgroundColor,
        status = it.statusText,
        onClick = onClick
      )
    }
  }
}

@Composable
fun StatusAction(
  icon: ImageVector,
  backgroundColor: Color,
  status: String,
  onClick: (ImageVector) -> Unit
) {
  Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
    Box(
      modifier = Modifier
        .size(35.dp)
        .clip(CircleShape)
        .background(backgroundColor)
        .clickable {
          onClick(icon)
        },
      contentAlignment = Alignment.Center
    ) {
      Image(
        imageVector = icon,
        contentDescription = "status",
        colorFilter = ColorFilter.tint(Color.White),
        modifier = Modifier
          .fillMaxSize()
          .padding(8.dp)
      )
    }
    Text(text = status, fontSize = 14.sp, textAlign = TextAlign.Center)
  }
}

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
private fun SidePanelListPreview() {
  Box(
    modifier = Modifier
      .size(100.dp, 300.dp)
      .background(Color.White)
  ) {
    SidePanelList(DefaultStatuses) {}
  }
}

@Preview
@Composable
fun StatusPreview() {
  StatusAction(
    icon = Icons.Filled.Add,
    backgroundColor = Color.Black,
    status = "Status",
    onClick = { }
  )
}

@Preview
@Composable
private fun SidePanelImagePreview() {
  SidePanelImage(Icons.Filled.ShoppingCart) {}
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