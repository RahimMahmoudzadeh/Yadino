package com.rahim.yadino.home.presentation.ui.main

import com.rahim.yadino.home.presentation.ui.main.contract.HomeContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(): HomeContract {


  override val state: StateFlow<HomeContract.State>
    get() = TODO("Not yet implemented")

  override val effects: Flow<HomeContract.Effect>
    get() = TODO("Not yet implemented")

  override fun onEvent(event: HomeContract.Event) {
    TODO("Not yet implemented")
  }

}
