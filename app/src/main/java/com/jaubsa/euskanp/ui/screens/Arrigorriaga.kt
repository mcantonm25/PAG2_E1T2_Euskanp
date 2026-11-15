package com.jaubsa.euskanp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import com.jaubsa.euskanp.ui.utils.KulturEvent
import com.jaubsa.euskanp.ui.utils.extractUniqueEventTypes
import com.jaubsa.euskanp.ui.utils.loadKulturEventsFromAssets
import java.text.Normalizer

@Composable
fun Arrigorriaga(
    onNavigateToProfila: () -> Unit
) {
    val context = LocalContext.current
    val allEvents = remember { loadKulturEventsFromAssets(context) }

    // JSON "eventType" balio guztiak gehitzen dira errepikatu gabe.
    val uniqueEventTypes = remember(allEvents) {
        extractUniqueEventTypes(allEvents)
    }

    // List pantaila kategoriarentzako
    var listScreenTitle by remember { mutableStateOf<String?>(null) }
    var listScreenEvents by remember { mutableStateOf<List<KulturEvent>>(emptyList()) }

    // Hautatutako ekitaldiaren xehetasun-pantaila osorako egoera.
    var selectedDetailEvent by remember { mutableStateOf<KulturEvent?>(null) }

    // Ekitaldi bat sakatzerakoan, informazio guztia agertuko da EkitaldiXehetasunaScreen.kt pantailan
    if (selectedDetailEvent != null) {
        EkitaldiXehetasunaScreen(
            event = selectedDetailEvent!!,
            onBack = {
                selectedDetailEvent = null
            }
        )
        return
    }

    // Ekitaldi bat hautatuta badago edo bilaketa egin bada, EkitaldiZerrendaScreen.kt pantailan erakusten da:
    if (listScreenTitle != null) {
        EkitaldiZerrendaScreen(
            title = listScreenTitle!!,
            events = listScreenEvents,
            onEventClick = { clickedEvent ->
                selectedDetailEvent = clickedEvent
            },
            onBack = {
                listScreenTitle = null
            }
        )
        return
    }

    var searchQuery by remember { mutableStateOf("") }
    var kategoriaExpanded by remember { mutableStateOf(false) }
    var ekitaldiaExpanded by remember { mutableStateOf(false) }

    val searchResultsTitle = stringResource(R.string.bilaketaren_emaitzak)

    // Bilaketaren balioa, "documentName" irakurtzen du JSONetik eta "Bilaketaren emaitzak" pantailan zabaltzen du.
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

    // Pantaila nagusia:
    Box(modifier = Modifier.fillMaxSize()) {
        // Pantailaren fondoa
        Image(
            painter = painterResource(id = R.drawable.portada),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Pantailaren Column nagusia:
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            // Goiko row-a
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Euskanp logoa
                Image(
                    painter = painterResource(id = R.drawable.logoa),
                    contentDescription = stringResource(R.string.app_name),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.size(65.dp)
                )

                // Bilaketa TextField-a:
                OutlinedTextField(
                    value = searchQuery, // Bilaketaren balioa definitzen diogu bilatu ahal izateko
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = stringResource(R.string.bilatu_ekitaldia), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                            fontSize = 14.sp
                        )
                    },
                    singleLine = true, // Linea bakarra ezartzen dugu
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

            // Pantailaren titulu nagusia
            Text(
                text = stringResource(R.string.arrigorriaga_ak), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            )

            // "Kategoriak" eta "Ekitaldiak" dropdown-ak izango duen Column-a
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // "Kategoriak" dropdown-a
                Box(modifier = Modifier.fillMaxWidth()) {
                    DropdownSelectorButton(
                        text = stringResource(R.string.kategoriak), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                        onClick = { kategoriaExpanded = !kategoriaExpanded }
                    )
                    DropdownMenu(
                        expanded = kategoriaExpanded,
                        onDismissRequest = { kategoriaExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 320.dp)
                    ) {
                        if (uniqueEventTypes.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.ekitaldirik_ez)) }, // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                onClick = { kategoriaExpanded = false }
                            )
                        } else {
                            uniqueEventTypes.forEach { eventTypeItem ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = eventTypeItem,
                                            fontWeight = FontWeight.SemiBold
                                        )
                                    },
                                    onClick = {
                                        kategoriaExpanded = false
                                        listScreenTitle = eventTypeItem
                                        listScreenEvents = allEvents.filter {
                                            it.eventType.equals(eventTypeItem, ignoreCase = true)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                // "Ekitaldiak" dropdown-a
                Box(modifier = Modifier.fillMaxWidth()) {
                    DropdownSelectorButton(
                        text = stringResource(R.string.ekitaldiak), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                        onClick = { ekitaldiaExpanded = !ekitaldiaExpanded }
                    )
                    DropdownMenu(
                        expanded = ekitaldiaExpanded,
                        onDismissRequest = { ekitaldiaExpanded = false },
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .heightIn(max = 320.dp)
                    ) {
                        if (allEvents.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text(stringResource(R.string.ekitaldirik_ez)) }, // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                                onClick = { ekitaldiaExpanded = false }
                            )
                        } else {
                            allEvents.forEachIndexed { index, event ->
                                val ordinalName = getOrdinalEventLabel(index + 1)
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(ordinalName, fontWeight = FontWeight.Bold)
                                            Text(
                                                text = event.documentName,
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    },
                                    onClick = {
                                        ekitaldiaExpanded = false
                                        selectedDetailEvent = event
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // "Atzera" botoia
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = { onNavigateToProfila() }, // onNavigateToProfila() MainActivity-tik definitzen dugu "Profila.kt" pantailara joateko.
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
                        contentDescription = stringResource(R.string.atzera_desc), // stringResource erabiltzen dugu itzultzeko beste hizkuntzetara
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }
            }
        }
    }
}

// Dropdown-aren aukerak botoi bezala funtzionatzen den private funtzioa.
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

// Ekitaldiaren zenbakia ezartzeko funtzioa bilaketaren arabera:
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

// Pantaila honen Preview-a
@Preview(showBackground = true)
@Composable
fun ArrigorriagaPreview() {
    EuskanpTheme {
        Arrigorriaga(
            onNavigateToProfila = {}
        )
    }
}