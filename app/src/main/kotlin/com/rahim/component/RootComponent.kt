package com.rahim.component

import com.arkivanov.decompose.value.Value
import com.rahim.yadino.home.presentation.component.HomeComponent

interface RootComponent {
  val stack: Value<com.arkivanov.decompose.router.stack.ChildStack<*, ChildStack>>

  sealed class ChildStack {
    class HomeStack(val homeComponent: HomeComponent) : ChildStack()
  }
}
