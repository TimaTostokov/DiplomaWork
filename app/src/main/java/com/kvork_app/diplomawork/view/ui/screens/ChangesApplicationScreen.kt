package com.kvork_app.diplomawork.view.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
fun ChangesApplicationScreen(
    viewModel: RequestViewModel = viewModel(),
    onBackClick: () -> Unit,
    onUpdate: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    var requestId by remember { mutableStateOf("") }
    var dateOfRegistrationRaw by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contactRaw by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("") }
    var masterFio by remember { mutableStateOf("") }

    var workType by remember { mutableStateOf("") }
    var customWorkType by remember { mutableStateOf("") }
    val workTypeOptions = listOf(
        "Регулировка БВД",
        "Регулировка БУД",
        "Регулировка ЭМЗ",
        "Регулировка доводчика",
        "Диагностика ЗУ",
        "Диагностика БВД",
        "Диагностика БУД",
        "Ремонт БВД",
        "Ремонт БУД",
        "Ремонт БПД",
        "Ремонт ЭМЗ",
        "Ремонт доводчика",
        "Крепление БВД",
        "Крепление ЭМЗ",
        "Крепление доводчика",
        "Монтаж БВД",
        "Монтаж БУД",
        "Монтаж БПД",
        "Монтаж БК",
        "Монтаж крепёж №8",
        "Монтаж кнопки выхода",
        "Монтаж платы управления ЭМЗ",
        "Монтаж ЭМЗ",
        "Монтаж доводчика",
        "Плановое ТО",
        "Устранение обрыва кабеля",
        "Монтаж кабеля",
        "Устранение КЗ",
        "Монтаж УКП",
        "Крепление тяги доводчика",
        "Крепление крепёж №6",
        "Крепление крепёж №8",
        "Монтаж контактора ключей",
        "Иное"
    )
    var expandedWorkType by remember { mutableStateOf(false) }

    val materialOptions = listOf(
        "Бвд 431 dxcvb",
        "Бвд 432 dxcvf",
        "Бвд N100",
        "Бвд N100V",
        "Бвд sm-100",
        "Бвд sm-101",
        "БВД 311 tm",
        "БВД 311 rf",
        "БВД 321 tm",
        "БВД 321 rf",
        "БК 2V",
        "Бвд цифрал 2094tm",
        "Бвд цифрал 2094 -И tm",
        "Буд 420-м",
        "Буд 302 -к",
        "БУД 302 -s",
        "Бпд визит 18/12 -01",
        "Бпд цифрал БП-1",
        "Бк-100",
        "Кгм 100 цифрал",
        "Кнопка выхода КоаП-1",
        "ЭМЗ визит",
        "ЭМЗ цифрал",
        "Плата управления Z5",
        "Плата управления цифрал",
        "Крепеж N6 (пятка)",
        "Крепеж N7 (болты для ЭМЗ)",
        "Крепеж N8 (якорь)",
        "Уголок ЦФРЛ 50*60*5",
        "Доводчик N605",
        "Доводчик ( легкий)",
        "Тяга доводчика",
        "Контактор ключей"
    )
    var expandedMaterials by remember { mutableStateOf(false) }
    var selectedMaterials by remember { mutableStateOf(listOf<String>()) }

    val statusOptions = listOf("Зарегистрирована", "В работе", "Выполнена", "Снята")
    var expandedStatus by remember { mutableStateOf(false) }

    val formattedDate = DateVisualTransformation().filter(AnnotatedString(dateOfRegistrationRaw)).text
    val formattedContact = "+$contactRaw"

    val isFormValid = formattedDate.length == 10 &&
            address.isNotBlank() &&
            formattedContact.length > 1 &&
            masterFio.isNotBlank() &&
            workType.isNotBlank() &&
            (workType != "Иное" || customWorkType.isNotBlank())

    val uiState by viewModel.state.collectAsState()

    LaunchedEffect(uiState.currentRequest) {
        uiState.currentRequest?.let { req ->
            dateOfRegistrationRaw = req.dateOfRegistration
            address = req.address
            contactRaw = req.contact.removePrefix("+")
            description = req.description
            status = req.status
            masterFio = req.masterFio
            workType = req.typeOfWork
            selectedMaterials = req.materials
        }
    }

    LaunchedEffect(requestId) {
        if (requestId.isNotEmpty()) {
            viewModel.loadRequestById(requestId)
        }
    }

    if (uiState.success) {
        onUpdate()
        viewModel.clearSuccess()
    }

    uiState.errorMessage?.let { errorMsg ->
        Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
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
            text = "Изменение заявки",
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
                    label = "ID заявки:",
                    value = requestId,
                    onValueChange = { newValue ->
                        requestId = newValue.filter { it.isDigit() }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

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
                    value = formattedContact,
                    onValueChange = { newValue ->
                        val digits = newValue.filter { it.isDigit() }
                        contactRaw = digits
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    )
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Тип работ:", modifier = Modifier.width(150.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(onClick = { expandedWorkType = true }, modifier = Modifier.width(200.dp)) {
                            Text(text = if (workType.isEmpty()) "Выберите тип" else workType)
                        }
                        DropdownMenu(
                            expanded = expandedWorkType,
                            onDismissRequest = { expandedWorkType = false },
                            modifier = Modifier
                                .height(600.dp)
                                .width(200.dp)
                        ) {
                            workTypeOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        workType = option
                                        expandedWorkType = false
                                    }
                                )
                            }
                        }
                    }
                }
                if (workType == "Иное") {
                    textFieldRow(
                        label = "Введите тип:",
                        value = customWorkType,
                        onValueChange = { customWorkType = it }
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Материалы:", modifier = Modifier.width(150.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(onClick = { expandedMaterials = true }, modifier = Modifier.width(200.dp)) {
                            Text(
                                text = if (selectedMaterials.isEmpty()) "Выберите материалы" else selectedMaterials.joinToString(", ")
                            )
                        }
                        DropdownMenu(
                            expanded = expandedMaterials,
                            onDismissRequest = { expandedMaterials = false },
                            modifier = Modifier
                                .height(600.dp)
                                .width(200.dp)
                                .padding(horizontal = 8.dp)
                        ) {
                            materialOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            val isSelected = option in selectedMaterials
                                            Checkbox(
                                                checked = isSelected,
                                                onCheckedChange = { checked ->
                                                    selectedMaterials = if (checked) {
                                                        selectedMaterials + option
                                                    } else {
                                                        selectedMaterials - option
                                                    }
                                                }
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(option)
                                        }
                                    },
                                    onClick = {
                                        val isSelected = option in selectedMaterials
                                        selectedMaterials = if (isSelected) {
                                            selectedMaterials - option
                                        } else {
                                            selectedMaterials + option
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = stringResource(R.string.status), modifier = Modifier.width(150.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.wrapContentSize()) {
                        Button(onClick = { expandedStatus = true }, modifier = Modifier.width(200.dp)) {
                            Text(text = if (status.isEmpty()) "Выберите статус" else status)
                        }
                        DropdownMenu(
                            expanded = expandedStatus,
                            onDismissRequest = { expandedStatus = false },
                            modifier = Modifier.width(200.dp)
                        ) {
                            statusOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option) },
                                    onClick = {
                                        status = option
                                        expandedStatus = false
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
                            val finalWorkType = if (workType == "Иное") customWorkType else workType
                            val updatedRequest = RequestItem(
                                id = requestId,
                                dateOfRegistration = DateVisualTransformation().filter(AnnotatedString(dateOfRegistrationRaw)).text.toString(),
                                address = address,
                                contact = formattedContact,
                                description = description,
                                status = status,
                                masterFio = masterFio,
                                typeOfWork = finalWorkType,
                                materials = selectedMaterials
                            )
                            viewModel.handleIntent(RequestIntent.UpdateRequest(id = requestId, newData = updatedRequest))
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "ИЗМЕНИТЬ", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChangesApplicationScreenPreview() {
    ChangesApplicationScreen(
        onBackClick = {},
        onUpdate = {}
    )
}
