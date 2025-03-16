package com.kvork_app.diplomawork.view.ui.screens

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
import com.kvork_app.diplomawork.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangesApplicationScreen(
    onBackClick: () -> Unit,
    onUpdate: () -> Unit
) {
    val focusManager = LocalFocusManager.current

    var dateOfRegistration by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val materialOptions = listOf("Материал 1", "Материал 2", "Материал 3")
    var expandedMaterials by remember { mutableStateOf(false) }
    var selectedMaterials by remember { mutableStateOf(emptyList<String>()) }

    val statusOptions = listOf("Открыта", "В работе", "Завершена")
    var expandedStatus by remember { mutableStateOf(false) }
    var status by remember { mutableStateOf("") }

    var masterFio by remember { mutableStateOf("") }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // добавляем вертикальную прокрутку для всего экрана
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
            text = "Изменение заявки",
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

                textFieldRow("Дата регистрации:", dateOfRegistration) { dateOfRegistration = it }
                textFieldRow("Адрес:", address) { address = it }
                textFieldRow("Контакт заявителя:", contact) { contact = it }
                textFieldRow("Тип проведенных работ:", description) { description = it }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Материалы:",
                        fontSize = 16.sp,
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedMaterials,
                        onExpandedChange = { expandedMaterials = !expandedMaterials },
                        modifier = Modifier.width(200.dp)
                    ) {
                        TextField(
                            value = selectedMaterials.joinToString(", "),
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMaterials)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedMaterials,
                            onDismissRequest = { expandedMaterials = false }
                        ) {
                            materialOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = {
                                        Row {
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
                    Text(
                        text = "Статус:",
                        fontSize = 16.sp,
                        modifier = Modifier.width(150.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ExposedDropdownMenuBox(
                        expanded = expandedStatus,
                        onExpandedChange = { expandedStatus = !expandedStatus },
                        modifier = Modifier.width(200.dp)
                    ) {
                        TextField(
                            value = status,
                            onValueChange = {},
                            readOnly = true,
                            singleLine = true,
                            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus)
                            },
                            colors = ExposedDropdownMenuDefaults.textFieldColors()
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStatus,
                            onDismissRequest = { expandedStatus = false }
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

                textFieldRow("ФИО мастера:", masterFio) { masterFio = it }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        focusManager.clearFocus()
                        onUpdate()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00AA00)),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "ИЗМЕНИТЬ",
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, device = "spec:width=411dp,height=891dp")
@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp")
@Composable
fun ChangesApplicationScreenPreview() {
    ChangesApplicationScreen(
        onBackClick = {},
        onUpdate = {}
    )
}
