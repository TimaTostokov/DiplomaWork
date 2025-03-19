package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatisticsScreen(
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
        Log.e("StatisticsScreen", "Ошибка: $errorMsg")
    }

    var yearQuery by remember { mutableStateOf("") }

    val lazyListState = rememberLazyListState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back_button_english),
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
                text = "Статистика по материалам",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = yearQuery,
                onValueChange = { yearQuery = it },
                label = { Text("Поиск по году (дате)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                modifier = Modifier.weight(1f),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_go),
                        contentDescription = "Поиск"
                    )
                }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (yearQuery.isNotBlank()) {
                        viewModel.handleIntent(RequestIntent.LoadRequestsByYear(yearQuery))
                    } else {
                        viewModel.handleIntent(RequestIntent.LoadRequestsSortedByDateDesc)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(text = "", color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            stickyHeader {
                Text(
                    text = "таблица с материалами и адресами",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.LightGray)
                        .padding(8.dp)
                )
            }
            items(allRequests) { request ->
                StatsRequestRow(request)
            }
        }
    }
}

@Composable
fun StatsRequestRow(request: RequestItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text("ID: ${request.id}")
        Text("Дата: ${request.dateOfRegistration}")
        Text("Адрес: ${request.address}")
        Text("Статус: ${request.status}")

        if (request.materials.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text("Материалы:")
            request.materials.forEach { material ->
                Text("• $material")
            }
        }

        Spacer(modifier = Modifier.height(4.dp))
        Divider()
    }
}
