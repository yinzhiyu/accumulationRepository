package com.randy.training.ui.splash.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.randy.training.ui.splash.theme.GroundDividerPurple
import com.randy.training.R
import com.randy.training.ui.splash.model.GameAction
import com.randy.training.ui.splash.model.TempRoadWidthOffset
import com.randy.training.ui.splash.model.ViewState
import com.randy.training.ui.splash.util.LogUtil
import com.randy.training.ui.splash.viewmodel.GameViewModel

@Composable
fun NearForeground(
    modifier: Modifier = Modifier,
    state: ViewState = ViewState()
) {
    LogUtil.printLog(message = "NearForeground()")
    val viewModel: GameViewModel = viewModel()

    Column(
        modifier
    ) {
        // Divider between background and foreground
        Divider(
            color = GroundDividerPurple,
            thickness = 5.dp
        )

        // Road
        Box(modifier = Modifier.fillMaxWidth()) {
            state.roadStateList.forEach { roadState ->
                LogUtil.printLog(message = "NearForeground() roadState:$roadState")
                Image(
                    painter = painterResource(id = R.drawable.foreground_road),
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.23f)
                        .offset(x = roadState.offset)
                )
            }
        }

        // Earth
        Image(
            painter = painterResource(id = R.drawable.foreground_earth),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        )

        // Send road reset action when road dismissed.
        if (state.playZoneSize.first > 0) {
//            val playZoneWidthInDP = with(LocalDensity.current) {
//                state.playZoneSize.first.toDp()
//            }
            state.roadStateList.forEachIndexed { index, roadState ->
                LogUtil.printLog(message = "Road offset:${roadState.offset}")
                if (roadState.offset <= - TempRoadWidthOffset) {
                    // Road need reset.
                    LogUtil.printLog(message = "Road reset")
                    viewModel.dispatch(GameAction.RoadExit, roadIndex = index)
                }
            }
        }
    }
}

@Preview(widthDp = 411, heightDp = 180)
@Composable
fun previewForeground() {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Divider(
            color = GroundDividerPurple,
            thickness = 5.dp
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            // Road
            Image(
                painter = painterResource(id = R.drawable.foreground_road),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .offset(x = (-10).dp)
            )

            Image(
                painter = painterResource(id = R.drawable.foreground_road),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.23f)
                    .offset(x = 290.dp)
            )
        }

        // Earth
        Image(
            painter = painterResource(id = R.drawable.foreground_earth),
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.77f)
        )
    }
}