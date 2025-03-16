package com.kvork_app.diplomawork.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.model.RequestItem

@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val sampleRequests = List(30) { index ->
        RequestItem(
            id = (index + 1).toString(),
            description = "Описание заявки ${index + 1}",
            status = when (index % 4) {
                0 -> "зарегана"
                1 -> "в работе"
                2 -> "выполнено"
                else -> "снята"
            }
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    val statusList = listOf("зарегана", "в работе", "выполнено", "снята")
    var expandedSort by remember { mutableStateOf(false) }
    var selectedSortStatus by remember { mutableStateOf("Все") }

    val filteredRequests = sampleRequests.filter { request ->
        val matchesStatus = (selectedSortStatus == "Все" || request.status == selectedSortStatus)
        val matchesSearch = request.description.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = stringResource(R.string.app_title),
                        fontSize = 28.sp,
                        color = Color(0xFF00AA00),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.app_subtitle),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Cтатистика по материалам",
                    fontSize = 20.sp,
                    color = Color.Black
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    placeholder = { Text("Поиск") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Search
                    ),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.images),
                            contentDescription = "Search Icon"
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                )

                Box {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            expandedSort = !expandedSort
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
                        modifier = Modifier
                            .wrapContentWidth()
                            .height(56.dp)
                    ) {
                        Text(
                            text = "Сортировка по статусу",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onPrimary,
                            textAlign = TextAlign.Center
                        )
                    }
                    DropdownMenu(
                        expanded = expandedSort,
                        onDismissRequest = { expandedSort = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Все") },
                            onClick = {
                                selectedSortStatus = "Все"
                                expandedSort = false
                            }
                        )
                        statusList.forEach { status ->
                            DropdownMenuItem(
                                text = { Text(status) },
                                onClick = {
                                    selectedSortStatus = status
                                    expandedSort = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        items(filteredRequests) { request ->
            RequestRowStatistics(request)
        }
    }
}

@Composable
fun RequestRowStatistics(request: RequestItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "ID: ${request.id}",
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = request.description,
            fontSize = 14.sp,
            color = Color.Black
        )
        Text(
            text = request.status,
            fontSize = 14.sp,
            color = Color.Black
        )
    }
    Divider(color = Color.LightGray)
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(onBackClick = {})
}
