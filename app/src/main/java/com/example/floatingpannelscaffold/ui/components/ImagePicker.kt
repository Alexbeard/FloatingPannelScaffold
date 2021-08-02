package com.example.floatingpannelscaffold.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.contentColorFor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.ColorFilter

@Composable
fun ImagePicker(
  vararg icons: ImageVector,
  modifier: Modifier = Modifier,
  imageSize: Dp = 40.dp,
  pickedColor: Color = MaterialTheme.colors.primary,
  selectedImageColor: Color = contentColorFor(pickedColor),
  unSelectedImageColor: Color = contentColorFor(MaterialTheme.colors.background).copy(alpha = 0.5f),
  borderColor: Color = contentColorFor(MaterialTheme.colors.background).copy(alpha = 0.5f),
  selectedIndex: Int = 0,
) {
  var index by rememberSaveable { mutableStateOf(selectedIndex.coerceIn(0, icons.size - 1)) }

  Box(
    modifier = modifier
      .clip(RoundedCornerShape(20.dp))
      .border(1.dp, borderColor, RoundedCornerShape(20.dp))
  ) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
      val offset: Dp by animateDpAsState((this.maxWidth / icons.size) * index)
      Canvas(
        modifier = Modifier
          .absoluteOffset(x = offset)
          .clip(RoundedCornerShape(20.dp))
          .height(imageSize)
          .fillMaxWidth(1f / icons.size)
      ) {
        drawRect(pickedColor)
      }
      Row(modifier = Modifier.fillMaxWidth()) {
        icons.forEachIndexed { iconIndex, imageVector ->
          Box(
            Modifier
              .weight(1f)
              .clip(RoundedCornerShape(20.dp))
              .clickable { index = iconIndex }
          ) {
            val iconColor by animateColorAsState(
              if (iconIndex == index) selectedImageColor else unSelectedImageColor
            )
            Image(
              imageVector = imageVector,
              contentDescription = "icon",
              modifier = Modifier
                .align(Alignment.Center)
                .size(imageSize)
                .padding(8.dp),
              colorFilter = ColorFilter.tint(iconColor)
            )
          }
        }
      }
    }

  }
}

@Preview
@Composable
fun ImagePickerPreview() {
  ImagePicker(
    Icons.Filled.DateRange,
    Icons.Filled.ShoppingCart,
    modifier = Modifier
      .padding(100.dp)
  )
}

@Preview
@Composable
fun ImagePickerPreview2() {
  ImagePicker(
    Icons.Filled.DateRange,
    Icons.Filled.ShoppingCart,
    Icons.Default.Call,
    modifier = Modifier
      .padding(100.dp)
  )
}

@Preview
@Composable
fun ImagePickerPreview3() {
  ImagePicker(
    Icons.Filled.DateRange,
    Icons.Filled.ShoppingCart,
    Icons.Default.Call,
    Icons.Filled.Check,
    modifier = Modifier
      .padding(100.dp)
  )
}