package com.kvork_app.diplomawork.view.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.kvork_app.diplomawork.R

@Composable
fun AddressStatsScreen(
    onBackClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }

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
            contentRef
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
            text = "Статистика по адресам",
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
                .constrainAs(searchRowRef) {
                    top.linkTo(screenTitleRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Поиск") },
                singleLine = true,
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
        }

        Box(
            modifier = Modifier
                .constrainAs(contentRef) {
                    top.linkTo(searchRowRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }
                .fillMaxWidth()
        ) {
            Text(
                text = "Таблица со всеми данными по заявкам и адресам",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.TopStart)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddressStatsScreenPreview() {
    AddressStatsScreen(onBackClick = {})
}