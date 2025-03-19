package com.kvork_app.diplomawork.intent

import com.kvork_app.diplomawork.model.dto.RequestItem

data class RequestState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val success: Boolean = false,
    val requests: List<RequestItem> = emptyList(),
    val currentRequestId: String? = null
)