package com.kvork_app.diplomawork.view.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kvork_app.diplomawork.R
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.view.viewmodels.RequestViewModel
import androidx.compose.ui.tooling.preview.Preview
import com.kvork_app.diplomawork.view.ui.theme.DateVisualTransformation

@Composable
fun AddRequestScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit,
    onSubmit: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var dateOfRegistrationRaw by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactRaw by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var masterFio by remember { mutableStateOf("") }

    val contact = "+" + contactRaw

    val statusOptions = listOf("зарегистрирована", "в работе", "выполнено", "снята")
    var expanded by remember { mutableStateOf(false) }

    val formattedDate = DateVisualTransformation().filter(AnnotatedString(dateOfRegistrationRaw)).text

    val isFormValid = formattedDate.length == 10 &&
            address.isNotBlank() &&
            contact.length > 1 &&
            description.isNotBlank() &&
            status.isNotBlank() &&
            masterFio.isNotBlank()

    val uiState by viewModel.state.collectAsState()

    if (uiState.success) {
        onSubmit()
        viewModel.clearSuccess()
    }

    uiState.errorMessage?.let { errorMsg ->
        Toast.makeText(context, "Error add", Toast.LENGTH_SHORT).show()
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
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.constrainAs(titleRef) {
                top.linkTo(parent.top, margin = 16.dp)
                start.linkTo(logoRef.end, margin = 12.dp)
            }
        )

        Text(
            text = stringResource(R.string.app_subtitle),
            fontSize = 16.sp,
            modifier = Modifier.constrainAs(subtitleRef) {
                top.linkTo(titleRef.bottom, margin = 4.dp)
                start.linkTo(titleRef.start)
            }
        )

        Text(
            text = stringResource(R.string.add_request),
            fontSize = 20.sp,
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
            Column {
                @Composable
                fun textFieldRow(
                    label: String,
                    value: String,
                    onValueChange: (String) -> Unit,
                    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                    visualTransformation: VisualTransformation = VisualTransformation.None
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = label, modifier = Modifier.width(150.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        TextField(
                            value = value,
                            onValueChange = onValueChange,
                            singleLine = true,
                            keyboardOptions = keyboardOptions,
                            visualTransformation = visualTransformation,
                            modifier = Modifier.width(200.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                textFieldRow(
                    label = stringResource(R.string.date_of_registration),
                    value = dateOfRegistrationRaw,
                    onValueChange = { newValue ->
                        dateOfRegistrationRaw = newValue.filter { it.isDigit() }.take(8)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    visualTransformation = DateVisualTransformation()
                )

                textFieldRow(
                    label = stringResource(R.string.address),
                    value = address,
                    onValueChange = { address = it }
                )

                textFieldRow(
                    label = stringResource(R.string.contact_applicant),
                    value = contact,
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }
                        contactRaw = digits
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                textFieldRow(
                    label = stringResource(R.string.description),
                    value = description,
                    onValueChange = { description = it }
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.status), modifier = Modifier.width(150.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box {
                        Button(
                            onClick = { expanded = true },
                            modifier = Modifier.width(200.dp)
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
                textFieldRow(
                    label = stringResource(R.string.master_fio),
                    value = masterFio,
                    onValueChange = { masterFio = it.filter { ch -> ch.isLetter() || ch.isWhitespace() } }
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        if (!isFormValid) {
                            Toast.makeText(
                                context,
                                "Пожалуйста, заполните все поля корректно",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            focusManager.clearFocus()
                            val request = RequestItem(
                                id = "",
                                dateOfRegistration = formattedDate.toString(),
                                address = address,
                                contact = contact,
                                description = description,
                                status = status,
                                masterFio = masterFio
                            )
                            viewModel.handleIntent(RequestIntent.SaveRequest(request))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = stringResource(R.string.submit_button),
                        color = MaterialTheme.colorScheme.onPrimary
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
