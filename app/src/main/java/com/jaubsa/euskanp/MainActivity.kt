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
    val navController = rememberNavController() // Beharrezkoa da NavHost-entzako
    val sessionManager = remember { SessionManager(context) } // remember honekin, sesioa gordetzen dugu memorian saioa ez ixteko automatikoki.

    val savedEmail = remember { sessionManager.getUserSession() }
    val initialDestination = if (!savedEmail.isNullOrEmpty()) {
        "profila/$savedEmail"
    } else {
        "portada"
    }

    // NavHost erabiliz, kudeatu daiteke funtzioak joateko pantailetara. Adibidez: botoi bat joaten dena "Jakinarazpenak" pantailara "MenuNagusia" pantailatik
    NavHost(
        navController = navController,
        startDestination = initialDestination,
        modifier = modifier
    ) {
        // Portada.kt-ren nabigazio funtzioak
        composable("portada") {
            // Login.kt pantailara joateko erabiltzen den funtzioa Portada.kt-ean
            Portada(onNavigateToLogin = {
                navController.navigate("login")
            })
        }

        // Login.kt-ren nabigazio funtzioak
        composable("login") {
            // Profila.kt pantailara joateko erabiltzen den funtzioa login egitean Login.kt pantailatik
            Login(onNavigateToProfila = { emaila ->
                sessionManager.saveUserSession(emaila)
                navController.navigate("profila/$emaila") {
                    popUpTo(0) { inclusive = true }
                }
            })
        }

        // Profila.kt-ren nabigazio funtzioak
        composable("profila/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""

            BackHandler {
                (context as? Activity)?.finish()
            }

            Profila(
                userEmail = userEmail,

                // Ezarpenak.kt pantailara joateko erabiltzen den funtzioa Profila.kt-ean
                onNavigateToEzarpenak = {
                    navController.navigate("ezarpenak/$userEmail") {
                        launchSingleTop = true
                    }
                },

                // Saioa ixteko erabiltzen den funtzioa Profila.kt-ean
                onLogout = {
                    sessionManager.clearSession()
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },

                // Arrigorriaga.kt pantailara joateko erabiltzen den funtzioa Profila.kt-ean
                onNavigateToArrigorriaga = {
                    navController.navigate("arrigorriaga") {
                        launchSingleTop = true
                    }
                },

                // Ekitaldiak.kt pantailara joateko erabiltzen den funtzioa Profila.kt-ean
                onNavigateToEkitaldiak = {
                    navController.navigate("ekitaldiak/$userEmail") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Arrigorriaga.kt-ren nabigazio funtzioak
        composable("arrigorriaga") {
            val currentEmail = sessionManager.getUserSession() ?: ""
            BackHandler {
                navController.navigate("profila/$currentEmail") {
                    popUpTo("profila/$currentEmail") { inclusive = true }
                }
            }
            Arrigorriaga(
                // Profile.kt pantailara joateko erabiltzen den funtzioa Arrigorriaga.kt-ean
                onNavigateToProfila = {
                    navController.navigate("profila/$currentEmail") {
                        popUpTo("profila/$currentEmail") { inclusive = true }
                    }
                }
            )
        }

        // Ezarpenak.kt-ren nabigazio funtzioak
        composable("ezarpenak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: "" // Oraingo saioa identifikatzen dugu baieztatzeko nork dagoen saioarekin
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Ezarpenak(
                // Profila.kt pantailara joateko erabiltzen den funtzioa Ezarpenak.kt-ean
                onNavigateToProfila = {
                    navController.navigate("profila/$userEmail") {
                        popUpTo("profila/$userEmail") { inclusive = true }
                    }
                },
                // Datuak.kt pantailara joateko erabiltzen den funtzioa Ezarpenak.kt-ean
                onNavigateToDatuak = {
                    navController.navigate("datuak/$userEmail") {
                        launchSingleTop = true
                    }
                },
                // Jakinarazpenak.kt pantailara joateko erabiltzen den funtzioa Ezarpenak.kt-ean
                onNavigateToJakinarazpenak = {
                    navController.navigate("jakinarazpenak/$userEmail") {
                        launchSingleTop = true
                    }
                }
            )
        }

        // Datuak.kt-ren nabigazio funtzioak
        composable("datuak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Datuak(
                userEmail = userEmail,
                // Ezarpenak.kt pantailara joateko erabiltzen den funtzioa Datuak.kt-ean
                onNavigateToEzarpenak = {
                    navController.popBackStack()
                }
            )
        }

        // Ekitaldiak.kt-ren nabigazio funtzioak
        composable("ekitaldiak/{userEmail}") { backStackEntry ->
            val userEmail = backStackEntry.arguments?.getString("userEmail") ?: ""
            BackHandler {
                navController.navigate("profila/$userEmail") {
                    popUpTo("profila/$userEmail") { inclusive = true }
                }
            }
            Ekitaldiak(
                userEmail = userEmail,
                // Profila.kt pantailara joateko erabiltzen den funtzioa Ekitaldia.kt-ean
                onNavigateToProfila = {
                    navController.navigate("profila/$userEmail") {
                        popUpTo("profila/$userEmail") { inclusive = true }
                    }
                }
            )
        }

        // Jakinarazpenak.kt-ren nabigazio funtzioak
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