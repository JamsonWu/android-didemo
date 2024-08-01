package com.example.marsphotos.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// 可序列化
@Serializable
data class MarsPhoto(
    val id: String,
    // 返回字段是img_src，转为imgSrc
    @SerialName(value = "img_src")
    val imgSrc: String
)
