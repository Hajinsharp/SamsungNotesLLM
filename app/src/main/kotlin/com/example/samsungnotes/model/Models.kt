package com.example.samsungnotes.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val sourceApp: String = "Samsung Notes",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class LLMResponse(
    val noteId: Long,
    val prompt: String,
    val response: String,
    val model: String = "Mistral-7B",
    val processingTimeMs: Long,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

data class UIState(
    val isLoading: Boolean = false,
    val noteContent: String = "",
    val llmResponse: String = "",
    val selectedModel: String = "Mistral-7B",
    val error: String? = null,
    val isModelLoaded: Boolean = false,
    val processingProgress: Int = 0
)

data class ModelInfo(
    val name: String,
    val modelId: String,
    val size: String, // e.g., "3.8 GB"
    val parameters: String, // e.g., "7B"
    val isDownloaded: Boolean = false,
    val downloadProgress: Int = 0
)
