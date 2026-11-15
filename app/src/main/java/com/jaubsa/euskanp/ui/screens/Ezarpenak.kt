package com.jaubsa.euskanp.ui.screens

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import com.jaubsa.euskanp.ui.utils.updateLocale

@Composable
fun Ezarpenak(
    onNavigateToProfila: () -> Unit, // "Atzera" botoiaren zeregina
    onNavigateToDatuak: () -> Unit // "Nire datuak" botoiaren zeregina
){
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
                    TextButton(
                        onClick = {
                            updateLocale(context, "en") // Castellano
                            showLanguageDialog = false
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("English", fontSize = 18.sp)
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

    // Pantaila osoa
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Fondoaren irudia
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = "fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize() // fillMaxSize() erabiltzen dugu pantaila osoan fondoa izateko
        )

        // Pantaila
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(all = 15.dp)
        ){
            // Header lerroa
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Euskanp logoa
                Image(
                    painter = painterResource(id = R.drawable.logoa),
                    contentDescription = "Euskanp JAUB logoa",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp)
                )
            }

            // Botoiak jartzeko Column-a
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top=30.dp),
            ){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.ezarpenak),
                        style = TextStyle(fontSize = 23.sp, fontWeight = FontWeight(800))
                    )
                }

                // "Nire datuak" botoiaren ilara
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    // "Nire datuak" botoia
                    Button(
                        onClick = { onNavigateToDatuak() },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        )
                    ){
                        Text(
                            text = stringResource(R.string.nire_datuak)
                        )
                    }
                }

                // "Hizkuntzak" botoiaren ilara
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    // "Hizkuntzak" botoia
                    Button(
                        onClick = { showLanguageDialog = true },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        )
                    ){
                        Text(
                            text = stringResource(R.string.hizkuntzak)
                        )
                    }
                }

                // "Jakinarazpenak" botoiaren ilara
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ){
                    // "Jakinarazpenak" botoia
                    Button(
                        onClick = {  },
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2D6A4F)
                        )
                    ){
                        Text(
                            text = stringResource(R.string.jakinarazpenak)
                        )
                    }
                }
            }

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
                        onClick = { onNavigateToProfila() }, // onNavigateToProfila() funtzioa erabiltzen dugu MainActivity-tik "Profila" pantailara joateko.
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
fun EzarpenakPreview() {
    EuskanpTheme {
        Ezarpenak(
            onNavigateToProfila = {},
            onNavigateToDatuak = {}
        )
    }
}