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

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.marsphotos.R
import com.example.marsphotos.network.MarsPhoto
import com.example.marsphotos.ui.theme.MarsPhotosTheme

@Composable
fun HomeScreen(
    marsUiState: MarsUiState,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    when (marsUiState) {
        // 接口调用成功状态
        // is判断是否为某个类的实例
        is MarsUiState.Success -> ResultScreen(
            // 好难理解，使用is后，才能访问marsUiState.photos
            // 为啥要写这么难理解的代码？
            // 由于marsUiState是MarsUiState.Success类型
            // 此时marsUiState会被视为MarsUiState.Success
            marsUiState.photos, modifier = modifier.fillMaxWidth()
        )
        // 接口调用异常状态
        is MarsUiState.Error -> Text(text = "Error")
        // 接口正在调用中
        is MarsUiState.Loading -> Text(text = "Loading")
    }
    // ResultScreen(marsUiState, modifier.padding(top = contentPadding.calculateTopPadding()))
}

/**
 * ResultScreen displaying number of photos retrieved.
 */
@Composable
fun ResultScreen(photos: List<MarsPhoto>,
                       modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {

        //        普通图片显示
        //        AsyncImage(
        //            model = photos,
        //            contentDescription = null
        //        )

        // 自定义图片显示
        //        AsyncImage(
        //            error = painterResource(R.drawable.ic_broken_image),
        //            placeholder = painterResource(R.drawable.loading_img),
        //            model = ImageRequest.Builder(LocalContext.current)
        //                .data(photos)
        //                .crossfade(true)
        //                .build(),
        //            contentDescription = null,
        //            contentScale = ContentScale.Crop,
        //            //  modifier = Modifier.clip(CircleShape)
        //            modifier = Modifier.fillMaxWidth()
        //
        //        )

        ImageGrid(imageList = photos,modifier)
    }
}
@Composable
fun ImageItem(imgUrl:String,modifier: Modifier=Modifier){
    Log.d("ImageItem TAG", imgUrl)
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        AsyncImage(
            error = painterResource(R.drawable.ic_broken_image),
            placeholder = painterResource(R.drawable.loading_img),
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            //  modifier = Modifier.clip(CircleShape)
            modifier = Modifier.fillMaxWidth()

        )
    }
}

// 表格显示图片
@Composable
fun ImageGrid(imageList:List<MarsPhoto>,
              modifier: Modifier = Modifier,

              contentPadding: PaddingValues = PaddingValues(0.dp),) {
    // 垂直表格布局
    LazyVerticalGrid(
        columns = GridCells.Adaptive(150.dp),
        modifier = modifier.padding(horizontal = 4.dp),
        contentPadding = contentPadding,
        // 设置垂直间隔
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
        // 设置水平间隔
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_small)),
    ) {
        items(imageList,key={it.id }) { image ->
           ImageItem(image.imgSrc)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenPreview() {
    MarsPhotosTheme {
        // ResultScreen(stringResource(R.string.placeholder_result))
    }
}
