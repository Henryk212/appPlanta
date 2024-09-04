package br.edu.up.appplanta

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.edu.up.appplanta.ui.theme.AppPlantaTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Alunos - Projeto Regar Planta:
 *  - Gabriel Correia André - RGM: 34379878
 *  - Henrique Onorato - RGM: 34395857
 *  - Francyne Leocadio Ramos - RGM: 32876131
 *  - Renan Luiz dos Santos - RGM: 34572309
 */

class MainActivity : ComponentActivity() {

    companion object {
        // Constantes usadas para salvar e restaurar o estado da atividade
        private const val KEY_CLICKS = "key_clicks"             // Chave para armazenar o número de cliques
        private const val KEY_NUMBER = "key_number"             // Chave para armazenar o número alvo
        private const val KEY_IS_GAME_OVER = "key_is_game_over" // Chave para armazenar o estado de fim de jogo
    }

    private var clicks by mutableStateOf(0)         // Variável que guarda o número de cliques, usando Compose para reatividade
    private var n by mutableStateOf(generateNumber())     // Variável que guarda o número alvo gerado aleatoriamente
    private var isGameOver by mutableStateOf(false) // Variável que indica se o jogo acabou

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Configura a tela para usar toda a área disponível
        enableEdgeToEdge()

        // Se existir um estado salvo, restaura os valores das variáveis
        savedInstanceState?.apply {
            clicks = getInt(KEY_CLICKS, 0)                       // Restaura o número de cliques
            n = getInt(KEY_NUMBER, generateNumber())             // Restaura o número alvo ou gera um novo
            isGameOver = getBoolean(KEY_IS_GAME_OVER, false)     // Restaura o estado de fim de jogo
        }

        setContent {
            AppPlantaTheme {  // Aplica o tema do aplicativo
                GameScreen(
                    clicks = clicks,                           // Passa o número de cliques atual para a tela do jogo
                    targetNumber = n,                          // Passa o número alvo atual para a tela do jogo
                    isGameOver = isGameOver,                   // Passa o estado de fim de jogo para a tela do jogo
                    onClickIncrement = { incrementClicks() },  // Ação para incrementar os cliques
                    onClickGiveUp = { giveUp() },              // Ação para desistir do jogo
                    onRestart = { restartGame() },             // Ação para reiniciar o jogo
                    onCloseApp = { finish() }                  // Ação para fechar o aplicativo
                )
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Salva o estado atual da atividade (número de cliques, número alvo, e estado de fim de jogo)
        outState.run {
            putInt(KEY_CLICKS, clicks)              // Salva o número de cliques
            putInt(KEY_NUMBER, n)                   // Salva o número alvo
            putBoolean(KEY_IS_GAME_OVER, isGameOver) // Salva o estado de fim de jogo
        }
    }

    @Composable
    fun GameScreen(
        clicks: Int,
        targetNumber: Int,
        isGameOver: Boolean,
        onClickIncrement: () -> Unit,
        onClickGiveUp: () -> Unit,
        onRestart: () -> Unit,
        onCloseApp: () -> Unit
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Exibe o número alvo no topo da tela
            Text(
                text = "Número: #$targetNumber",
                color = Color.Black,
                modifier = Modifier.padding(16.dp)
            )

            // Exibe o número de cliques atual
            Text(
                text = "Clicks: $clicks",
                color = Color.Black,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Se o jogo ainda não acabou, exibe os botões de ação
            if (!isGameOver) {
                ActionButtons(onClickIncrement, onClickGiveUp)
            }

            // Exibe o conteúdo baseado no número de cliques
            DisplayContentBasedOnClicks(clicks, targetNumber, onRestart, onCloseApp)
        }
    }

    @Composable
    fun ActionButtons(onClickIncrement: () -> Unit, onClickGiveUp: () -> Unit) {
        // Função que exibe os botões de ação (regar planta e envenenar)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            // Botão para incrementar os cliques (regar a planta)
            AnimatedButton(
                onClick = onClickIncrement,               // Função callback chamada ao clicar no botão
                containerColor = Color(200, 230, 10),
                contentColor = Color.White,
                icon = painterResource(id = R.drawable.ic_watering_can),
                text = "Regar a planta!"
            )

            // Botão para desistir do jogo (envenenar a planta)
            AnimatedButton(
                onClick = onClickGiveUp,                  // Função callback chamada ao clicar no botão
                containerColor = Color.Black,
                contentColor = Color.White,
                icon = painterResource(id = R.drawable.veneno),
                text = "Envenenar"
            )
        }
    }

