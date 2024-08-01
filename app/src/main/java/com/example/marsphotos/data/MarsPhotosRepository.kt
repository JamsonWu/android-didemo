package com.example.marsphotos.data

import com.example.marsphotos.network.MarsApiService
import com.example.marsphotos.network.MarsPhoto

// 仓库，提供接口给UI调用
interface MarsPhotosRepository {
    suspend fun getMarsPhotos(): List<MarsPhoto>
}

// 创建一个仓库类实现接口
// 注意入参是私有变量，避免实例还能改变
class NetworkMarsPhotosRepository(private val retrofitService: MarsApiService): MarsPhotosRepository {

    //   以下是完整写法
    //    override suspend fun getMarsPhotos(): List<MarsPhoto> {
    //        return retrofitService.getPhotos()
    //    }
    // 下面是简写版
    override suspend fun getMarsPhotos(): List<MarsPhoto> = retrofitService.getPhotos()

}

