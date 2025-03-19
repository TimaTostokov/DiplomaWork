package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (
            backBtnRef,
            logoRef,
            titleRef,
            subtitleRef,
            screenTitleRef,
            searchRowRef,
            tableNoteRef,
            listRef
        ) = createRefs()

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(backBtnRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start, margin = 16.dp)
            }
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back),
                contentDescription = stringResource(id = R.string.back_button_english),
                tint = Color.Black
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(id = R.string.logo_description),
            modifier = Modifier
                .size(64.dp)
                .constrainAs(logoRef) {
                    top.linkTo(parent.top)
                    start.linkTo(backBtnRef.end, margin = 8.dp)
                }
        )

        Text(
            text = stringResource(id = R.string.app_title),
            fontSize = 28.sp,
            color = Color(0xFF00AA00),
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 8.dp)
                start.linkTo(logoRef.end, margin = 12.dp)
            }
        )

        Text(
            text = stringResource(id = R.string.app_subtitle),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(subtitleRef) {
                top.linkTo(titleRef.bottom, margin = 4.dp)
                start.linkTo(titleRef.start)
            }
        )

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
                .constrainAs(searchRowRef) {
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

            IconButton(
                onClick = {
                    focusManager.clearFocus()
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back),
                    contentDescription = "Иконка поиска"
                )
            }
        }

        Text(
            text = "таблица со всеми данными по заявкам и адресам",
            fontSize = 14.sp,
            modifier = Modifier.constrainAs(tableNoteRef) {
                top.linkTo(searchRowRef.bottom, margin = 16.dp)
                start.linkTo(parent.start)
            }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(listRef) {
                    top.linkTo(tableNoteRef.bottom, margin = 16.dp)
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