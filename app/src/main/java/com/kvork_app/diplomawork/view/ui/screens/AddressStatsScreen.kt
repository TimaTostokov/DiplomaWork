package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.utils.RequestIntent
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

@Composable
fun AddressStatsScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    // При заходе на экран — загрузим все заявки
    LaunchedEffect(Unit) {
        viewModel.handleIntent(RequestIntent.LoadAllRequests)
    }
    val uiState by viewModel.state.collectAsState()
    val allRequests = uiState.requests

    uiState.errorMessage?.let { errorMsg ->
        Log.e("AddressStatsScreen", "Ошибка: $errorMsg")
    }

    var searchQuery by remember { mutableStateOf("") }

    // Фильтруем заявки по адресу (или по описанию адреса)
    val filtered = allRequests.filter {
        it.address.contains(searchQuery, ignoreCase = true)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (backBtnRef, screenTitleRef, searchRef, listRef) = createRefs()

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(backBtnRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_button)
            )
        }

        Text(
            text = "Адреса и заявки",
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(screenTitleRef) {
                top.linkTo(backBtnRef.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(searchRef) {
                    top.linkTo(screenTitleRef.bottom, margin = 16.dp)
                },
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
            // Можете добавить кнопку для сброса
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(listRef) {
                    top.linkTo(searchRef.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                }
        ) {
            items(filtered) { item ->
                AddressRow(item)
            }
        }
    }
}

@Composable
fun AddressRow(item: RequestItem) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = "ID: ${item.id}")
        Text(text = "Адрес: ${item.address}")
        Text(text = "Описание: ${item.description}")
    }
}
