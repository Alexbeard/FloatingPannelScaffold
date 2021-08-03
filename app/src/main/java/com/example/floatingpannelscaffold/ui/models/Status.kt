package com.example.floatingpannelscaffold.ui.models

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Status(
  val icon: ImageVector,
  val backgroundColor: Color,
  val statusText: String
)

val DefaultStatuses = listOf(
  Status(
    icon = Icons.Filled.ShoppingCart,
    backgroundColor = Color(0xFF8bc34a),
    statusText = "Sell"
  ),
  Status(
    icon = Icons.Filled.Call,
    backgroundColor = Color(0xFF1976d2),
    statusText = "Follow"
  ),
  Status(
    icon = Icons.Filled.Check,
    backgroundColor = Color(0xFFe91e63),
    statusText = "Check"
  ),
  Status(
    icon = Icons.Filled.Face,
    backgroundColor = Color(0xFFab47bc),
    statusText = "Face"
  ),
  Status(
    icon = Icons.Filled.Email,
    backgroundColor = Color(0xFFffb300),
    statusText = "Email"
  ),
  Status(
    icon = Icons.Filled.Delete,
    backgroundColor = Color(0xFFe53935),
    statusText = "Delete"
  ),
  Status(
    icon = Icons.Filled.Build,
    backgroundColor = Color(0xFF9575cd),
    statusText = "Build"
  ),
  Status(
    icon = Icons.Filled.AccountBox,
    backgroundColor = Color(0xFF4dd0e1),
    statusText = "AccountBox"
  ),
  Status(
    icon = Icons.Filled.Create,
    backgroundColor = Color(0xFFff5722),
    statusText = "Create"
  ),
  Status(
    icon = Icons.Filled.DateRange,
    backgroundColor = Color(0xFF455a64),
    statusText = "DateRange"
  ),
  Status(
    icon = Icons.Filled.Favorite,
    backgroundColor = Color(0xFF004d40),
    statusText = "Favorite"
  ),

  Status(
    icon = Icons.Filled.Home,
    backgroundColor = Color(0xFF4660e1),
    statusText = "Home"
  ),
  Status(
    icon = Icons.Filled.Done,
    backgroundColor = Color(0xFF655712),
    statusText = "Done"
  ),
  Status(
    icon = Icons.Filled.List,
    backgroundColor = Color(0xFF455f10),
    statusText = "List"
  ),
  Status(
    icon = Icons.Filled.ThumbUp,
    backgroundColor = Color(0xFF097d40),
    statusText = "ThumbUp"
  ),
)