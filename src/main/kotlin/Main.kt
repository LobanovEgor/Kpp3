import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.*

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        var travelDistance by remember { mutableStateOf(0) }
        var energy by remember { mutableStateOf(100) }
        var time by remember { mutableStateOf(0) }
        var isTraveling by remember { mutableStateOf(false) }
        var restingDistance by remember { mutableStateOf(energy) } // расстояние для отдыха
        var isResting by remember { mutableStateOf(false) } // отдыхает ли путешественник

        val speed = 10
        val depletionRate = 10

        var job: Job? = null

        fun startTravel() {
            job = GlobalScope.launch {
                isTraveling = true
                while (isTraveling && energy >= 0) {
                    if (!isResting) {
                        travelDistance += speed
                        energy -= depletionRate
                        time++
                        delay(1000)
                    }
                    if (travelDistance >= restingDistance) {
                        isResting = true
                    }
                    if (isResting && energy < 100) {
                        energy += 20
                        delay(1000)// Восстановление энергии во время отдыха
                    }
                    if (isResting && energy == 100) {
                        isResting = false
                        restingDistance += 50 // Путешественник будет отдыхать каждый раз на большем расстоянии
                    }

                    if (energy <= 0) {
                        isTraveling = false
                    }

                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Travel Distance: $travelDistance")
            Text("Energy: $energy")
            Text("Time: $time")

            Button(onClick = {
                if (!isTraveling) {
                    startTravel()
                }
            }) {
                Text(if (isTraveling) "Traveling" else "Start Travel")
            }

            Button(onClick = {
                isTraveling = false
                job?.cancel()
            }) {
                Text("Stop Travel")
            }
        }
    }
}