package com.kvork_app.diplomawork.view.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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
fun AddRequestScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    var customId by remember { mutableStateOf("") }

    var dateOfRegistration by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var masterFio by remember { mutableStateOf("") }

    val statusOptions = listOf("зарегистрирована", "в работе", "выполнено", "снята")
    var expanded by remember { mutableStateOf(false) }

    val uiState by viewModel.state.collectAsState()

    if (uiState.success) {
        onSubmit()
    }

    uiState.errorMessage?.let { errorMsg ->
        Log.e("AddRequestScreen", "Ошибка сохранения: $errorMsg")
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val (backBtnRef, logoRef, titleRef, subtitleRef, screenTitleRef, formBoxRef) = createRefs()

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
            fontWeight = FontWeight.Bold,
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
            text = stringResource(R.string.add_request),
            fontSize = 20.sp,
            color = Color.Black,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.constrainAs(screenTitleRef) {
                top.linkTo(logoRef.bottom, margin = 32.dp)
                centerHorizontallyTo(parent)
            }
        )

        Box(
            modifier = Modifier
                .constrainAs(formBoxRef) {
                    top.linkTo(screenTitleRef.bottom, margin = 24.dp)
                    centerHorizontallyTo(parent)
                }
                .fillMaxWidth()
        ) {
            Column(horizontalAlignment = Alignment.Start) {

                @Composable
                fun textFieldRow(label: String, value: String, onValueChange: (String) -> Unit) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = label,
                            fontSize = 16.sp,
                            modifier = Modifier.width(150.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = value,
                            onValueChange = onValueChange,
                            modifier = Modifier.width(200.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                textFieldRow("ID заявки:", customId) {
                    customId = it
                }

                textFieldRow(stringResource(R.string.date_of_registration), dateOfRegistration) {
                    dateOfRegistration = it
                }
                textFieldRow(stringResource(R.string.address), address) {
                    address = it
                }
                textFieldRow(stringResource(R.string.contact_applicant), contact) {
                    contact = it
                }
                textFieldRow(stringResource(R.string.description), description) {
                    description = it
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = stringResource(R.string.status),
                        fontSize = 16.sp,
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.width(200.dp),
                        ) {
                            Text(text = if (status.isEmpty()) "Выберите статус" else status)
                        }
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        status = option
                                        expanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                textFieldRow(stringResource(R.string.master_fio), masterFio) {
                    masterFio = it
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        val request = RequestItem(
                            id = customId,
                            dateOfRegistration = dateOfRegistration,
                            address = address,
                            contact = contact,
                            description = description,
                            status = status,
                            masterFio = masterFio
                        )
                        viewModel.handleIntent(RequestIntent.SaveRequest(request))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.submit_button),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddRequestScreenPreview() {
    AddRequestScreen(
        onBackClick = {},
        onSubmit = {}
    )

}