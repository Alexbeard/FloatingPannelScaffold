package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

@Composable
fun IntroBody() {
  val navigator = LocalNavigator.currentOrThrow
  Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
      Image(
        imageVector = Icons.Filled.ThumbUp,
        contentDescription = "Hello Image",
        modifier = Modifier.size(300.dp),
        colorFilter = ColorFilter.tint(contentColorFor(MaterialTheme.colors.background))
      )
      Spacer(modifier = Modifier.height(16.dp))
      Text(
        text = "Hello! Try my new functionality of map",
        fontSize = 22.sp,
        style = MaterialTheme.typography.h1,
        color = contentColorFor(MaterialTheme.colors.background)
      )
      Spacer(modifier = Modifier.height(16.dp))
      Button(onClick = { navigator.push(MainNavigation.Map) }) {
        Text(text = "Go to Map screen")
      }
    }
  }
}