    @Composable
    fun DisplayContentBasedOnClicks(
        clicks: Int,
        targetNumber: Int,
        onRestart: () -> Unit,
        onCloseApp: () -> Unit
    ) {
        // Exibe conteúdo com base no progresso do usuário em relação ao número alvo
        when {
            clicks < (targetNumber * 0.33) -> {
                DisplayImage(R.drawable.image_1, "Girassol Fase 1")
            }
            clicks < (targetNumber * 0.66) -> {
                DisplayImage(R.drawable.image_2, "Girassol Fase 2")
            }
            clicks < targetNumber -> {
                DisplayImage(R.drawable.image_3, "Girassol Fase 3")
            }
            clicks == targetNumber -> {
                // Exibe a tela de vitória e a imagem do girassol completo quando o alvo é atingido
                GameWonScreen(onRestart, onCloseApp)
                DisplayImage(R.drawable.image_4, "Girassol Completo")
            }
            isGameOver -> {
                // Exibe a tela de tentativa de novo jogo e a imagem do planeta envenenado se o jogo terminar
                RetryScreen(onRestart, onCloseApp)
                DisplayImage(R.drawable.planeta_envenenado, "Planeta Envenenado")
            }
        }
    }

    // Exibe as imagens de fundo
    @Composable
    fun DisplayImage(imageRes: Int, contentDescription: String) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = contentDescription,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }

    // Exibe a tela final quando o número final de clicks é atingido
    @Composable
    fun GameWonScreen(onRestart: () -> Unit, onCloseApp: () -> Unit) {
        ResultScreen(
            message = "Parabéns! Você concluiu a jornada do girassol! Deseja jogar novamente?",
            onRestart = onRestart,
            onCloseApp = onCloseApp
        )
    }

    // Exibe a tela para reiniciar o jogo
    @Composable
    fun RetryScreen(onRestart: () -> Unit, onCloseApp: () -> Unit) {
        ResultScreen(
            message = "Deseja jogar novamente?",
            onRestart = onRestart,
            onCloseApp = onCloseApp
        )
    }

    // Função genérica para exibir cards e botões de ações
    @Composable
    fun ResultScreen(message: String, onRestart: () -> Unit, onCloseApp: () -> Unit) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(56, 142, 60)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = message,
                    fontSize = 16.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AnimatedButton(
                    onClick = onRestart,
                    containerColor = Color.Green,
                    contentColor = Color.White,
                    icon = null,
                    text = "Sim"
                )
                AnimatedButton(
                    onClick = onCloseApp,
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    icon = null,
                    text = "Não"
                )
            }
        }
    }

    // Função para exibir botões com animação
    @Composable
    fun AnimatedButton(
        onClick: () -> Unit,
        containerColor: Color,
        contentColor: Color,
        icon: Painter?,
        text: String
    ) {
        // Estado que indica se o botão está pressionado
        var pressed by remember { mutableStateOf(false) }

        // Escopo de coroutine para gerenciar o reset do estado de "pressed"
        val coroutineScope = rememberCoroutineScope()

        // Animação para a escala do botão, criando um efeito de "bounce" (saltitar)
        val scale by animateFloatAsState(
            targetValue = if (pressed) 0.9f else 1f, // O botão fica ligeiramente menor quando pressionado
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy, // Configuração para um efeito elástico médio
                stiffness = Spring.StiffnessLow // Configuração de rigidez para um movimento mais suave
            )
        )

        // Animação para a cor de fundo do botão quando ele é pressionado
        val backgroundColor by animateColorAsState(
            targetValue = if (pressed) containerColor.copy(alpha = 0.8f) else containerColor, // O fundo fica ligeiramente mais escuro ao ser pressionado
            animationSpec = tween(durationMillis = 200) // Animação suave ao longo de 200 milissegundos
        )

        Button(
            onClick = {
                pressed = true
                onClick()

                // Lança uma coroutine para redefinir o estado de "pressed" após um pequeno atraso
                coroutineScope.launch {
                    delay(150) // Aguarda 150 milissegundos para permitir que a animação ocorra
                    pressed = false // Reseta o estado de "pressed" para falso, retornando o botão ao estado original
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = contentColor
            ),
            modifier = Modifier
                .scale(scale)
                .padding(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                icon?.let {
                    // Exibe o ícone, se existir
                    Icon(
                        painter = it,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(text = text, color = contentColor)
            }
        }
    }

    // Gera um número aleatório entre 1 e 50
    private fun generateNumber(): Int {
        return (1..50).random()
    }

    // Gerencia o incremento de clicks
    private fun incrementClicks() {
        clicks++
        if (clicks >= n) {
            isGameOver = true
        }
    }

    // Gerencia a desistência do jogo
    private fun giveUp() {
        clicks = n + 1
        isGameOver = true
    }

    // Gerencia o estado para reiniciar o jogo
    private fun restartGame() {
        n = generateNumber()
        clicks = 0
        isGameOver = false
    }
}
