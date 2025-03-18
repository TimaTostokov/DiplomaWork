package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberTopAppBarState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.utils.RequestIntent
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel

@Composable
fun StatisticsScreen(
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
        Log.e("StatisticsScreen", "Ошибка: $errorMsg")
    }

    // Здесь вы можете реализовать любую фильтрацию (материалы, адрес, год и т.д.)
    var yearQuery by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (backBtnRef, screenTitleRef, controlsRef, listRef) = createRefs()

        IconButton(
            onClick = onBackClick,
            modifier = Modifier.constrainAs(backBtnRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(parent.start)
            }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_button)
            )
        }

        Text(
            text = "Статистика",
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(screenTitleRef) {
                top.linkTo(backBtnRef.bottom, margin = 16.dp)
                centerHorizontallyTo(parent)
            }
        )

        Column(
            modifier = Modifier
                .constrainAs(controlsRef) {
                    top.linkTo(screenTitleRef.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row {
                Button(
                    onClick = {
                        // Загрузим в порядке убывания
                        viewModel.handleIntent(RequestIntent.LoadRequestsSortedByDateDesc)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00))
                ) {
                    Text("По дате DESC", color = Color.White)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        // В порядке возрастания
                        viewModel.handleIntent(RequestIntent.LoadRequestsSortedByDateAsc)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00))
                ) {
                    Text("По дате ASC", color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = yearQuery,
                    onValueChange = { yearQuery = it },
                    singleLine = true,
                    label = { Text("Поиск по году (yyyy)") },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        focusManager.clearFocus()
                        viewModel.handleIntent(RequestIntent.LoadRequestsByYear(yearQuery))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00))
                ) {
                    Text("Фильтр по году", color = Color.White)
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .constrainAs(listRef) {
                    top.linkTo(controlsRef.bottom, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
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
        Spacer(modifier = Modifier.height(4.dp))
        Divider()
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun StatisticsScreenPreview() {
    StatisticsScreen(onBackClick = {})
}
