package just_pouya.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import just_pouya.data.repositories.BluetoothRepositoryImpl
import just_pouya.data.repositories.DeviceRepositoryImpl
import just_pouya.data.repositories.MessageRepositoryImpl
import justp.k.domain.repositories.BluetoothRepository
import justp.k.domain.repositories.DeviceRepository
import justp.k.domain.repositories.MessageRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindBluetoothRepository(
        bluetoothRepositoryImpl: BluetoothRepositoryImpl
    ): BluetoothRepository

    @Singleton
    @Binds
    abstract fun bindDeviceRepository(deviceRepositoryImpl: DeviceRepositoryImpl): DeviceRepository

    @Singleton
    @Binds
    abstract fun bindMessageRepository(
        messageRepositoryImpl: MessageRepositoryImpl
    ): MessageRepository

}