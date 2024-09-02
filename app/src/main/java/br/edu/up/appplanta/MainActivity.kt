package br.edu.up.appplanta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.edu.up.appplanta.ui.theme.AppPlantaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppPlantaTheme {
                var start by remember { mutableStateOf('y') }
                var n by remember { mutableStateOf(GenerateNumber()) }
                var clicks by remember { mutableStateOf(0) }
                if (start == 'y') {
                    Render(clicks, n, start,onClickIncrement = {clicks++}, onClickGiveUp = {clicks = 51}, onRestart = {
                        n = GenerateNumber()
                        clicks = 0
                        start = 'y' },
                        onCloseApp = {finish()})
                }
            }
        }
    }

}

@Composable
fun Render(clicks : Int, n : Int, start : Char,
           onClickIncrement: () -> Unit,
           onClickGiveUp: () -> Unit,
           onRestart: () -> Unit,
           onCloseApp: () -> Unit){

    if (start == 'y') {
//        Text(text = "$n",
//            color = Color.Black,
//            modifier = Modifier.padding(50.dp))

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            //Text(text = "Count: $clicks", color = Color.Black, fontSize = 24.sp, fontWeight = FontWeight.Bold)

            if (clicks != n && clicks != 51) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { onClickIncrement() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)) {
                    Text(text = "Regar a planta!", color = Color.White)

                }

                Button(onClick = { onClickGiveUp() },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black, contentColor = Color.White),
                ) {
                    Text(text = "Desistir", color = Color.White)
                }
            }

            if (clicks < (n  * 0.33)) {
                Image(painter = painterResource(id = R.drawable.planta1), contentDescription = "Flor 1",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }

            if (clicks >= (n * 0.33) && clicks < (n * 0.66)) {
                Image(painter = painterResource(id = R.drawable.planta2), contentDescription = "Flor 2",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }

            if (clicks >= (n * 0.66) && clicks < (n * 0.99)) {
                Image(painter = painterResource(id = R.drawable.planta3), contentDescription = "Flor 3",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop)
            }

            if (clicks == n) {
                Text(text = "Parabéns! Você concluiu a jornada do girassol!",
                    modifier = Modifier.fillMaxWidth())

                Image(painter = painterResource(id = R.drawable.sun), contentDescription = "Sol",
                    modifier = Modifier,
                    contentScale = ContentScale.Crop)

                Column (
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){

                    Text(text = "Jogar de novo?")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(onClick = { onRestart() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.White),
                        ) {
                            clicks == 0
                            Text(text = "Sim")

                        }

                        Button(onClick = { onCloseApp() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)) {
                            Text(text = "Não")
                        }
                    }


                }
            }

            if (clicks == 51) {
                Image(painter = painterResource(id = R.drawable.desistencia), contentDescription = "Emoji chorando",
                    modifier = Modifier,
                    contentScale = ContentScale.Crop)

                Column (
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(50.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "Jogar de novo?")
                    Spacer(modifier = Modifier.height(16.dp))

                    Row {
                        Button(onClick = { onRestart() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Green, contentColor = Color.White),
                        ) {
                            clicks == 0
                            Text(text = "Sim")

                        }

                        Button(onClick = { onCloseApp() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Red, contentColor = Color.White)) {
                            Text(text = "Não")
                        }
                    }


                }
            }
        }

    }
}

fun GenerateNumber() : Int {
    return (Math.random() * 50 + 1).toInt()
}


