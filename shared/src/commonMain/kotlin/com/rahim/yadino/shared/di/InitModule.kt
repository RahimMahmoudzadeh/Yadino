package com.rahim.yadino.shared.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
//val featureModules = listOf<Module>(HomeDiModule)
fun initKoin(config: KoinAppDeclaration? = null) {
  startKoin {
    config?.invoke(this)
//    modules(featureModules)
  }
}
