package com.example.samsungnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.samsungnotes.model.UIState
import com.example.samsungnotes.ui.NotesLLMViewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Timber logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NotesLLMApp()
                }
            }
        }
    }
}

@Composable
fun NotesLLMApp(viewModel: NotesLLMViewModel = viewModel { NotesLLMViewModel(androidx.compose.ui.platform.LocalContext.current) }) {
    val uiState by viewModel.uiState.collectAsState()

    var showSettings by remember { mutableStateOf(false) }

    if (showSettings) {
        SettingsDialog(onDismiss = { showSettings = false })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.app_name)) },
            actions = {
                IconButton(onClick = { showSettings = true }) {
                    Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.settings))
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        )

        // Main Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Model Status
            ModelStatusCard(uiState)

            // Error Message
            if (uiState.error != null) {
                AlertCard(
                    message = uiState.error!!,
                    onDismiss = { viewModel.clearError() }
                )
            }

            // Note Input Section
            Text(
                text = stringResource(R.string.paste_note_content),
                style = MaterialTheme.typography.headlineSmall
            )

            OutlinedTextField(
                value = uiState.noteContent,
                onValueChange = { viewModel.updateNoteContent(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 150.dp),
                placeholder = { Text(stringResource(R.string.note_placeholder)) },
                enabled = !uiState.isLoading,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            // Action Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { viewModel.sendToLLM() },
                    modifier = Modifier.weight(1f),
                    enabled = !uiState.isLoading && uiState.noteContent.isNotBlank()
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(stringResource(R.string.send_to_llm))
                    }
                }

                OutlinedButton(
                    onClick = { viewModel.clearContent() },
                    modifier = Modifier.weight(0.5f),
                    enabled = !uiState.isLoading
                ) {
                    Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear))
                }
            }

            // Progress Indicator
            if (uiState.isLoading) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = (uiState.processingProgress / 100f)
                )
                Text(stringResource(R.string.processing_note))
            }

            // Response Section
            if (uiState.llmResponse.isNotBlank()) {
                Text(
                    text = stringResource(R.string.response),
                    style = MaterialTheme.typography.headlineSmall
                )

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 120.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = uiState.llmResponse,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ModelStatusCard(uiState: UIState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (uiState.isModelLoaded) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Model Status: Mistral-7B",
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (uiState.isModelLoaded) {
                    stringResource(R.string.model_loaded)
                } else if (uiState.isLoading) {
                    stringResource(R.string.loading_model)
                } else {
                    stringResource(R.string.error_loading_model)
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun AlertCard(message: String, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = message,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        }
    }
}

@Composable
fun SettingsDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.settings)) },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("App Version: 1.0.0")
                Text("Model: Mistral-7B (Quantized)")
                Text("Framework: MLC LLM")
                Divider()
                Text("Accessibility Service: Required")
                Text("Permissions: Internet, Storage")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close")
            }
        }
    )
}
