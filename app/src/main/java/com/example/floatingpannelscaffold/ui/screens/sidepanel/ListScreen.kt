package com.example.floatingpannelscaffold.ui.screens.sidepanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.floatingpannelscaffold.ui.models.DefaultStatuses
import com.example.floatingpannelscaffold.ui.models.Status
import com.google.accompanist.insets.LocalWindowInsets

@Composable
fun SidePanelList(items: List<Status>, onClick: (ImageVector) -> Unit) {
  val navigationBarTop = with(LocalDensity.current) {
    LocalWindowInsets.current.navigationBars.bottom.toDp()
  }

  LazyColumn {
    itemsIndexed(items) { index, item ->
      Spacer(modifier = Modifier.height(16.dp))
      StatusAction(
        icon = item.icon,
        backgroundColor = item.backgroundColor,
        status = item.statusText,
        onClick = onClick
      )
      if (index == items.lastIndex) {
        Spacer(modifier = Modifier.height(navigationBarTop))
      }
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
