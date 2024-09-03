package br.edu.up.appplanta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.appplanta.ui.theme.AppPlantaTheme

class MainActivity : ComponentActivity() {

    companion object {
        private const val KEY_CLICKS = "key_clicks"
        private const val KEY_NUMBER = "key_number"
        private const val KEY_START = "key_start"
        private const val KEY_IS_GAME_OVER = "key_is_game_over"
    }

    private var clicks by mutableStateOf(0)
    private var n by mutableStateOf(GenerateNumber())
    private var start by mutableStateOf('y')
    private var isGameOver by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        savedInstanceState?.let {
            clicks = it.getInt(KEY_CLICKS, 0)
            n = it.getInt(KEY_NUMBER, GenerateNumber())
            start = it.getChar(KEY_START, 'y')
            isGameOver = it.getBoolean(KEY_IS_GAME_OVER, false)
        }

        setContent {
            AppPlantaTheme {
                Render(
                    clicks = clicks,
                    n = n,
                    start = start,
                    isGameOver = isGameOver,
                    onClickIncrement = { incrementClicks() },
                    onClickGiveUp = { giveUp() },
                    onRestart = { restartGame() },
                    onCloseApp = { finish() }
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CLICKS, clicks)
        outState.putInt(KEY_NUMBER, n)
        outState.putChar(KEY_START, start)
        outState.putBoolean(KEY_IS_GAME_OVER, isGameOver)
    }

    private fun incrementClicks() {
        clicks++
        if (clicks >= n) {
            isGameOver = true
        }
    }

    private fun giveUp() {
        clicks = 51
        isGameOver = true
    }

    private fun restartGame() {
        n = GenerateNumber()
        clicks = 0
        start = 'y'
        isGameOver = false
    }

    @Composable
    fun Render(
        clicks: Int,
        n: Int,
        start: Char,
        isGameOver: Boolean,
        onClickIncrement: () -> Unit,
        onClickGiveUp: () -> Unit,
        onRestart: () -> Unit,
        onCloseApp: () -> Unit
    ) {
        if (start == 'y') {
            Text(text = "$n",
            color = Color.Black,
            modifier = Modifier.padding(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Count: $clicks", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                if (clicks != n && clicks != 51) {
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedButton(
                        onClick = { onClickIncrement() },
                        containerColor = Color(200, 230, 10),
                        contentColor = Color.White,
                        icon = painterResource(id = R.drawable.ic_watering_can),
                        text = "Regar a planta!"
                    )

                    AnimatedButton(
                        onClick = { onClickGiveUp() },
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        icon = painterResource(id = R.drawable.veneno),
                        text = "Envenenar"
                    )
                }

                when {
                    clicks < (n * 0.33) -> {
                        Image(
                            painter = painterResource(id = R.drawable.planta1),
                            contentDescription = "Flor 1",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    clicks >= (n * 0.33) && clicks < (n * 0.66) -> {
                        Image(
                            painter = painterResource(id = R.drawable.planta2),
                            contentDescription = "Flor 2",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    clicks >= (n * 0.66) && clicks < (n * 0.99) -> {
                        Image(
                            painter = painterResource(id = R.drawable.planta3),
                            contentDescription = "Flor 3",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    clicks == n -> {
                        Text(
                            text = "Parabéns! Você concluiu a jornada do girassol!",
                            modifier = Modifier.fillMaxWidth()
                        )

                        Image(
                            painter = painterResource(id = R.drawable.sun),
                            contentDescription = "Sol",
                            modifier = Modifier,
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Jogar de novo?")
                            Spacer(modifier = Modifier.height(16.dp))

                            Row {
                                AnimatedButton(
                                    onClick = { onRestart() },
                                    containerColor = Color.Green,
                                    contentColor = Color.White,
                                    icon = null,
                                    text = "Sim"
                                )

                                AnimatedButton(
                                    onClick = { onCloseApp() },
                                    containerColor = Color.Red,
                                    contentColor = Color.White,
                                    icon = null,
                                    text = "Não"
                                )
                            }
                        }
                    }

                    clicks == 51 -> {
                        Image(
                            painter = painterResource(id = R.drawable.planeta_envenenado),
                            contentDescription = "Emoji chorando",
                            modifier = Modifier,
                            contentScale = ContentScale.Crop
                        )

                        Column(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(50.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Jogar de novo?")
                            Spacer(modifier = Modifier.height(16.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                AnimatedButton(
                                    onClick = { onRestart() },
                                    containerColor = Color.Green,
                                    contentColor = Color.White,
                                    icon = null,
                                    text = "Sim",
                                )

                                AnimatedButton(
                                    onClick = { onCloseApp() },
                                    containerColor = Color.Red,
                                    contentColor = Color.White,
                                    icon = null,
                                    text = "Não"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun AnimatedButton(
        onClick: () -> Unit,
        containerColor: Color,
        contentColor: Color,
        icon: Painter?,
        text: String,
    ) {
        var pressed by remember { mutableStateOf(false) }
        val scale by animateFloatAsState(
            targetValue = if (pressed) 0.95f else 1f,
            animationSpec = tween(durationMillis = 150)
        )

        Button(
            onClick = {
                pressed = !pressed
                onClick()
            },
            colors = ButtonDefaults.buttonColors(containerColor = containerColor, contentColor = contentColor),
            modifier = Modifier
                .scale(scale)
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                if (icon != null) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified // Não aplicar cor ao ícone
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = text, color = contentColor)
            }
        }
    }

    fun GenerateNumber(): Int {
        return (Math.random() * 50 + 1).toInt()
    }
}
