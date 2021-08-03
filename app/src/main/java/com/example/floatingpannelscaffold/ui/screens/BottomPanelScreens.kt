package com.example.floatingpannelscaffold.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.floatingpannelscaffold.ui.components.ImagePicker
import com.example.floatingpannelscaffold.ui.components.SelectableChip
import com.example.floatingpannelscaffold.ui.models.DefaultUsers
import com.example.floatingpannelscaffold.ui.models.User
import com.google.accompanist.insets.navigationBarsPadding

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
      itemsIndexed(DefaultUsers) { index , user ->
        UserCell(user) {
          navigator.push(BottomPanelScreens.BottomPanelUserScreen(it))
        }
        if (index == DefaultUsers.lastIndex){
          Spacer(modifier = Modifier.navigationBarsPadding())
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
  val scrollState = rememberScrollState()
  Column(modifier = Modifier.fillMaxWidth()) {
    Spacer(modifier = Modifier.height(16.dp))
    IconButton(onClick = { navigator.pop() }) {
      Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back Arrow")
    }
    Spacer(modifier = Modifier.height(16.dp))
    Column(
      modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
    ) {
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
      Spacer(modifier = Modifier.height(16.dp))
      ChipGroup()
      Spacer(modifier = Modifier.height(16.dp))
      ImagePicker(
        Icons.Filled.DateRange,
        Icons.Filled.ShoppingCart,
        Icons.Filled.Call,
        modifier = Modifier
          .widthIn(150.dp, 200.dp)
          .align(Alignment.CenterHorizontally)
      )
    }
  }
}

@Composable
fun ChipGroup() {
  var selectedChipIndex by rememberSaveable { mutableStateOf(-1) }
  val chips = listOf("First", "Second", "Third", "Fourth")
  LazyRow(modifier = Modifier.fillMaxWidth()) {
    itemsIndexed(chips) { index, chip ->
      Spacer(modifier = Modifier.width(16.dp))
      SelectableChip(
        label = chip,
        contentDescription = chip,
        selected = selectedChipIndex == index,
        onClick = { selectedChipIndex = index }
      )
    }
  }
}

