package com.example.aimessengerapp

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import android.Manifest
import android.util.Log

class VoiceToTextParser(
    private val app: Application
): RecognitionListener {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    val recogniser = SpeechRecognizer.createSpeechRecognizer(app)

    fun clearSpokenText(){
        _state.update {
            it.copy(
                spokenText = ""
            )
        }
    }
    fun startListening(languageCode: String = "ru-RU"){
        _state.update { VoiceToTextParserState() }

        if (!SpeechRecognizer.isRecognitionAvailable(app)){
            Log.i("voiceee","Error")
            _state.update{
                it.copy(error = "Recognition is not available")
            }
        }


        if (ContextCompat.checkSelfPermission(app, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.i("voiceee","Error")
            _state.update {
                it.copy(error = "Microphone permission not granted")
            }
            return
        }


        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,languageCode
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, app.packageName)
        }

        recogniser.setRecognitionListener(this)
        recogniser.startListening(intent)

        _state.update{
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening(){
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recogniser.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT)
            return
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        Log.i("voiceee", "onResults() вызван")
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
            if (matches.isNotEmpty()) {
                val recognizedText = matches[0]
                Log.i("voiceee", "Распознанный текст: $recognizedText")
                _state.update { it.copy(spokenText = recognizedText) }
                stopListening()
            } else {
                Log.e("voiceee", "SpeechRecognizer вернул пустой список")
            }
        } ?: Log.e("voiceee", "SpeechRecognizer вернул null")

    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}

data class VoiceToTextParserState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)