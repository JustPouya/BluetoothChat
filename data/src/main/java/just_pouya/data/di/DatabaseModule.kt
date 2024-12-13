package just_pouya.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import just_pouya.data.db.ChatDatabase
import just_pouya.data.db.daos.DeviceDao
import just_pouya.data.db.daos.MessageDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule{

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ChatDatabase {
        return Room.databaseBuilder(context.applicationContext, ChatDatabase::class.java,"app_dataBase.db")
            .build()
    }

    @Provides
    fun provideDeviceDao(database: ChatDatabase): DeviceDao = database.deviceDao()

    @Provides
    fun provideMessageDao(database: ChatDatabase): MessageDao = database.messageDao()

}