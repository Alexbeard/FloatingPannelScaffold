package com.example.floatingpannelscaffold.ui.floatingpanelscaffold

import androidx.compose.animation.*
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt

enum class BottomPanelValue {
  Collapsed,
  Expanded,
  HalfExpanded,
}

enum class SidePanelValue {
  Closed,
  Open;

  operator fun not(): SidePanelValue {
    return if (this == Closed) Open else Closed
  }
}

@ExperimentalMaterialApi
class BottomPanelState(
  initialValue: BottomPanelValue,
  animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
  confirmStateChange: (BottomPanelValue) -> Boolean = { true }
) : SwipeableState<BottomPanelValue>(
  initialValue = initialValue,
  animationSpec = animationSpec,
  confirmStateChange = confirmStateChange
) {

  val isCollapsed: Boolean
    get() = currentValue == BottomPanelValue.Collapsed

  val isHalfExpandedEnabled: Boolean
    get() = currentValue == BottomPanelValue.HalfExpanded

  suspend fun show() {
    val targetValue =
      if (isHalfExpandedEnabled) BottomPanelValue.HalfExpanded
      else BottomPanelValue.Expanded
    animateTo(targetValue = targetValue)
  }

  internal suspend fun halfExpand() {
    if (!isHalfExpandedEnabled) {
      return
    }
    animateTo(BottomPanelValue.HalfExpanded)
  }

  internal suspend fun expand() = animateTo(BottomPanelValue.Expanded)

  suspend fun hide() = animateTo(BottomPanelValue.Collapsed)

  val nestedScrollConnection = PreUpPostDownNestedScrollConnection

  val <T> SwipeableState<T>.PreUpPostDownNestedScrollConnection: NestedScrollConnection
    get() = object : NestedScrollConnection {
      override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
        val delta = available.toFloat()
        return if (delta < 0 && source == NestedScrollSource.Drag) {
          performDrag(delta).toOffset()
        } else {
          Offset.Zero
        }
      }

      override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource
      ): Offset {
        return if (source == NestedScrollSource.Drag) {
          performDrag(available.toFloat()).toOffset()
        } else {
          Offset.Zero
        }
      }

      override suspend fun onPreFling(available: Velocity): Velocity {
        val toFling = Offset(available.x, available.y).toFloat()
        return if (toFling < 0 && offset.value > 0f) {
          performFling(velocity = toFling)
          // since we go to the anchor with tween settling, consume all for the best UX
          available
        } else {
          Velocity.Zero
        }
      }

      override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
        performFling(velocity = Offset(available.x, available.y).toFloat())
        return available
      }

      private fun Float.toOffset(): Offset = Offset(0f, this)

      private fun Offset.toFloat(): Float = this.y
    }

  companion object {
    fun Saver(
      animationSpec: AnimationSpec<Float>,
      confirmStateChange: (BottomPanelValue) -> Boolean
    ): Saver<BottomPanelState, *> = Saver(
      save = { it.currentValue },
      restore = {
        BottomPanelState(
          initialValue = it,
          animationSpec = animationSpec,
          confirmStateChange = confirmStateChange
        )
      }
    )
  }
}

@Composable
@ExperimentalMaterialApi
fun rememberBottomPanelState(
  initialValue: BottomPanelValue,
  animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec,
  confirmStateChange: (BottomPanelValue) -> Boolean = { true }
): BottomPanelState {
  return rememberSaveable(
    saver = BottomPanelState.Saver(
      animationSpec = animationSpec,
      confirmStateChange = confirmStateChange
    )
  ) {
    BottomPanelState(
      initialValue = initialValue,
      animationSpec = animationSpec,
      confirmStateChange = confirmStateChange
    )
  }
}

@ExperimentalMaterialApi
@Stable
class FloatingPanelScaffoldState(
  val bottomPanelState: BottomPanelState
)

@Composable
@ExperimentalMaterialApi
fun rememberFloatingPanelScaffoldState(
  bottomPanelState: BottomPanelState = rememberBottomPanelState(BottomPanelValue.Collapsed),
): FloatingPanelScaffoldState {
  return remember(bottomPanelState) {
    FloatingPanelScaffoldState(
      bottomPanelState = bottomPanelState
    )
  }
}


