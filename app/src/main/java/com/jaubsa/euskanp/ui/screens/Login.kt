package com.jaubsa.euskanp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import com.jaubsa.euskanp.ui.theme.EuskanpTheme
import com.jaubsa.euskanp.R
import androidx.compose.ui.res.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.jaubsa.euskanp.ui.utils.BezeroaEmail
import com.jaubsa.euskanp.ui.utils.updateLocale

@Composable
fun Login(
    onNavigateToProfila: (String) -> Unit = {} // Saioa hasterakoan, zein pantailara joango da.
) {
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }

    if (showLanguageDialog) {
        AlertDialog(
            onDismissRequest = { showLanguageDialog = false },
            title = { Text(text = stringResource(id = R.string.hizkuntzak)) },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            updateLocale(context, "eu") // Euskera
                            showLanguageDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Euskara", fontSize = 18.sp)
                    }
                    TextButton(
                        onClick = {
                            updateLocale(context, "es") // Castellano
                            showLanguageDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Español", fontSize = 18.sp)
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showLanguageDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    var erabiltzailea by remember { mutableStateOf("") } // Erabiltzailea
    var pasahitza by remember { mutableStateOf("") } // Pasahitza

    var errorMessage by remember { mutableStateOf("") } // Errore mezua erabiltzailea eta/edo pasahitza oker denean

    // Pantaila osoa
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondoaren irudia
        Image(
            painter = painterResource(R.drawable.portada),
            contentDescription = "fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.logoa),
            contentDescription = "Euskanp JAUB Logoa",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp)
                .size(160.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            // "Erabiltzailea" TextField-a
            OutlinedTextField(
                value = erabiltzailea,
                onValueChange = {
                    erabiltzailea = it
                    errorMessage = ""
                },
                label = { Text(stringResource(R.string.erabiltzailea)) },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF347969),
                    focusedLabelColor = Color(0xFF347969),
                    cursorColor = Color(0xFF347969)
                )
            )

            Spacer(modifier = Modifier.height(15.dp))

            // "Pasahitza" TextField-a
            OutlinedTextField(
                value = pasahitza,
                onValueChange = {
                    pasahitza = it
                    errorMessage = ""
                },
                label = { Text(stringResource(R.string.pasahitza)) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF347969),
                    focusedLabelColor = Color(0xFF347969),
                    cursorColor = Color(0xFF347969)
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true
            )

            // Error mezuaren style-a
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // "Saioa hasi" botoia justo debajo de los TextFields
            Button(
                onClick = {
                    val emailTrimmed = erabiltzailea.trim()
                    val passTrimmed = pasahitza.trim()

                    when {
                        emailTrimmed.isEmpty() || passTrimmed.isEmpty() -> {
                            errorMessage = "Eremu guztiak bete behar dira saioa hasteko!"
                        }
                        BezeroaEmail(context, emailTrimmed) == null -> {
                            errorMessage = "Email hori ez dago erregistratuta."
                        }
                        else -> {
                            errorMessage = ""
                            onNavigateToProfila(emailTrimmed)
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF347969)
                ),
                shape = RoundedCornerShape(size = 35.dp)
            ) {
                Text(
                    text = stringResource(R.string.saioa_hasi),
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    EuskanpTheme {
        Login()
    }
}