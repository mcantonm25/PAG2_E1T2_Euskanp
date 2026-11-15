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

@Composable
fun Jakinarazpenak(
    onNavigateToProfila: () -> Unit
){
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
            }

            // "Jakinarazpenak" Row nagusia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = stringResource(R.string.jakinarazpenak),
                    textAlign = TextAlign.Center,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                // Jakinarazpenak [left]
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
                        onClick = { onNavigateToProfila() },
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
fun JakinarazpenakPreview() {
    EuskanpTheme {
        Jakinarazpenak(
            onNavigateToProfila = {}
        )
    }
}