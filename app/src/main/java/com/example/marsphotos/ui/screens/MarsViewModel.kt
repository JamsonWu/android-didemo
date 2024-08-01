/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.marsphotos.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.marsphotos.MarsPhotosApplication
import com.example.marsphotos.data.MarsPhotosRepository
import com.example.marsphotos.network.MarsPhoto
import kotlinx.coroutines.launch
import java.io.IOException

// 接口声明加 sealed代表是受限接口，只能在当前文件中使用，外部无法访问这个接口
sealed interface MarsUiState {
    // 接口内定义的数据类，实现接口MarsUiState
    // 只是接口MarsUiState没的成员，所以数据类与对象都没有要实现的成员
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
    // 单例对象，实现接口MarsUiState
    object Error : MarsUiState
    object Loading : MarsUiState
}

class MarsViewModel(private val marsPhotosRepository: MarsPhotosRepository) : ViewModel() {
    /** The mutable State that stores the status of the most recent request */
    // 使用mutableState当数据发生变更时会更新UI
    // 目前是单一状态，希望能保存不同的状态
    var marsUiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    /**
     * Call getMarsPhotos() on init so we can display status immediately.
     * 在视图模型类初始化时调用接口
     */
    init {
        getMarsPhotos()
    }

    /**t
     * 调用接口API
     * Gets Mars photos information from the Mars API Retrofit service and updates the
     * [MarsPhoto] [List] [MutableList].
     */
    fun getMarsPhotos() {
//        marsUiState = "Set the Mars API status response here!"
        // 在viewModelScope上下文中发起一个协程，实现异步调用接口
        viewModelScope.launch {
            //  java.lang.SecurityException: Permission denied (missing INTERNET permission?)
            //  报错说明没有开通INTERNET访问权限
            //  需要加异常处理，否则程序遇到异常会崩溃退出，比如连接不上网络等
            try {
                // 与API打交道，即直接与数据源打交道
                // val listResult = MarsApi.retrofitService.getPhotos()
                // 改为与仓库打交道，让仓库与数据源打交道
                // 这里是手动实例化仓库，后面直接与仓库定义的接口打交道
                // ViewModel读取数据实现松耦合，不需要知道具体是哪个数据源，
                val listResult = marsPhotosRepository.getMarsPhotos()
                marsUiState = MarsUiState.Success(listResult)

            }
            catch (e:IOException){
               marsUiState = MarsUiState.Error
            }
        }
    }

    // companion是什么
    companion object {
        // ViewModel本身不能传参，所以只好通过Factory方式来传参
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as MarsPhotosApplication)
                val marsPhotosRepository = application.container.marsPhotosRepository
                // 实例化时注入仓库实例
                MarsViewModel(marsPhotosRepository = marsPhotosRepository)
            }
        }
    }
}
