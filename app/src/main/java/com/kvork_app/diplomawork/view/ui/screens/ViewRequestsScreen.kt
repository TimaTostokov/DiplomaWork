package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

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

    uiState.errorMessage?.let { errorMsg ->
        Log.e("ViewRequestsScreen", "Ошибка: $errorMsg")
    }

    val allRequests = uiState.requests

    var searchQuery by remember { mutableStateOf("") }

    val statusList = listOf("Все", "зарегана", "в работе", "выполнено", "снята")
    var expandedSort by remember { mutableStateOf(false) }
    var selectedSortStatus by remember { mutableStateOf("Все") }

    val filteredRequests = allRequests.filter { request ->
        val statusMatch = if (selectedSortStatus == "Все") true
        else request.status.equals(selectedSortStatus, ignoreCase = true)
        val searchMatch = request.description.contains(searchQuery, ignoreCase = true)
                || request.id.contains(searchQuery, ignoreCase = true)
        statusMatch && searchMatch
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
            headerRowRef,
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
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_button)
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = stringResource(R.string.logo_description),
            modifier = Modifier.constrainAs(logoRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(backBtnRef.end, margin = 8.dp)
            }
        )

        Text(
            text = stringResource(R.string.app_title),
            fontSize = 28.sp,
            color = Color(0xFF00AA00),
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(logoRef.end, margin = 12.dp)
            }
        )

        Text(
            text = stringResource(R.string.app_subtitle),
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(subtitleRef) {
                top.linkTo(titleRef.bottom, margin = 4.dp)
                start.linkTo(titleRef.start)
            }
        )

        Text(
            text = "Просмотр заявок",
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.constrainAs(screenTitleRef) {
                top.linkTo(logoRef.bottom, margin = 32.dp)
                centerHorizontallyTo(parent)
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(headerRowRef) {
                    top.linkTo(screenTitleRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                singleLine = true,
                placeholder = { Text("Поиск по описанию или id") },
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

        LazyColumn(
            modifier = Modifier
                .constrainAs(listRef) {
                    top.linkTo(headerRowRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        ) {
            items(filteredRequests) { request ->
                RequestRow(request)
            }
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

@Preview( showBackground = true, device = "spec:width=411dp,height=891dp", apiLevel = 34)
@Preview( showBackground = true, device = "spec:width=1280dp,height=800dp", apiLevel = 34)
@Composable
fun ViewRequestsScreenPreview() {
    ViewRequestsScreen(onBackClick = {})
}
