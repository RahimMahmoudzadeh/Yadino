package com.rahim.yadino.base

import androidx.compose.runtime.Composable
import com.rahim.yadino.enums.error.ErrorMessageCode

@Composable
fun <T> LoadableComponent(
  loadableData: LoadableData<T>,
  loading: @Composable () -> Unit = {},
  error: @Composable (ErrorMessageCode) -> Unit,
  loaded: @Composable (T) -> Unit,
  initial: @Composable () -> Unit = {},
) {
    when (loadableData) {
        is LoadableData.Loading -> loading()
        is LoadableData.Error -> error(loadableData.error)
        is LoadableData.Loaded -> loaded(loadableData.data)
        is LoadableData.Initial -> initial()
    }
}
