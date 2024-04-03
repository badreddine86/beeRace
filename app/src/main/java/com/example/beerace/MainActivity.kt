package com.example.beerace
import ErrorScreen
import WinnerScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            JetpackComposeTheme {
                Surface(color = MaterialTheme.colors.background) {
                    ProvideBeeListViewModel { viewModel ->
                        NavHost(navController, startDestination = "firstScreen") {
                            composable("firstScreen") {
                                CenteredButton(navController)
                            }
                            composable("secondScreen") {
                                BeeListScreen(viewModel = viewModel, navController = navController)
                            }
                            composable(
                                route = "thirdScreen/{winnerName}/{winnerColor}",
                                arguments = listOf(
                                    navArgument("winnerName") { type = NavType.StringType },
                                    navArgument("winnerColor") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val winnerName = backStackEntry.arguments?.getString("winnerName") ?: ""
                                val winnerColor = backStackEntry.arguments?.getString("winnerColor")?.let { Color(android.graphics.Color.parseColor(it)) }
                                    ?: Color.Unspecified
                                WinnerScreen(winnerName = winnerName, winnerColor = winnerColor, navController = navController)
                            }
                            composable("errorScreen") {
                                ErrorScreen()
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun JetpackComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(content = content)
}

@Composable
private fun CenteredButton(navHostController: NavHostController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { navHostController.navigate("secondScreen")},
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Black,
                contentColor = Color.White)
        ) {
            Text(text = "Start Bee Race")
        }
    }
}

@Composable
fun ProvideBeeListViewModel(
    content: @Composable (BeeListViewModel) -> Unit
) {
    val beeListViewModel = remember { BeeListViewModel(BeeRepository(ApiBeeService.create())) }
    content(beeListViewModel)
}

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    val navHostController = rememberNavController()
    JetpackComposeTheme {
        CenteredButton(navHostController)
    }
}
