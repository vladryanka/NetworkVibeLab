package com.smorzhok.network.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.HttpURLConnection
import java.net.URL

@Serializable
data class Message(
    @SerialName("userId") val userId: Int,
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("body") val body: String,
) {
    override fun toString(): String {
        return "Message(userId=$userId, id=$id, title='$title', body='$body')"
    }
}

var messages: List<Message> = emptyList()
var index = 0
val link = "https://jsonplaceholder.typicode.com/posts"


