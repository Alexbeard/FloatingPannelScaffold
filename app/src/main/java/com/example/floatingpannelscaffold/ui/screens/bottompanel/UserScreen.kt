package com.example.floatingpannelscaffold.ui.screens.bottompanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.example.floatingpannelscaffold.ui.components.ImagePicker
import com.example.floatingpannelscaffold.ui.components.SelectableChip
import com.example.floatingpannelscaffold.ui.models.DefaultUsers
import com.example.floatingpannelscaffold.ui.models.User

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

@Preview
@Composable
fun BottomPanelUserContentPreview() {
  BottomPanelUserContent(DefaultUsers.first())
}

@Preview
@Composable
fun ChipGroupPreview() {
  ChipGroup()
}
