package com.example.floatingpannelscaffold.ui.components


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.floatingpannelscaffold.ui.theme.FloatingPannelScaffoldTheme

@Composable
fun Chip(
  label: String,
  contentDescription: String,
  color: Color = MaterialTheme.colors.surface,
  contentColor: Color = contentColorFor(color),
  startIcon: () -> ImageVector? = { null },
  isStartIconEnabled: Boolean = false,
  startIconTint: Color = contentColor,
  onStartIconClicked: () -> Unit = { },
  endIcon: () -> ImageVector? = { null },
  isEndIconEnabled: Boolean = false,
  endIconTint: Color = contentColor,
  onEndIconClicked: () -> Unit = { },
  isClickable: Boolean = false,
  onClick: () -> Unit = { }
) {
  Surface(
    modifier = Modifier.clickable(
      enabled = isClickable,
      onClick = { onClick() }
    ),
    elevation = 8.dp,
    shape = MaterialTheme.shapes.small,
    color = color,
    contentColor = contentColor
  ) {
    Row(verticalAlignment = Alignment.CenterVertically) {
      val leader = startIcon()
      val trailer = endIcon()

      if (leader != null) {
        Icon(
          leader,
          contentDescription = contentDescription,
          tint = startIconTint,
          modifier = Modifier
            .clickable(enabled = isStartIconEnabled, onClick = onStartIconClicked)
            .padding(horizontal = 4.dp)
        )
      }

      Text(
        label,
        modifier = Modifier.padding(8.dp),
        style = MaterialTheme.typography.button
      )

      if (trailer != null) {
        Icon(
          trailer,
          contentDescription = contentDescription,
          tint = endIconTint,
          modifier = Modifier
            .clickable(enabled = isEndIconEnabled, onClick = onEndIconClicked)
            .padding(horizontal = 4.dp)
        )
      }

    }
  }
}

@Composable
fun SelectableChip(
  label: String,
  contentDescription: String,
  selected: Boolean,
  selectedColor: Color = MaterialTheme.colors.primary,
  onClick: (nowSelected: Boolean) -> Unit
) {
  Chip(
    startIcon = { if (selected) Icons.Default.Check else null },
    contentDescription = contentDescription,
    label = label,
    color = if (selected) selectedColor else MaterialTheme.colors.surface,
    isClickable = true,
    onClick = { onClick(!selected) }
  )
}

@Composable
fun RemovableChip(
  label: String,
  contentDescription: String,
  onRemove: () -> Unit
) {
  Chip(
    endIcon = { Icons.Default.Clear },
    endIconTint = Color.Black.copy(alpha = 0.5f),
    contentDescription = contentDescription,
    label = label,
    onEndIconClicked = { onRemove() }
  )
}

@Preview
@Composable
fun SelectableChipPreviewSelected() {
  FloatingPannelScaffoldTheme {
    SelectableChip(
      label = "Selectable Chip",
      contentDescription = "SelectableChip",
      selected = true,
      onClick = {}
    )
  }
}

@Preview
@Composable
fun SelectableChipPreviewUnSelected() {
  SelectableChip(
    label = "Selectable Chip",
    contentDescription = "SelectableChip",
    selected = false,
    onClick = {}
  )
}

@Preview
@Composable
fun RemovableChipPreview() {
  RemovableChip(label = "Removable Chip", contentDescription = "RemovableChip") {}
}

