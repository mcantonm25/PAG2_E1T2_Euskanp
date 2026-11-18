package com.jaubsa.euskanp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jaubsa.euskanp.R
import com.jaubsa.euskanp.ui.theme.EuskanpTheme
import com.jaubsa.euskanp.ui.utils.AgendaManager
import com.jaubsa.euskanp.ui.utils.KulturEvent
import com.jaubsa.euskanp.ui.utils.SessionManager
import com.jaubsa.euskanp.ui.utils.extractUniqueEventTypes
import com.jaubsa.euskanp.ui.utils.loadKulturEventsFromAssets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.Normalizer

@Composable
fun Arrigorriaga(
    onNavigateToProfila: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val agendaManager = remember { AgendaManager(context) }
    val currentUserEmail = remember { sessionManager.getUserSession() ?: "" }

    // Egoera asinkronoa JSON datuak UI haria blokeatu gabe kargatzeko
    var allEvents by remember { mutableStateOf<List<KulturEvent>>(emptyList()) }
    var uniqueEventTypes by remember { mutableStateOf<List<String>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        val loaded = withContext(Dispatchers.IO) {
            loadKulturEventsFromAssets(context)
        }
        val types = withContext(Dispatchers.Default) {
            extractUniqueEventTypes(loaded)
        }
        allEvents = loaded
        uniqueEventTypes = types
        isLoading = false
    }

    var listScreenTitle by remember { mutableStateOf<String?>(null) }
    var listScreenEvents by remember { mutableStateOf<List<KulturEvent>>(emptyList()) }
    var selectedDetailEvent by remember { mutableStateOf<KulturEvent?>(null) }

    if (selectedDetailEvent != null) {
        EkitaldiXehetasunaScreen(
            event = selectedDetailEvent!!,
            onBack = { selectedDetailEvent = null },
            onAddToAgenda = { eventToSave ->
                agendaManager.addEventToAgenda(currentUserEmail, eventToSave.id)
            }
        )
        return
    }

    if (listScreenTitle != null) {
        EkitaldiZerrendaScreen(
            title = listScreenTitle!!,
            events = listScreenEvents,
            onEventClick = { clickedEvent -> selectedDetailEvent = clickedEvent },
            onBack = { listScreenTitle = null }
        )
        return
    }

    var searchQuery by remember { mutableStateOf("") }
    var kategoriaExpanded by remember { mutableStateOf(false) }
    var ekitaldiaExpanded by remember { mutableStateOf(false) }

    val searchResultsTitle = stringResource(R.string.bilaketaren_emaitzak)

    val executeSearchByDocumentName = {
        val queryNormalized = searchQuery.trim().normalizeForSearch()
        val filteredEvents = if (queryNormalized.isEmpty()) {
            allEvents
        } else {
            allEvents.filter {
                it.documentName.normalizeForSearch().contains(queryNormalized)
            }
        }
        listScreenTitle = searchResultsTitle
        listScreenEvents = filteredEvents
    }

    // Kategoriak aukeratzeko elkarrizketa-koadroa (LazyColumn erabiliz crash-ak saihesteko)
    if (kategoriaExpanded) {
        AlertDialog(
            onDismissRequest = { kategoriaExpanded = false },
            title = {
                Text(
                    text = stringResource(R.string.kategoriak),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
            },
            text = {
                if (uniqueEventTypes.isEmpty()) {
                    Text(stringResource(R.string.ekitaldirik_ez))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {
                        items(uniqueEventTypes) { eventTypeItem ->
                            Text(
                                text = eventTypeItem,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        kategoriaExpanded = false
                                        listScreenTitle = eventTypeItem
                                        listScreenEvents = allEvents.filter {
                                            it.eventType.equals(eventTypeItem, ignoreCase = true)
                                        }
                                    }
                                    .padding(vertical = 12.dp, horizontal = 8.dp)
                            )
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { kategoriaExpanded = false }) {
                    Text(stringResource(R.string.ezeztatu), color = Color(0xFF2D6A4F))
                }
            }
        )
    }

    // Ekitaldiak aukeratzeko elkarrizketa-koadroa (LazyColumn erabiliz crash-ak saihesteko)
    if (ekitaldiaExpanded) {
        AlertDialog(
            onDismissRequest = { ekitaldiaExpanded = false },
            title = {
                Text(
                    text = stringResource(R.string.ekitaldiak),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B4332)
                )
            },
            text = {
                if (allEvents.isEmpty()) {
                    Text(stringResource(R.string.ekitaldirik_ez))
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 350.dp)
                    ) {
                        itemsIndexed(allEvents, key = { idx, ev -> "${ev.id}_$idx" }) { index, event ->
                            val ordinalName = getOrdinalEventLabel(index + 1)
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        ekitaldiaExpanded = false
                                        selectedDetailEvent = event
                                    }
                                    .padding(vertical = 10.dp, horizontal = 8.dp)
                            ) {
                                Text(ordinalName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    text = event.documentName,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { ekitaldiaExpanded = false }) {
                    Text(stringResource(R.string.ezeztatu), color = Color(0xFF2D6A4F))
                }
            }
        )
    }

    // Pantaila nagusia
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = "Euskanp portada",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logoa),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(65.dp)
                )

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.bilatu_ekitaldia),
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = { executeSearchByDocumentName() }
                    ),
                    trailingIcon = {
                        IconButton(onClick = { executeSearchByDocumentName() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_search),
                                contentDescription = stringResource(R.string.bilatu_desc),
                                tint = Color(0xFF2D6A4F)
                            )
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White.copy(alpha = 0.92f),
                        unfocusedContainerColor = Color.White.copy(alpha = 0.92f),
                        focusedBorderColor = Color(0xFF2D6A4F),
                        unfocusedBorderColor = Color.Gray
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                )
            }

            Text(
                text = stringResource(R.string.arrigorriaga_ak),
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2D6A4F))
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DropdownSelectorButton(
                        text = stringResource(R.string.kategoriak),
                        onClick = { kategoriaExpanded = true }
                    )

                    DropdownSelectorButton(
                        text = stringResource(R.string.ekitaldiak),
                        onClick = { ekitaldiaExpanded = true }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
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
private fun DropdownSelectorButton(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Color(0xFF1B4332).copy(alpha = 0.85f),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_arrowdown),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun getOrdinalEventLabel(number: Int): String {
    return when (number) {
        1 -> stringResource(R.string.ordinal_1)
        2 -> stringResource(R.string.ordinal_2)
        3 -> stringResource(R.string.ordinal_3)
        4 -> stringResource(R.string.ordinal_4)
        5 -> stringResource(R.string.ordinal_5)
        6 -> stringResource(R.string.ordinal_6)
        7 -> stringResource(R.string.ordinal_7)
        8 -> stringResource(R.string.ordinal_8)
        9 -> stringResource(R.string.ordinal_9)
        10 -> stringResource(R.string.ordinal_10)
        else -> stringResource(R.string.ordinal_generikoa, number)
    }
}

private fun String.normalizeForSearch(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
        .replace(Regex("\\p{InCombiningDiacriticalMarks}+"), "")
        .lowercase()
}

@Preview(showBackground = true)
@Composable
fun ArrigorriagaPreview() {
    EuskanpTheme {
        Arrigorriaga(
            onNavigateToProfila = {}
        )
    }
}