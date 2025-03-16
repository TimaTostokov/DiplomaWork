package com.kvork_app.diplomawork.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.model.RequestItem

@Composable
fun ViewRequestsScreen(
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    val sampleRequests = listOf(
        RequestItem("1", "Замена розетки", "зарегана"),
        RequestItem("2", "Починка крана", "в работе"),
        RequestItem("3", "Укладка плитки", "выполнено"),
        RequestItem("4", "Монтаж потолка", "снята"),
        RequestItem("5", "Обслуживание котла", "зарегана"),
        RequestItem("6", "Установка дверей", "в работе"),
        RequestItem("7", "Замена замка", "снята")
    )

    var searchQuery by remember { mutableStateOf("") }

    val statusList = listOf("зарегана", "в работе", "выполнено", "снята")
    var expandedSort by remember { mutableStateOf(false) }
    var selectedSortStatus by remember { mutableStateOf("Все") }

    val filteredRequests = sampleRequests.filter { request ->
        val matchesStatus = (selectedSortStatus == "Все" || request.status == selectedSortStatus)
        val matchesSearch = request.description.contains(searchQuery, ignoreCase = true)
        matchesStatus && matchesSearch
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

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun ViewRequestsScreenPreview() {
    ViewRequestsScreen(onBackClick = {})
}