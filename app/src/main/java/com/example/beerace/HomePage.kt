package com.example.beerace

import ErrorScreen
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay


@Composable
fun BeeListScreen(viewModel: BeeListViewModel, navController: NavController) {

    val bees by viewModel.bees
    val errorMessage by viewModel.errorMessage
    val raceDuration by viewModel.raceDuration

    if (errorMessage != null && errorMessage!!.contains("429")) {
        ErrorScreen()
    } else if (errorMessage != null && errorMessage!!.contains("403")){
        AndroidView(factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                loadUrl("https://www.google.com/recaptcha/api2/demo")
            }
        })
    } else {
        Column {
            raceDuration?.let { CountdownTimer(bees, it, navController) }
            Spacer(modifier = Modifier.height(16.dp))
            BeeList(bees = bees)
        }
    }
}

@Composable
fun CountdownTimer(
    bees: List<Bee>,
    raceDuration: RaceDurationReponse,
    navController: NavController
) {
    var secondsRemaining by remember { mutableStateOf(raceDuration.timeInSeconds) }

    LaunchedEffect(secondsRemaining) {
        while (secondsRemaining > 0) {
            delay(1000)
            secondsRemaining--
        }

        if (bees.isNotEmpty()) {
            val winner = bees.first()
            navController.navigate("thirdScreen/${winner.name}/${winner.color}")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .height(64.dp)
    ) {
        Text(
            text = "Time Remaining: \n $secondsRemaining",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )
    }
}

@Composable
fun BeeList(bees: List<Bee>) {
    LazyColumn {
        itemsIndexed(bees) { index, bee ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            Color(android.graphics.Color.parseColor(bee.color)),
                            shape = CircleShape
                        )
                        .clip(CircleShape)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_bee),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                Column() {
                    Text(
                        text = if (index == 0) "${index + 1}st" else if (index == 1) "${index + 1}nd" else "${index + 1}th",
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(text = bee.name)
                }
                Spacer(modifier = Modifier.weight(1f))

                if (index < 3)
                    Box(
                        modifier = Modifier
                            .size(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(
                                id = when (index) {
                                    0 -> R.drawable.med1
                                    1 -> R.drawable.med2
                                    2 -> R.drawable.med3
                                    else -> throw IllegalStateException("Unexpected index: $index")
                                }
                            ),
                            contentDescription = null
                        )
                    }

            }
        }
    }
}

@Preview
@Composable
fun PreviewBeeListScreen() {
    val viewModel: BeeListViewModel = viewModel()
    BeeListScreen(viewModel = viewModel, navController = rememberNavController())
}