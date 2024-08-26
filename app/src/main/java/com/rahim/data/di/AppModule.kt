package com.rahim.data.di

import com.rahim.utils.permissionHandler.PermissionHandlerNotification
import com.rahim.utils.permissionHandler.PermissionHandlerNotificationImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
  @Binds
  abstract fun providePermissionHandler(permissionHandlerNotificationImpl: PermissionHandlerNotificationImpl): PermissionHandlerNotification
}
