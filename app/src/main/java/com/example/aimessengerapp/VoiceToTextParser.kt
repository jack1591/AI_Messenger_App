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

// RecognitionListener - для перевода голоса в текст
class VoiceToTextParser(
    private val app: Application
): RecognitionListener {

    //Состояние распознавания речи
    private val _state = MutableStateFlow(VoiceToTextParserState())
    val state = _state.asStateFlow()

    //SpeechRecogniser для обработки голоса
    val recogniser = SpeechRecognizer.createSpeechRecognizer(app)


    //Очистить текст
    fun clearSpokenText(){
        _state.update {
            it.copy(
                spokenText = ""
            )
        }
    }

    //начать слушать
    fun startListening(languageCode: String = "ru-RU"){
        _state.update { VoiceToTextParserState() } //сбрасывание состояния

        //Если недоступен - ошибка
        if (!SpeechRecognizer.isRecognitionAvailable(app)){
            _state.update{
                it.copy(error = "Recognition is not available")
            }
            return
        }

        //если нет разрешения на запись аудио - ошибка
        if (ContextCompat.checkSelfPermission(app, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            _state.update {
                it.copy(error = "Microphone permission not granted")
            }
            return
        }


        //создание Intent'а для распознавателя речи
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply{
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM //свободная речь
                )
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE,languageCode //язык распознавания
            )
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, app.packageName) //пакет приложения
        }

        //установить слушатель
        recogniser.setRecognitionListener(this)

        //начать слушать с настройками intent
        recogniser.startListening(intent)

        _state.update{
            it.copy(
                isSpeaking = true
            )
        }
    }

    //установка слушания
    fun stopListening(){
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recogniser.stopListening()
    }


    //вызывается когда распознаватель готов к работе
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

    //завершение речи
    override fun onEndOfSpeech() {
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    //ошибка распознавания речт
    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_CLIENT)
            return
        //если ошибка не у клиента, вывести код ошибки
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    //обработка результатов распознавания речи
    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)?.let { matches ->
            if (matches.isNotEmpty()) {
                val recognizedText = matches[0] //первый вариант текста
                _state.update { it.copy(spokenText = recognizedText) } //обновление содержимого состояния
                stopListening() //закончить слушать
            }
        } ?: Log.e("voiceee", "SpeechRecognizer вернул null")
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}

//класс парсера
data class VoiceToTextParserState(
    val spokenText: String = "", //преобразованный текст из речи
    val isSpeaking: Boolean = false, //проверка, говорит ли человек
    val error: String? = null //ошибка
)