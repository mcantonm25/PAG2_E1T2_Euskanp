package com.jaubsa.euskanp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jaubsa.euskanp.ui.screens.*
import com.jaubsa.euskanp.ui.theme.EuskanpTheme
import com.jaubsa.euskanp.ui.utils.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EuskanpTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNav(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNav(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val sessionManager = remember { SessionManager(context) }

    val savedEmail = remember { sessionManager.getUserSession() }
    val initialDestination = if (!savedEmail.isNullOrEmpty()) {
        "profila/$savedEmail"
    } else {
        "portada"
    }

    NavHost(
        navController = navController,
        startDestination = initialDestination,
        modifier = modifier
    ) {
        // Portada.kt
        composable("portada") {
            Portada(onNavigateToLogin = {
                navController.navigate("login")
            })
        }

        // Login.kt
        composable("login") {
            Login(onNavigateToProfila = { emaila ->
                sessionManager.saveUserSession(emaila)
                navController.navigate("profila/$emaila") {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        // Profila.kt
        composable("profila/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""

            BackHandler {
                (context as? Activity)?.finish()
            }

            Profila(
                userEmail = userEmail,
                onNavigateToEzarpenak = {
                    navController.navigate("ezarpenak/$userEmail") {
                        launchSingleTop = true
                    }
                },
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToArrigorriaga = {
                    navController.navigate("arrigorriaga") {
                        launchSingleTop = true
                    }
                },
                onNavigateToEkitaldiak = {
                    navController.navigate("ekitaldiak/$userEmail") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Arrigorriaga.kt
        composable("arrigorriaga") {
            val currentEmail = sessionManager.getUserSession() ?: ""
            BackHandler {
                navController.navigate("profila/$currentEmail") {
                    popUpTo("profila/$currentEmail") { inclusive = true }
                }
            }
            Arrigorriaga(
                onNavigateToProfila = {
                    navController.navigate("profila/$currentEmail") {
                        popUpTo("profila/$currentEmail") { inclusive = true }
                    }
                }
            )
        }

        // Ezarpenak.kt
        composable("ezarpenak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Ezarpenak(
                onNavigateToProfila = {
                    navController.navigate("profila/$userEmail") {
                        popUpTo("profila/$userEmail") { inclusive = true }
                    }
                },
                onNavigateToDatuak = {
                    navController.navigate("datuak/$userEmail") {
                        launchSingleTop = true
                    }
                },
                onNavigateToJakinarazpenak = {
                    navController.navigate("jakinarazpenak/$userEmail") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Datuak.kt
        composable("datuak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Datuak(
                userEmail = userEmail,
                onNavigateToEzarpenak = {
                    navController.popBackStack()
                }
            )
        }

        // Ekitaldiak.kt
        composable("ekitaldiak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Ekitaldiak(
                userEmail = userEmail,
                onNavigateToProfila = {
                    navController.navigate("profila/$userEmail") {
                        popUpTo("profila/$userEmail") { inclusive = true }
                    }
                }
            )
        }

        // Jakinarazpenak.kt
        composable("jakinarazpenak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("ezarpenak/$userEmail") {
                    popUpTo("ezarpenak/$userEmail") { inclusive = true }
                }
            }
            Jakinarazpenak(
                onNavigateToProfila = {
                    navController.navigate("ezarpenak/$userEmail") {
                        popUpTo("ezarpenak/$userEmail") { inclusive = true }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    EuskanpTheme {
        AppNav()
    }
}