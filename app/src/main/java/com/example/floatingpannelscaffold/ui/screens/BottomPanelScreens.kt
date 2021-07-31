package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.floatingpannelscaffold.ui.models.DefaultUsers
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
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(modifier = Modifier.fillMaxSize()) {
      items(DefaultUsers) { user ->
        UserCell(user) {
          navigator.push(BottomPanelScreens.BottomPanelUserScreen(it))
        }
      }
    }
  }
}

@Composable
fun UserCell(user: User, onClick: (User) -> Unit) {
  Card(
    shape = RoundedCornerShape(10.dp), modifier = Modifier
      .fillMaxWidth()
      .padding(8.dp)
      .clickable { onClick(user) },
    backgroundColor = MaterialTheme.colors.surface,
    elevation = 10.dp
  ) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
      Image(
        painter = painterResource(id = user.image),
        contentDescription = "User Image",
        modifier = Modifier
          .padding(16.dp)
          .size(40.dp)
          .clip(CircleShape),
        contentScale = ContentScale.Crop
      )
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = user.fullName,
          fontSize = 14.sp,
          maxLines = 1,
          color = MaterialTheme.colors.onSurface,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = user.address,
          fontSize = 12.sp,
          maxLines = 1,
          color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
          overflow = TextOverflow.Ellipsis
        )
      }
    }
  }
}


@Composable
fun BottomPanelUserContent(user: User) {
  val navigator = LocalNavigator.currentOrThrow
  Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(16.dp))
    IconButton(onClick = { navigator.pop() }) {
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Arrow")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Column(modifier = Modifier.fillMaxSize()) {
      Image(
        painter = painterResource(id = user.image),
        contentDescription = "User Image",
        modifier = Modifier
          .padding(16.dp)
          .size(80.dp)
          .clip(CircleShape)
          .align(Alignment.CenterHorizontally),
        contentScale = ContentScale.Crop
      )
      Spacer(modifier = Modifier.height(8.dp))
      Text(
        text = user.fullName,
        fontSize = 22.sp,
        maxLines = 1,
        color = MaterialTheme.colors.onSurface,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
      Text(
        text = user.address,
        fontSize = 18.sp,
        maxLines = 1,
        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.align(Alignment.CenterHorizontally)
      )
    }
  }
}

