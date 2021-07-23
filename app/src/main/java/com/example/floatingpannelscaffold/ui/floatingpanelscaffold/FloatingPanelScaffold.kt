package com.example.floatingpannelscaffold.ui.floatingpanelscaffold

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlin.math.max
import kotlin.math.roundToInt

enum class BottomPanelValue {
  Collapsed,
  Expanded,
  HalfExpanded,
}

enum class SidePanelValue {
  Closed,
  Open,
}


@Suppress("NotCloseable")
@OptIn(ExperimentalMaterialApi::class)
@Stable
class SidePanelState(
  initialValue: SidePanelValue,
  confirmStateChange: (SidePanelValue) -> Boolean = { true }
) {

  val swipeableState = SwipeableState(
    initialValue = initialValue,
    animationSpec = AnimationSpec,
    confirmStateChange = confirmStateChange
  )

  val isOpen: Boolean
    get() = currentValue == SidePanelValue.Open

  val isClosed: Boolean
    get() = currentValue == SidePanelValue.Closed

  val currentValue: SidePanelValue
    get() {
      return swipeableState.currentValue
    }

  val isAnimationRunning: Boolean
    get() {
      return swipeableState.isAnimationRunning
    }

  suspend fun open() = animateTo(SidePanelValue.Open, AnimationSpec)

  suspend fun close() = animateTo(SidePanelValue.Closed, AnimationSpec)

  @ExperimentalMaterialApi
  suspend fun animateTo(targetValue: SidePanelValue, anim: AnimationSpec<Float>) {
    swipeableState.animateTo(targetValue, anim)
  }


  @ExperimentalMaterialApi
  suspend fun snapTo(targetValue: SidePanelValue) {
    swipeableState.snapTo(targetValue)
  }

  @ExperimentalMaterialApi
  @get:ExperimentalMaterialApi
  val targetValue: SidePanelValue
    get() = swipeableState.targetValue


  @ExperimentalMaterialApi
  @get:ExperimentalMaterialApi
  val offset: State<Float>
    get() = swipeableState.offset

  companion object {

    fun Saver(confirmStateChange: (SidePanelValue) -> Boolean) =
      Saver<SidePanelState, SidePanelValue>(
        save = { it.currentValue },
        restore = { SidePanelState(it, confirmStateChange) }
      )
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


  internal val isHalfExpandedEnabled: Boolean
    get() = anchors.values.contains(BottomPanelValue.HalfExpanded)

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

  internal val nestedScrollConnection = this.PreUpPostDownNestedScrollConnection

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


@Composable
fun rememberSidePanelState(
  initialValue: SidePanelValue,
  confirmStateChange: (SidePanelValue) -> Boolean = { true }
): SidePanelState {
  return rememberSaveable(saver = SidePanelState.Saver(confirmStateChange)) {
    SidePanelState(
      initialValue = initialValue,
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


@Composable
@ExperimentalMaterialApi
fun FloatingPanelScaffold(
  modifier: Modifier = Modifier,
  scaffoldState: FloatingPanelScaffoldState = rememberFloatingPanelScaffoldState(),
  bottomPanelContent: @Composable ColumnScope.() -> Unit,
  bottomPanelModifier: Modifier = Modifier,
  bottomPanelShape: Shape = MaterialTheme.shapes.large,
  bottomPanelElevation: Dp = BottomPanelScaffoldDefaults.SheetElevation,
  bottomPanelBackgroundColor: Color = MaterialTheme.colors.surface,
  bottomPanelContentColor: Color = contentColorFor(bottomPanelBackgroundColor),
  bottomPanelPeekHeight: Dp = BottomPanelScaffoldDefaults.SheetPeekHeight,
  bottomPanelGesturesEnabled: Boolean = true,
  sidePanelContent: @Composable ColumnScope.() -> Unit,
  sidePanelModifier: Modifier = Modifier,
  backgroundColor: Color = MaterialTheme.colors.background,
  contentColor: Color = contentColorFor(backgroundColor),
  content: @Composable (PaddingValues) -> Unit
) {
  BoxWithConstraints(modifier) {
    val fullHeight = constraints.maxHeight.toFloat()
    val peekHeightPx = with(LocalDensity.current) { bottomPanelPeekHeight.toPx() }
    val bottomSheetHeight = remember { mutableStateOf(fullHeight) }

    val swipeable = Modifier
      .nestedScroll(scaffoldState.bottomPanelState.nestedScrollConnection)
      .bottomPanelSwipeable(
        scaffoldState.bottomPanelState,
        fullHeight,
        peekHeightPx,
        bottomSheetHeight,
        bottomPanelGesturesEnabled
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
            .requiredHeightIn(min = bottomPanelPeekHeight) then bottomPanelModifier,
          shape = bottomPanelShape,
          elevation = bottomPanelElevation,
          color = bottomPanelBackgroundColor,
          contentColor = bottomPanelContentColor,
          content = { Column(content = bottomPanelContent) }
        )
      },
      sidePanel = {
        Surface(
          modifier = sidePanelModifier,
          shape = RoundedCornerShape(topStart = 10.dp, bottomStart = 10.dp),
          elevation = bottomPanelElevation,
          color = bottomPanelBackgroundColor,
          contentColor = bottomPanelContentColor,
          content = { Column(content = sidePanelContent) }
        )
      },
      bottomPanelOffset = scaffoldState.bottomPanelState.offset
    )
  }
}

@Composable
private fun FloatingPanelScaffoldStack(
  body: @Composable () -> Unit,
  bottomPanel: @Composable () -> Unit,
  sidePanel: @Composable () -> Unit,
  bottomPanelOffset: State<Float>,
) {
  Layout(
    content = {
      body()
      bottomPanel()
      sidePanel()
    }
  ) { measurables, constraints ->
    val placeable = measurables.first().measure(constraints)
    layout(placeable.width, placeable.height) {
      placeable.placeRelative(0, 0)
      val (sheetPlaceable, sidePanelPlaceable) = measurables.drop(1).map {
        it.measure(constraints.copy(minWidth = 0, minHeight = 0))
      }
      sidePanelPlaceable.placeRelative(
        constraints.maxWidth - sidePanelPlaceable.width,
        constraints.maxHeight / 2 - sidePanelPlaceable.height / 2
      )
      val sheetOffsetY = bottomPanelOffset.value.roundToInt()
      sheetPlaceable.placeRelative(0, sheetOffsetY)
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

object BottomPanelScaffoldDefaults {
  val SheetElevation = 8.dp
  val SheetPeekHeight = 56.dp
}

private val AnimationSpec = TweenSpec<Float>(durationMillis = 256)


