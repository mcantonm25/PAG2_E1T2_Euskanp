package com.jaubsa.euskanp.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import com.jaubsa.euskanp.ui.theme.EuskanpTheme
import com.jaubsa.euskanp.R
import androidx.compose.ui.res.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import com.jaubsa.euskanp.ui.utils.BezeroaEmail
import com.jaubsa.euskanp.ui.utils.updateLocale

@SuppressLint("LocalContextGetResourceValueCall")
@Composable
fun Profila(
    userEmail: String = "",
    onNavigateToEzarpenak: () -> Unit = {},
    onLogout: () -> Unit = {},
    onNavigateToArrigorriaga: () -> Unit = {},
    onNavigateToEkitaldiak: () -> Unit = {}
){
    val context = LocalContext.current
    var showLanguageDialog by remember { mutableStateOf(false) }

    var erabiltzailea by remember { mutableStateOf("Kargatzen...") }

    LaunchedEffect(userEmail) {
        val cleanEmail = userEmail.trim()
        if (cleanEmail.isNotEmpty()) {
            val bezeroa = BezeroaEmail(context, cleanEmail)
            bezeroa?.let {
                val izenaAbizena = "${it.izena} ${it.abizena}"
                erabiltzailea = if (izenaAbizena.isNotEmpty()) izenaAbizena else cleanEmail
            } ?: run {
                erabiltzailea = "Erabiltzaile ezezaguna"
            }
        } else {
            erabiltzailea = context.getString(R.string.saiorik_ez)
        }
    }

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
                            updateLocale(context, "es") // Español
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

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = "fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ){
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoa),
                    contentDescription = "Euskanp JAUB logoa",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp)
                )

                Button(
                    onClick = { onNavigateToEzarpenak() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D6A4F)
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(60.dp)
                ){
                    Icon(
                        painter = painterResource(R.drawable.settings),
                        contentDescription = null,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Profilaren kutxa
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .shadow(
                            elevation = 52.dp,
                            shape = RoundedCornerShape(30.dp)
                        )
                        .background(
                            color = Color(0xFF2D6A4F),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .clip(RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        // "Saioa" box-a
                        Box(
                            modifier = Modifier
                                .padding(top = 12.dp)
                                .fillMaxWidth(0.8f)
                                .height(35.dp)
                                .background(Color(0xFF1B4332), shape = RoundedCornerShape(10.dp)),
                            contentAlignment = Alignment.Center
                        ){
                            Text(
                                text = erabiltzailea,
                                color = Color.White
                            )
                        }

                        // Erabiltzailearen irudia
                        Image(
                            painter = painterResource(R.drawable.ic_profile),
                            contentDescription = null,
                            modifier = Modifier
                                .width(120.dp)
                                .height(130.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            Button(
                onClick = { onNavigateToEkitaldiak() },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D6A4F)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(260.dp)
                    .height(40.dp)
            ){
                Text(
                    text = stringResource(R.string.ekitaldiak)
                )
            }

            Spacer(modifier = Modifier.height(25.dp))

            Button(
                onClick = { onNavigateToArrigorriaga() },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2D6A4F)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(260.dp)
                    .height(40.dp)
            ){
                Text(
                    text=stringResource(R.string.arrigorriaga_ak)
                )
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Box-a jartzeko pantailaren behekaldean
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.End, // Arrangement.End erabiliz, pantailaren behean jartzen dugu Row guztia
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Button(
                        onClick = { onLogout() }, // onLogout() funtzioa erabiltzen dugu MainActivity-tik saioa ixteko.
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        ),
                        modifier = Modifier
                            .width(100.dp)
                            .height(60.dp)
                    ){
                        // Botoiaren ikonoa
                        Icon(
                            painter = painterResource(R.drawable.ic_logout),
                            contentDescription = null,
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
fun ProfilaPreview() {
    EuskanpTheme {
        Profila()
    }
}