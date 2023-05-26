package ru.yandex.hrfriend.presentation.chat

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ru.yandex.hrfriend.data.dto.chat.MessageDto
import ru.yandex.hrfriend.domain.models.Message
import ru.yandex.hrfriend.presentation.chat.util.GsonLocalDateTimeAdapter
import ru.yandex.hrfriend.util.Constants
import ru.yandex.hrfriend.util.PreferencesManager
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.StompMessage
import ua.naiksoftware.stomp.provider.OkHttpConnectionProvider.TAG
import java.time.LocalDateTime
import java.util.Collections
import javax.inject.Inject
import kotlin.streams.toList

@HiltViewModel
class StompViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager
) : ViewModel() {


    companion object {
//        const val SOCKET_URL = "ws://10.0.2.2:7777/api/v1/chat/websocket"
//        const val SOCKET_URL = "ws://192.168.18.2:7000/api/v1/chat/websocket"
//        const val SOCKET_URL = "ws://${Constants.domain}/api/v1/chat/websocket"
        const val SOCKET_URL = "ws://mail.bresler.ru:7777/api/v1/chat/websocket"
        const val CHAT_TOPIC = "/topic/chat"
        const val CHAT_LINK_SOCKET = "/api/v1/chat/sock"
        const val MESSAGES_LINK_SOCKET = "/api/v1/chat/all"
    }

    private val gson: Gson = GsonBuilder().registerTypeAdapter(
        LocalDateTime::class.java,
        GsonLocalDateTimeAdapter()
    ).create()
    private var mStompClient: StompClient? = null
    private var compositeDisposable: CompositeDisposable? = null

    private val _chatState = MutableLiveData<Message>()
    val liveChatState: LiveData<Message> = _chatState

    private val _initChatState = MutableLiveData<List<Message>>()
    val initChatState: LiveData<List<Message>> = _initChatState

    init {
            val headerMap: Map<String, String> =
                Collections.singletonMap("Authorization", "Bearer " + preferencesManager.getString(Constants.JWT_KEY))
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, SOCKET_URL, headerMap)
            .withServerHeartbeat(30000)
        resetSubscriptions()
        initializeChat()
    }

    private fun initializeChat() {
        resetSubscriptions()
        if (mStompClient != null) {
            val topicSubscribe = mStompClient!!.topic(MESSAGES_LINK_SOCKET)
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ messages: StompMessage ->
                    Log.d(TAG, messages.payload)
                    val type = object : TypeToken<Collection<MessageDto>>() {}.type
                    val messageList: List<MessageDto> =
                        gson.fromJson(messages.payload, type)
                    addMessages(messageList)
                    startChat()
                },
                    {
                        Log.e(TAG, "Error2!", it)
                    }
                )

            val lifecycleSubscribe = mStompClient!!.lifecycle()
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lifecycleEvent: LifecycleEvent ->
                    when (lifecycleEvent.type!!) {
                        LifecycleEvent.Type.OPENED -> Log.d(TAG, "Stomp connection opened")
                        LifecycleEvent.Type.ERROR -> Log.e(TAG, "Error", lifecycleEvent.exception)
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT,
                        LifecycleEvent.Type.CLOSED -> {
                            Log.d(TAG, "Stomp connection closed")
                        }
                    }
                }

            compositeDisposable!!.add(lifecycleSubscribe)

            compositeDisposable!!.add(topicSubscribe)

            if (!mStompClient!!.isConnected) {
                mStompClient!!.connect()
            }
        }
    }

    private fun startChat() {
        resetSubscriptions()

        if (mStompClient != null) {
            val topicSubscribe = mStompClient!!.topic(CHAT_TOPIC)
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ topicMessage: StompMessage ->
                    Log.d(TAG, topicMessage.payload)

                    //val type = object : TypeToken<Collection<MessageDto>>() {}.type

                    val messageDto: MessageDto =
                        gson.fromJson(topicMessage.payload, MessageDto::class.java)
                    //val newMessage = messageDto.toMessage()
                    addMessage(messageDto)
                },
                    {
                        Log.e(TAG, "Error!", it)
                    }
                )

            val lifecycleSubscribe = mStompClient!!.lifecycle()
                .subscribeOn(Schedulers.io(), false)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { lifecycleEvent: LifecycleEvent ->
                    when (lifecycleEvent.type!!) {
                        LifecycleEvent.Type.OPENED -> Log.d(TAG, "Stomp connection opened")
                        LifecycleEvent.Type.ERROR -> Log.e(TAG, "Error", lifecycleEvent.exception)
                        LifecycleEvent.Type.FAILED_SERVER_HEARTBEAT,
                        LifecycleEvent.Type.CLOSED -> {
                            Log.d(TAG, "Stomp connection closed")
                        }
                    }
                }

            compositeDisposable!!.add(lifecycleSubscribe)
            compositeDisposable!!.add(topicSubscribe)

            if (!mStompClient!!.isConnected) {
                mStompClient!!.connect()
            }


        } else {
            Log.e(TAG, "mStompClient is null!")
        }
    }

    fun sendMessage(text: String) {
        val messageDto = MessageDto(
            text,
            System.currentTimeMillis(),
            preferencesManager.getString(Constants.USERNAME),
            "1"
        )
        sendCompletable(mStompClient!!.send(CHAT_LINK_SOCKET, gson.toJson(messageDto)))
        //addMessage(messageDto)
    }

    private fun addMessage(message: MessageDto) {
        _chatState.value = message.toMessage()
    }
    private fun addMessages(messages: List<MessageDto>) {
        _initChatState.value = messages.stream().map {it.toMessage()}.toList()
    }

    private fun sendCompletable(request: Completable) {
        compositeDisposable?.add(
            request.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        Log.d(TAG, "Stomp sended")
                    },
                    {
                        Log.e(TAG, "Stomp error", it)
                    }
                )
        )
    }

    private fun resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable!!.dispose()
        }

        compositeDisposable = CompositeDisposable()
    }

    override fun onCleared() {
        super.onCleared()

        mStompClient?.disconnect()
        compositeDisposable?.dispose()
    }

}