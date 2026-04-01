package com.example.samsungnotes.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.samsungnotes.llm.LLMInferenceService
import com.example.samsungnotes.model.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

class NotesLLMViewModel(context: Context) : ViewModel() {

    private val llmService = LLMInferenceService(context)

    private val _uiState = MutableStateFlow(UIState())
    val uiState: StateFlow<UIState> = _uiState.asStateFlow()

    init {
        loadModel()
    }

    private fun loadModel() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val success = llmService.loadModel()
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        isModelLoaded = success,
                        error = if (success) null else "Failed to load model"
                    )
                }
            } catch (e: Exception) {
                Timber.e(e, "Error loading model")
                _uiState.update { it.copy(isLoading = false, error = e.message) }
            }
        }
    }

    fun updateNoteContent(content: String) {
        _uiState.update { it.copy(noteContent = content) }
    }

    fun sendToLLM() {
        val currentState = _uiState.value
        if (currentState.noteContent.isBlank()) {
            _uiState.update { it.copy(error = "Please enter note content") }
            return
        }

        if (!llmService.isModelLoaded()) {
            _uiState.update { it.copy(error = "Model not loaded. Please wait...") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, processingProgress = 10) }

            try {
                val response = llmService.inference(currentState.noteContent)
                _uiState.update { it.copy(llmResponse = response, processingProgress = 100) }
            } catch (e: Exception) {
                Timber.e(e, "Error during inference")
                _uiState.update { it.copy(error = "Error: ${e.message}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun clearContent() {
        _uiState.update { it.copy(noteContent = "", llmResponse = "") }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    override fun onCleared() {
        super.onCleared()
        llmService.unloadModel()
    }
}
