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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
fun AddressStatsScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.handleIntent(RequestIntent.LoadAllRequests)
    }

    val uiState by viewModel.state.collectAsState()
    val allRequests = uiState.requests

    uiState.errorMessage?.let { errorMsg ->
        Log.e("AddressStatsScreen", "Ошибка: $errorMsg")
    }

    var searchQuery by remember { mutableStateOf("") }
    val filtered = allRequests.filter {
        it.address.contains(searchQuery, ignoreCase = true)
    }

    // Используем LazyColumn с stickyHeader, чтобы заголовок таблицы всегда оставался наверху
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Верхняя часть: панель с кнопкой, логотипом и заголовками
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_english),
                    tint = Color.Black
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Image(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = stringResource(id = R.string.logo_description),
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = stringResource(id = R.string.app_title),
                    fontSize = 28.sp,
                    color = Color(0xFF00AA00)
                )
                Text(
                    text = stringResource(id = R.string.app_subtitle),
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "Адреса и заявки",
            fontSize = 20.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        // Поисковая строка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск по адресу") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { focusManager.clearFocus() }) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Иконка поиска"
                )
            }
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            stickyHeader {
                Text(
                    text = "таблица со всеми данными по заявкам и адресам",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(16.dp)
                )
            }
            items(filtered) { item ->
                AddressRow(item)
            }
        }
    }
}

@Composable
fun AddressRow(item: RequestItem) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "ID: ${item.id}")
        Text(text = "Адрес: ${item.address}")
        Text(text = "Описание: ${item.description}")
    }
}

@Preview(showBackground = true)
@Composable
fun AddressStatsScreenPreview() {
    AddressStatsScreen(onBackClick = {})
}
