package com.jaubsa.euskanp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaubsa.euskanp.R
import com.jaubsa.euskanp.ui.utils.AgendaManager
import com.jaubsa.euskanp.ui.utils.KulturEvent

@Composable
fun Ekitaldiak(
    userEmail: String,
    onNavigateToProfila: () -> Unit
) {
    val context = LocalContext.current
    val agendaManager = remember { AgendaManager(context) }

    var savedEvents by remember { mutableStateOf<List<KulturEvent>>(emptyList()) }
    var selectedDetailEvent by remember { mutableStateOf<KulturEvent?>(null) }

    // Egoera ezabatzeko berrespen-elkarrizketarako (AlertDialog)
    var eventPendingDeletion by remember { mutableStateOf<KulturEvent?>(null) }

    // Erabiltzailea aldatzen den bakoitzean gordetako ekitaldiak kargatzen ditu
    LaunchedEffect(userEmail) {
        savedEvents = agendaManager.getSavedEventsForUser(context, userEmail)
    }

    // Ekitaldi bat sakatzerakoan, xehetasun-pantaila irekitzen da
    if (selectedDetailEvent != null) {
        EkitaldiXehetasunaScreen(
            event = selectedDetailEvent!!,
            onBack = {
                selectedDetailEvent = null
                savedEvents = agendaManager.getSavedEventsForUser(context, userEmail)
            },
            onAddToAgenda = { event ->
                agendaManager.addEventToAgenda(userEmail, event.id)
            }
        )
        return
    }

    // Ezabatzeko berrespen AlertDialog-a
    eventPendingDeletion?.let { eventToDelete ->
        AlertDialog(
            onDismissRequest = { eventPendingDeletion = null },
            title = {
                Text(
                    text = stringResource(R.string.ezabatu_ekitaldia_titulua),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
            },
            text = {
                Text(
                    text = stringResource(
                        R.string.ezabatu_ekitaldia_mezua,
                        eventToDelete.documentName
                    )
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        agendaManager.removeEventFromAgenda(userEmail, eventToDelete.id)
                        savedEvents = agendaManager.getSavedEventsForUser(context, userEmail)
                        eventPendingDeletion = null
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D6A4F)
                    )
                ) {
                    Text(
                        text = stringResource(R.string.bai_ezabatu),
                        color = Color.White
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { eventPendingDeletion = null }
                ) {
                    Text(
                        text = stringResource(R.string.ezeztatu),
                        color = Color(0xFF1B4332)
                    )
                }
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
            // Goiko logoa
            Image(
                painter = painterResource(id = R.drawable.logoa),
                contentDescription = stringResource(R.string.app_name),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.size(65.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Pantailaren titulua
            Text(
                text = stringResource(R.string.ekitaldiak).uppercase(),
                color = Color(0xFF1B4332),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.White,
                        offset = Offset(2f, 2f),
                        blurRadius = 3f
                    )
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Edukia: hutsik badago mezua, bestela LazyColumn
            if (savedEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.gordetako_ekitaldirik_ez),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.88f), RoundedCornerShape(16.dp))
                            .padding(horizontal = 20.dp, vertical = 16.dp)
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    contentPadding = PaddingValues(bottom = 16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    itemsIndexed(savedEvents, key = { _, event -> event.id }) { index, event ->
                        SavedEkitaldiCard(
                            position = index + 1,
                            event = event,
                            onClick = { selectedDetailEvent = event },
                            onDeleteClick = { eventPendingDeletion = event }
                        )
                    }
                }
            }

            // Atzera botoia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onNavigateToProfila() },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2D6A4F)
                    ),
                    modifier = Modifier
                        .width(100.dp)
                        .height(60.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_arrowback),
                        contentDescription = stringResource(R.string.atzera_desc),
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SavedEkitaldiCard(
    position: Int,
    event: KulturEvent,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val ordinalLabel = getOrdinalEventLabel(position).uppercase()

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color(0xFF317863),
        border = BorderStroke(1.5.dp, Color(0xFF1B4332)),
        shadowElevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF245C4B),
                    shadowElevation = 4.dp,
                    modifier = Modifier.weight(1f, fill = false)
                ) {
                    Text(
                        text = ordinalLabel,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFF245C4B),
                        shadowElevation = 4.dp
                    ) {
                        Text(
                            text = event.formattedShortDate.ifBlank { "--/--/--" },
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = stringResource(R.string.ezabatu_desc),
                            tint = Color.White
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(165.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color(0xFFD8F3DC), Color(0xFF95D5B2))
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
                        color = Color(0xFF1B4332).copy(alpha = 0.85f)
                    ) {
                        Text(
                            text = "${event.eventTownName} • ${event.eventType}",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Text(
                        text = event.documentName,
                        color = Color(0xFF081C15),
                        fontSize = 19.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )

                    Text(
                        text = listOf(event.eventWhere, event.eventTimeTable)
                            .filter { it.isNotBlank() }
                            .joinToString(" • "),
                        color = Color(0xFF1B4332),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}