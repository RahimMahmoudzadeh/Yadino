package com.rahim.yadino.base

import androidx.compose.runtime.Composable

@Composable
fun <T> LoadableComponent(
  loadableData: LoadableData<T>,
  loading: @Composable () -> Unit = {},
  loaded: @Composable (T) -> Unit,
  initial: @Composable () -> Unit = {},
) {
  when (loadableData) {
    is LoadableData.Loading -> loading()
    is LoadableData.Loaded -> loaded(loadableData.data)
    is LoadableData.Initial -> initial()
  }
}
