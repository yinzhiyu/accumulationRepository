package com.randy.training.ui.splash

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.randy.training.ui.main.MainActivity
import com.randy.training.ui.splash.theme.FlappyBirdTheme
import com.randy.training.ui.splash.model.GameAction
import com.randy.training.ui.splash.model.GameStatus
import com.randy.training.ui.splash.util.SplashScreenController
import com.randy.training.ui.splash.util.StatusBarUtil
import com.randy.training.ui.splash.view.Clickable
import com.randy.training.ui.splash.view.GameScreen
import com.randy.training.ui.splash.viewmodel.GameViewModel
import com.randy.training.utils.IntentUtil
import com.randy.training.utils.PermissionUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

class SplashActivity : AppCompatActivity() {
    private val viewModel: GameViewModel by viewModels()

    // `@RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Need to be called before setContentView or other view operation on the root view.
        val splashScreen = installSplashScreen()

        // Expand screen to status bar.
        StatusBarUtil.transparentStatusBar(this)
        PermissionUtil.verifyStoragePermissions(this)
        setContent {
            FlappyBirdTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val gameViewModel: GameViewModel = viewModel()

                    // Send a auto tick action to view model and trigger game start.
                    LaunchedEffect(key1 = Unit) {
                        while (isActive) {
                            delay(AutoTickDuration)
                            if (gameViewModel.viewState.value.gameStatus != GameStatus.Waiting) {
                                gameViewModel.dispatch(GameAction.AutoTick)
                            }
                        }
                    }

                    val lifecycleOwner = LocalLifecycleOwner.current
                    DisposableEffect(key1 = Unit) {
                        val observer = object : LifecycleObserver {
                            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
                            fun onPause() {
                                // Todo send pause action
                            }

                            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
                            fun onResume() {
                                // Todo send resume action
                            }
                        }

                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    Flappy(Clickable(

                        onStart = {
                            IntentUtil.go(this, MainActivity::class.java)
//                            gameViewModel.dispatch(GameAction.Start)
                        },

                        onTap = {
                            gameViewModel.dispatch(GameAction.TouchLift)
                        },

                        onRestart = {
                            gameViewModel.dispatch(GameAction.Restart)
                        },

                        onExit = {
                            finish()

                        }
                    ))
                }
            }
        }

        SplashScreenController(splashScreen, viewModel).apply {
            customizeSplashScreen()
        }

        // Log.d("Splash", "onCreate() splashScreen:${getSplashScreen()}}")
    }
}

@Composable
fun Flappy(clickable: Clickable = Clickable()) {
    GameScreen(clickable = clickable)
}

const val AutoTickDuration = 50L // 300L Control bird and pipe speed.