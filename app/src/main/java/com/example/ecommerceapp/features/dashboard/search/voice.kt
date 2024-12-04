package com.example.ecommerceapp.features.dashboard.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat

@Composable
fun VoiceSearchScreen(
    onVoiceSearchResult: (String) -> Unit // Callback when voice search is successful
) {
    val context = LocalContext.current
    // Launcher for voice recognition intent
    val voiceRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
        spokenText?.let { onVoiceSearchResult(it) }
    }

    // Permission state for RECORD_AUDIO
    val permissionState = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                startVoiceRecognition(context, voiceRecognitionLauncher)
            } else {
                Toast.makeText(context, "Microphone permission is required for voice search", Toast.LENGTH_SHORT).show()
            }
        }
    )




    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = "Tap the microphone to search by voice", fontSize = 18.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Voice search button
        IconButton(onClick = {
            // Check permission before launching voice recognition
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startVoiceRecognition(context,voiceRecognitionLauncher)
            } else {
                permissionState.launch(Manifest.permission.RECORD_AUDIO)
            }
        }) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Voice Search",
                modifier = Modifier.size(50.dp)
            )
        }
    }
}

// Function to start voice recognition
fun startVoiceRecognition(context: Context, voiceRecognitionLauncher: ActivityResultLauncher<Intent>) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now to search")
    }
    voiceRecognitionLauncher.launch(intent)
}
