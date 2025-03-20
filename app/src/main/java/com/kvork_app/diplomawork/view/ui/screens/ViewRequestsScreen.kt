package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ViewRequestsScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val isInPreview = LocalInspectionMode.current

    LaunchedEffect(Unit) {
        if (!isInPreview) {
            viewModel.handleIntent(RequestIntent.LoadAllRequests)
        }
    }

    val uiState by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    uiState.errorMessage?.let { errorMsg ->
        Log.e("ViewRequestsScreen", "Ошибка: $errorMsg")
    }

    val allRequests = uiState.requests

    var searchQuery by remember { mutableStateOf("") }

    val statusList = listOf("Все", "Зарегистрирована", "В работе", "Выполнена", "Снята")
    var expandedSort by remember { mutableStateOf(false) }
    var selectedSortStatus by remember { mutableStateOf("Все") }

    val filteredRequests = allRequests.filter { request ->
        val statusMatch =
            if (selectedSortStatus == "Все") true
            else request.status.equals(selectedSortStatus, ignoreCase = true)

        val searchMatch =
            request.description.contains(searchQuery, ignoreCase = true) ||
                    request.id.contains(searchQuery, ignoreCase = true)

        statusMatch && searchMatch
    }

    LazyColumn(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_back),
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Image(
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(R.string.logo_description),
                    modifier = Modifier.size(48.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = stringResource(R.string.app_title),
                        fontSize = 24.sp,
                        color = Color(0xFF00AA00)
                    )
                    Text(
                        text = stringResource(R.string.app_subtitle),
                        fontSize = 14.sp,
                        color = Color.Black
                    )
                }
            }

            Text(
                text = "Просмотр заявок",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    label = { Text("Поиск по описанию или ID") },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search_go),
                            contentDescription = "Поиск"
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box {
                    Button(
                        onClick = {
                            focusManager.clearFocus()
                            expandedSort = !expandedSort
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00))
                    ) {
                        Text(
                            text = "Сортировка",
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    DropdownMenu(
                        expanded = expandedSort,
                        onDismissRequest = { expandedSort = false }
                    ) {
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
        }

        stickyHeader {
            Text(
                text = "таблица со всеми данными заявок",
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(16.dp)
            )
        }

        items(filteredRequests) { request ->
            RequestRow(request)
        }
    }
}

@Composable
fun RequestRow(request: RequestItem) {
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

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp", apiLevel = 34)
@Composable
fun ViewRequestsScreenPreview() {
    ViewRequestsScreen(onBackClick = {})
}
