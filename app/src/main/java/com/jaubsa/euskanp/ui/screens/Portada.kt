package com.jaubsa.euskanp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.jaubsa.euskanp.R
import com.jaubsa.euskanp.ui.theme.EuskanpTheme

@Composable
fun Portada(onNavigateToLogin: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onNavigateToLogin() }
    ) {
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = "fondoa",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Image(
            painter = painterResource(id = R.drawable.logoa),
            contentDescription = "logoa",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(248.dp)
                .align(Alignment.Center)
        )

        Button(
            onClick = onNavigateToLogin,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp)
        ) {
            Text(text = stringResource(R.string.saioa_hasi))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PortadaPreview() {
    EuskanpTheme {
        Portada { null }
    }
}