@ExperimentalAnimationApi
@Composable
@ExperimentalMaterialApi
fun FloatingPanelScaffold(
  modifier: Modifier = Modifier,
  scaffoldState: FloatingPanelScaffoldState = rememberFloatingPanelScaffoldState(),
  sidePanelState: SidePanelValue = SidePanelValue.Closed,
  isInListMode: State<Boolean>,
  bottomPanelModifier: Modifier = Modifier,
  bottomPanelShape: Shape = RoundedCornerShape(15.dp),
  bottomPanelElevation: Dp = FloatingPanelScaffoldDefaults.Elevation,
  bottomPanelBackgroundColor: Color = MaterialTheme.colors.surface,
  bottomPanelContentColor: Color = contentColorFor(bottomPanelBackgroundColor),
  bottomPanelPeekHeight: Dp = FloatingPanelScaffoldDefaults.SheetPeekHeight,
  bottomPanelGesturesEnabled: Boolean = true,
  bottomPanelContent: @Composable ColumnScope.() -> Unit,
  sidePanelModifier: Modifier = Modifier,
  sidePanelShape: Shape = RoundedCornerShape(topStart = 15.dp, bottomStart = 15.dp),
  sidePanelElevation: Dp = FloatingPanelScaffoldDefaults.Elevation,
  sidePanelBackgroundColor: Color = MaterialTheme.colors.surface,
  sidePanelContentColor: Color = contentColorFor(sidePanelBackgroundColor),
  sidePanelContent: @Composable ColumnScope.() -> Unit,
  backgroundColor: Color = MaterialTheme.colors.background,
  contentColor: Color = contentColorFor(backgroundColor),
  content: @Composable (PaddingValues) -> Unit
) {
  BoxWithConstraints(modifier) {
    val fullHeight = constraints.maxHeight.toFloat()
    val peekHeightPx = with(LocalDensity.current) { bottomPanelPeekHeight.toPx() }
    val bottomSheetHeight = remember { mutableStateOf(fullHeight) }

    LaunchedEffect(isInListMode.value) {
      launch {
        if (isInListMode.value) {
          scaffoldState.bottomPanelState.expand()
        } else {
          scaffoldState.bottomPanelState.hide()
        }
      }
    }

    val swipeable = Modifier
      .then(
        if (!isInListMode.value)
          Modifier.nestedScroll(scaffoldState.bottomPanelState.nestedScrollConnection)
        else Modifier
      )
      .bottomPanelSwipeable(
        scaffoldState.bottomPanelState,
        fullHeight,
        peekHeightPx,
        bottomSheetHeight,
        if (isInListMode.value) false else bottomPanelGesturesEnabled
      )
      .onGloballyPositioned {
        bottomSheetHeight.value = it.size.height.toFloat()
      }

    FloatingPanelScaffoldStack(
      body = {
        Surface(
          color = backgroundColor,
          contentColor = contentColor
        ) {
          Column(Modifier.fillMaxSize()) {
            content(PaddingValues(bottom = bottomPanelPeekHeight))
          }
        }
      },
      bottomPanel = {
        Surface(
          swipeable
            .requiredHeightIn(min = bottomPanelPeekHeight)
            .then(bottomPanelModifier),
          shape = bottomPanelShape,
          elevation = bottomPanelElevation,
          color = bottomPanelBackgroundColor,
          contentColor = bottomPanelContentColor,
          content = { Column(content = bottomPanelContent) }
        )
      },
      sidePanel = {
        Surface(
          modifier = sidePanelModifier.then(
            if (isInListMode.value) Modifier.fillMaxHeight()
            else Modifier
          ),
          shape = sidePanelShape,
          elevation = sidePanelElevation,
          color = sidePanelBackgroundColor,
          contentColor = sidePanelContentColor,
          content = {
            Column {
              AnimatedVisibility(
                visible = sidePanelState == SidePanelValue.Open,
                enter = fadeIn() + expandHorizontally(),
                exit = fadeOut() + shrinkHorizontally()
              ) {
                sidePanelContent()
              }
            }
          }
        )
      },
      isInListMode = isInListMode.value,
      bottomPanelOffset = scaffoldState.bottomPanelState.offset
    )
  }
}

@Composable
private fun FloatingPanelScaffoldStack(
  body: @Composable () -> Unit,
  bottomPanel: @Composable () -> Unit,
  sidePanel: @Composable () -> Unit,
  isInListMode: Boolean,
  bottomPanelOffset: State<Float>,
) {
  Layout(
    content = {
      body()
      sidePanel()
      bottomPanel()
    }
  ) { measurables, constraints ->
    val placeable = measurables.first().measure(constraints)
    layout(placeable.width, placeable.height) {
      placeable.placeRelative(0, 0)
      val sidePanelPlaceable = measurables[1].measure(constraints.copy(minWidth = 0, minHeight = 0))
      val sheetPlaceable = measurables[2].let {
        if (isInListMode) {
          it.measure(
            constraints.copy(
              maxWidth = constraints.maxWidth - sidePanelPlaceable.width,
              minHeight = 0
            )
          )
        } else {
          it.measure(constraints.copy(minWidth = 0, minHeight = 0))
        }
      }
      // Condition to draw first either sidePanel or bottomPanel in box
      if (isInListMode) {
        val sheetOffsetY = bottomPanelOffset.value.roundToInt()
        sheetPlaceable.placeRelative(0, sheetOffsetY)
        sidePanelPlaceable.placeRelative(
          constraints.maxWidth - sidePanelPlaceable.width,
          constraints.maxHeight / 2 - sidePanelPlaceable.height / 2
        )
      } else {
        sidePanelPlaceable.placeRelative(
          constraints.maxWidth - sidePanelPlaceable.width,
          constraints.maxHeight / 2 - sidePanelPlaceable.height / 2
        )
        val sheetOffsetY = bottomPanelOffset.value.roundToInt()
        sheetPlaceable.placeRelative(0, sheetOffsetY)
      }
    }
  }
}


@ExperimentalMaterialApi
private fun Modifier.bottomPanelSwipeable(
  sheetState: BottomPanelState,
  fullHeight: Float,
  sheetPeakHeight: Float,
  sheetHeightState: State<Float?>,
  sheetGestureEnabled: Boolean
): Modifier {
  val sheetHeight = sheetHeightState.value
  val modifier = if (sheetHeight != null) {
    val anchors = if (sheetHeight < fullHeight / 2) {
      mapOf(
        fullHeight - sheetPeakHeight to BottomPanelValue.Collapsed,
        fullHeight - sheetHeight to BottomPanelValue.Expanded,
      )
    } else {
      mapOf(
        fullHeight - sheetPeakHeight to BottomPanelValue.Collapsed,
        fullHeight / 2 to BottomPanelValue.HalfExpanded,
        max(0f, fullHeight - sheetHeight) to BottomPanelValue.Expanded,
      )
    }
    Modifier.swipeable(
      state = sheetState,
      anchors = anchors,
      orientation = Orientation.Vertical,
      enabled = sheetGestureEnabled,
      resistance = null
    )
  } else {
    Modifier
  }

  return this.then(modifier)
}

object FloatingPanelScaffoldDefaults {
  val Elevation = 8.dp
  val SheetPeekHeight = 56.dp
}
