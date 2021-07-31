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
)