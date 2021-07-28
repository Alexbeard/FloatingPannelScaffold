package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun BottomPanelContent(
  onExpandBottomClicked: () -> Unit = {},
  onExpandSideClicked: () -> Unit = {},
  onListModeClicked: () -> Unit = {}
) {
  Column {
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
      TextButton(onClick = onExpandBottomClicked) {
        Text(text = "Expand Bottom", fontSize = 14.sp, maxLines = 1)
      }
      TextButton(onClick = onExpandSideClicked) {
        Text(text = "Expand Side", fontSize = 14.sp, maxLines = 1)
      }
    }
    TextButton(
      onClick = onListModeClicked,
      modifier = Modifier
        .fillMaxWidth()
        .align(Alignment.CenterHorizontally)
    ) {
      Text(text = "List Mode", fontSize = 14.sp, maxLines = 1)
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(50) {
        Text(
          text = "I am in bottom pannel",
          modifier = Modifier
            .height(height = 40.dp)
            .padding(horizontal = 16.dp)
        )
      }
    }
  }
}