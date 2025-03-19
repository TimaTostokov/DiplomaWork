package com.kvork_app.diplomawork.view.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kvork_app.diplomawork.model.dto.RequestItem
import com.kvork_app.diplomawork.model.repository.RequestRepository
import com.kvork_app.diplomawork.intent.RequestIntent
import com.kvork_app.diplomawork.intent.RequestState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RequestViewModel(
    private val repository: RequestRepository = RequestRepository()
) : ViewModel() {

    private val _state = MutableStateFlow(RequestState())
    val state: StateFlow<RequestState> = _state

    fun clearSuccess() {
        _state.value = _state.value.copy(success = false)
    }

    fun handleIntent(intent: RequestIntent) {
        when (intent) {
            is RequestIntent.SaveRequest -> {
                saveRequest(intent.request)
            }
            is RequestIntent.UpdateRequest -> {
                updateRequest(intent.id, intent.newData)
            }
            RequestIntent.LoadAllRequests -> {
                loadAllRequests()
            }
            RequestIntent.LoadRequestsSortedByDateDesc -> {
                loadRequestsSortedByDateDesc()
            }
            RequestIntent.LoadRequestsSortedByDateAsc -> {
                loadRequestsSortedByDateAsc()
            }
            is RequestIntent.LoadRequestsByYear -> {
                loadRequestsByYear(intent.year)
            }
        }
    }

    private fun saveRequest(request: RequestItem) {
        _state.value = _state.value.copy(isLoading = true, success = false, errorMessage = null)
        viewModelScope.launch {
            val requests = repository.getAllRequests()
            val maxId = requests.mapNotNull { it.id.toIntOrNull() }.maxOrNull() ?: 0
            val newId = (maxId + 1).toString()
            val requestToSave = request.copy(id = newId)
            val resultId = repository.addRequest(requestToSave)
            if (resultId != null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    success = true,
                    errorMessage = null,
                    currentRequestId = resultId
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    success = false,
                    errorMessage = "Не удалось сохранить заявку"
                )
            }
        }
    }

    private fun updateRequest(id: String, newData: RequestItem) {
        _state.value = _state.value.copy(isLoading = true, success = false, errorMessage = null)
        viewModelScope.launch {
            val result = repository.updateRequestById(id, newData)
            if (result) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    success = true,
                    errorMessage = null,
                    currentRequestId = id
                )
            } else {
                _state.value = _state.value.copy(
                    isLoading = false,
                    success = false,
                    errorMessage = "Не удалось обновить заявку (id=$id)"
                )
            }
        }
    }

    private fun loadAllRequests() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val list = repository.getAllRequests()
            _state.value = _state.value.copy(
                isLoading = false,
                requests = list,
                errorMessage = null
            )
        }
    }

    private fun loadRequestsSortedByDateDesc() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val list = repository.getRequestsSortedByDateDesc()
            _state.value = _state.value.copy(
                isLoading = false,
                requests = list,
                errorMessage = null
            )
        }
    }

    private fun loadRequestsSortedByDateAsc() {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val desc = repository.getRequestsSortedByDateDesc()
            val asc = desc.reversed()
            _state.value = _state.value.copy(
                isLoading = false,
                requests = asc,
                errorMessage = null
            )
        }
    }

    private fun loadRequestsByYear(year: String) {
        _state.value = _state.value.copy(isLoading = true, errorMessage = null)
        viewModelScope.launch {
            val list = repository.getRequestsByYear(year)
            _state.value = _state.value.copy(
                isLoading = false,
                requests = list,
                errorMessage = null
            )
        }
    }
}
