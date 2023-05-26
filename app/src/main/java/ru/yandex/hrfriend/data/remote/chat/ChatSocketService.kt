package ru.yandex.hrfriend.data.remote.chat

import kotlinx.coroutines.flow.Flow
import ru.yandex.hrfriend.domain.models.Message
import ru.yandex.hrfriend.util.Resource

interface ChatSocketService {
    suspend fun initSession(
        username: String
    ): Resource<Unit>

    suspend fun sendMessage(message: String)

    fun observeMessages(): Flow<Message>

    suspend fun closeSession()

    companion object {
        const val BASE_URL = "ws://10.0.2.2:7777"
        //const val BASE_URL = "ws://192.168.0.2:7777"
    }

    sealed class Endpoints(val url: String) {
        object ChatSocket: Endpoints("$BASE_URL/api/v1/chat/websocket")
    }


}