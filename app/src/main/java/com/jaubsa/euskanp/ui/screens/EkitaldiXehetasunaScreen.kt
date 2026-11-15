package com.jaubsa.euskanp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaubsa.euskanp.R
import com.jaubsa.euskanp.ui.utils.KulturEvent

@Composable
fun EkitaldiXehetasunaScreen(
    event: KulturEvent,
    onBack: () -> Unit,
    onAddToAgenda: (KulturEvent) -> Unit = {}
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val addedMessage = stringResource(R.string.agendara_gehitua) // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondoa
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            // Euskanp logoa
            Image(
                painter = painterResource(id = R.drawable.logoa),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Txartel nagusia
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFC5F0A4),
                border = BorderStroke(2.dp, Color(0xFF1B4332)),
                shadowElevation = 8.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Header-a
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFF95D5B2)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(38.dp)
                                        .background(Color(0xFFD8F3DC), RoundedCornerShape(8.dp))
                                        .border(1.5.dp, Color(0xFF1B4332), RoundedCornerShape(8.dp))
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.logoa),
                                        contentDescription = null,
                                        tint = Color.Unspecified,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = stringResource(R.string.kanpina_izenburua), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = Color(0xFF081C15),
                                        lineHeight = 14.sp
                                    )
                                    Text(
                                        text = stringResource(R.string.kanpina_azpititulua), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 12.sp,
                                        color = Color(0xFF1B4332),
                                        lineHeight = 13.sp
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = stringResource(R.string.instituzioa_goi), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Color(0xFF081C15)
                                )
                                Text(
                                    text = stringResource(R.string.instituzioa_behe), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 10.sp,
                                    color = Color(0xFF2D6A4F)
                                )
                            }
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFF95D5B2)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(175.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(
                                        Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xFFB7E4C7),
                                                Color(0xFF74C69D)
                                            )
                                        )
                                    )
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.portada),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    alpha = 0.35f,
                                    modifier = Modifier.fillMaxSize()
                                )

                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = Color(0xFF2D6A4F).copy(alpha = 0.9f)
                                    ) {
                                        Text(
                                            text = "${event.eventType.uppercase()} • ${event.eventTownName}",
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                        )
                                    }

                                    Text(
                                        text = event.documentName,
                                        color = Color(0xFF081C15),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        textAlign = TextAlign.Center,
                                        maxLines = 4,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.padding(horizontal = 6.dp)
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }

                            // Data eta ordua
                            val dateAndTimeText = buildString {
                                append(event.formattedShortDate.ifBlank { event.eventStartDate })
                                if (event.eventEndDate.isNotBlank() && event.eventEndDate != event.eventStartDate) {
                                    append(" → ${event.eventEndDate}")
                                }
                                if (event.eventTimeTable.isNotBlank()) {
                                    append(" - ${event.eventTimeTable}")
                                }
                            }
                            DetailInfoCard(
                                title = stringResource(R.string.data_eta_ordua_label), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                content = dateAndTimeText.ifBlank { stringResource(R.string.zehaztu_gabe) } // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                            )

                            // Lekua
                            val locationText = listOf(event.eventWhere, event.eventTownName, event.address)
                                .filter { it.isNotBlank() }
                                .distinct()
                                .joinToString(" • ")
                            DetailInfoCard(
                                title = stringResource(R.string.tokia_label), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                content = locationText.ifBlank { stringResource(R.string.zehaztu_gabe) } // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                            )

                            // Deialdiaren deskribapena
                            DetailInfoCard(
                                title = stringResource(R.string.deskribapena_label), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                content = event.documentDescription.ifBlank {
                                    stringResource(R.string.deskribapenik_ez) // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                }
                            )

                            // Deialdiaren informazioa (Prezioa, Hizkuntza, Konpainia, Mota...)
                            val extraDetails = buildList {
                                add("${stringResource(R.string.etiketa_mota)} ${event.eventType}") // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                if (event.eventPrice.isNotBlank()) {
                                    add("${stringResource(R.string.etiketa_prezioa)} ${event.eventPrice}") // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                }
                                if (event.eventLanguages.isNotBlank()) {
                                    add("${stringResource(R.string.etiketa_hizkuntza)} ${event.eventLanguages}") // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                }
                                if (event.company.isNotBlank()) {
                                    add("Konpainia: ${event.company}")
                                }
                            }.joinToString("\n")

                            DetailInfoCard(
                                title = stringResource(R.string.informazio_gehiago_label), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                content = extraDetails
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // "Gehitu nire agenda" botoiaren zeregina
                            Button(
                                onClick = {
                                    onAddToAgenda(event)
                                    Toast.makeText(context, addedMessage, Toast.LENGTH_SHORT).show()
                                },
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.5.dp, Color(0xFF081C15)),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF2D6A4F)
                                ),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                                modifier = Modifier
                                    .fillMaxWidth(0.82f)
                                    .height(42.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.gehitu_agendara), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }
                    }
                }
            }

            // Aurreko pantailara itzultzeko botoia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Surface(
                    onClick = onBack,
                    shape = RoundedCornerShape(24.dp),
                    color = Color(0xFF3E7B67).copy(alpha = 0.85f),
                    shadowElevation = 6.dp,
                    modifier = Modifier
                        .width(105.dp)
                        .height(58.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrowback),
                            contentDescription = stringResource(R.string.atzera_desc),
                            tint = Color(0xFF95D5B2),
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }
            }
        }
    }
}

// Card bakoitzaren itxura
@Composable
private fun DetailInfoCard(
    title: String,
    content: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8FBF8),
        border = BorderStroke(1.dp, Color(0xFFD8E2DC)),
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            /*Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(34.dp)
                    .background(Color(0xFFD8F3DC), RoundedCornerShape(8.dp))
                    .border(1.dp, Color(0xFF2D6A4F), RoundedCornerShape(8.dp))
            ) {
                Text(text = "", fontSize = 16.sp)
            }*/

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    color = Color(0xFF1B4332)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = content,
                    fontWeight = FontWeight.Medium,
                    fontSize = 13.sp,
                    color = Color(0xFF081C15),
                    lineHeight = 17.sp
                )
            }
        }
    }
}