package com.jaubsa.euskanp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import com.jaubsa.euskanp.ui.theme.EuskanpTheme
import com.jaubsa.euskanp.R
import androidx.compose.ui.res.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.jaubsa.euskanp.ui.utils.BezeroaEmail
import com.jaubsa.euskanp.ui.utils.gordeDatuak
import com.jaubsa.euskanp.ui.utils.SessionManager

@Composable
fun Datuak(
    userEmail: String = "", // Erabiltzailea TextField-erako beharrezkoa den String-a JSON-etik hartzeko datua
    onNavigateToEzarpenak: () -> Unit // "Atzera" botoiaren zeregina
) {
    val context = LocalContext.current

    val sessionManager = remember { SessionManager(context) }
    var currentSavedEmail by remember(userEmail) { mutableStateOf(userEmail) }

    // TextField-etan JSON fitxategitik kargatzeko datuak
    var erabiltzailea by remember { mutableStateOf("") }
    var telefonoa by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(userEmail) }

    LaunchedEffect(userEmail) {
        if (userEmail.isNotEmpty()) {
            val bezeroa = BezeroaEmail(context, userEmail)
            bezeroa?.let {
                erabiltzailea = "${it.izena} ${it.abizena}".trim() // Izena eta abizena
                telefonoa = it.telefonoa
                email = it.emaila
            }
        }
    }

    // Pantaila osoa
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondoaren irudia
        Image(
            painter = painterResource(R.drawable.portada),
            contentDescription = "fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize() // fillMaxSize() erabiltzen dugu pantaila osoan fondoa izateko
        )

        // Pantaila
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp) // Padding-a ezartzen dugu bordeak adierazteko
        ) {
            // Header lerroa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Euskanp logoa
                Image(
                    painter = painterResource(R.drawable.logoa),
                    contentDescription = "Euskanp JAUB logoa",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(75.dp)
                )
            }

            // "Nire datuak" titulua
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.nire_datuak),
                    style = TextStyle(fontSize = 23.sp, fontWeight = FontWeight(800))
                )
            }

            // TextField-ak kokatzeko Column-a
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Izen-abizenak TextField-a
                OutlinedTextField(
                    value = erabiltzailea,
                    onValueChange = { erabiltzailea = it },
                    label = { Text(stringResource(R.string.izen_abizenak)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF347969),
                        focusedLabelColor = Color(0xFF347969),
                        cursorColor = Color(0xFF347969)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Telefonoa TextField-a
                OutlinedTextField(
                    value = telefonoa,
                    onValueChange = { telefonoa = it },
                    label = { Text(stringResource(R.string.telefonoa)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF347969),
                        focusedLabelColor = Color(0xFF347969),
                        cursorColor = Color(0xFF347969)
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Emaila TextField-a
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(stringResource(R.string.emaila)) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF347969),
                        focusedLabelColor = Color(0xFF347969),
                        cursorColor = Color(0xFF347969)
                        )
                    )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val cleanNewEmail = email.trim()
                        val a = gordeDatuak(
                            context = context,
                            oldEmail = currentSavedEmail,
                            newIzenAbizena = erabiltzailea,
                            newTelefonoa = telefonoa,
                            newEmail = cleanNewEmail
                        )
                        if (a) {
                            Toast.makeText(context, "Datuak eguneratu dira!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Errorea gordetzean", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF347969)
                    ),
                    modifier = Modifier
                        .height(60.dp)
                        .padding(end = 8.dp)
                ) {
                    Text(
                        text = stringResource(R.string.gorde),
                        fontSize = 18.sp, color = Color.White
                    )
                }
            }

            // Box-a jartzeko pantailaren behekaldean
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.End // Arrangement.End erabiliz, pantailaren behean jartzen dugu Row guztia
                ) {
                    Button(
                        onClick = { onNavigateToEzarpenak() }, // onNavigateToEzarpenak() funtzioa erabiltzen dugu MainActivity-tik "Ezarpenak" pantailara joateko.
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp)
                    ) {
                        // Botoiaren ikonoa
                        Icon(
                            painter = painterResource(R.drawable.ic_arrowback),
                            contentDescription = "Itzuli",
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatuakPreview() {
    EuskanpTheme {
        Datuak(
            onNavigateToEzarpenak = {}
        )
    }
}