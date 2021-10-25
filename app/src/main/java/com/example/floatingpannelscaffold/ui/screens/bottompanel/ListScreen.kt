package com.example.floatingpannelscaffold.ui.screens.bottompanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
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
import com.example.floatingpannelscaffold.ui.models.DefaultUsers
import com.example.floatingpannelscaffold.ui.models.User
import com.google.accompanist.insets.navigationBarsPadding

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
      itemsIndexed(DefaultUsers) { index, user ->
        UserCell(user) {
          navigator.push(BottomPanelScreens.BottomPanelUserScreen(it))
        }
        if (index == DefaultUsers.lastIndex) {
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


@Preview
@Composable
fun BottomPanelMainContentPreview() {
  BottomPanelMainContent()
}

@Preview
@Composable
fun UserCellPreview() {
  UserCell(user = DefaultUsers.first(), onClick = {})
}
