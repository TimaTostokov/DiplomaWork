package com.kvork_app.diplomawork.model.dto

data class RequestItem(
    val id: String = "",
    val dateOfRegistration: String = "",
    val address: String = "",
    val contact: String = "",
    val description: String = "",
    val typeOfWork: String = "",
    val materials: List<String> = emptyList(),
    val status: String = "",
    val masterFio: String = ""
)
