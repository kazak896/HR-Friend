package ru.yandex.hrfriend.domain.repository.stomp

import ua.naiksoftware.stomp.StompClient
import ua.naiksoftware.stomp.dto.StompMessage

interface StompRepository {

    suspend fun connect() : StompClient

    suspend fun topic(stompClient: StompClient) : StompMessage

    suspend fun send(groupId: Int, jsonString: String, stompClient: StompClient)

}