package ru.yandex.hrfriend.util

import java.util.Collections

class Constants {
    companion object {
        const val KEY_PREFERENCE_NAME = "HRFriendPrefs"
        const val JWT_KEY = "jwt_key"
        const val JWT_REFRESH = "jwt_refresh"
        const val USERNAME = "username"
        const val LASTNAME = "lastname"
        const val ROLE = "role"
        const val EMAIL = "email"
        const val PATH_LINK = "path_link"
        const val ID = "id"
        const val TIMESTAMP = "timestamp"

        fun getAuthHeader(token: String) : String {
            return "Bearer $token"
        }


        //const val domain = "kas-dev.bresler.ru"
        const val domain = "10.0.2.2:8080"
        //const val domain = "192.168.18.4:7777"
        // const val domain = "kas-dev.bresler.ru"
        //const val domain = "mail.bresler.ru:7777"

        const val placeholder = "placeholder"

        //        const val address = "ws://10.0.2.2:8080/im/websocket"
        const val address = "ws://10.0.2.2:8080/im/websocket"

        const val broadcast = "/broadcast"
        const val broadcastResponse = "/b"

        // replace {@code placeholder} with group id
        const val group = "/group/$placeholder"
        const val groupResponse = "/g/$placeholder"
        const val chat = "/chat"

        // replace {@code placeholder} with user id
        const val chatResponse = "/user/$placeholder/msg"
    }
}