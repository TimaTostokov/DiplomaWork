package com.kvork_app.diplomawork.intent

import com.kvork_app.diplomawork.model.dto.RequestItem

sealed class RequestIntent {
    data class SaveRequest(val request: RequestItem) : RequestIntent()
    data class UpdateRequest(val id: String, val newData: RequestItem) : RequestIntent()

    object LoadAllRequests : RequestIntent()
    object LoadRequestsSortedByDateDesc : RequestIntent()
    object LoadRequestsSortedByDateAsc : RequestIntent()

    data class LoadRequestsByYear(val year: String) : RequestIntent()
}