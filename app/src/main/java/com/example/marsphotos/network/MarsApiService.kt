package com.example.marsphotos.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

// 后台服务接口定义
interface MarsApiService {
    // 获取图片方法声明
    // 注解 @GET告知这是GET请求
    // photos代表终结点即后台服务接口对外公布的方法名
    // 添加 suspend 声明为异步方法
    @GET("photos")
    suspend fun getPhotos():List<MarsPhoto>
}

