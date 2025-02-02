package app.aloha.di

import app.aloha.internet.service.AvatarApiService
import app.aloha.internet.service.CommentApiService
import app.aloha.internet.service.PostApiService
import app.aloha.internet.service.TopicApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient().newBuilder().build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://192.168.1.100:7071")
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    @Provides
    fun provideTopicApi(retrofit: Retrofit): TopicApiService {
        return retrofit.create(TopicApiService::class.java)
    }

    @Provides
    fun providePostApi(retrofit: Retrofit): PostApiService {
        return retrofit.create(PostApiService::class.java)
    }

    @Provides
    fun provideCommentApi(retrofit: Retrofit): CommentApiService {
        return retrofit.create(CommentApiService::class.java)
    }

    @Provides
    fun provideAvatarApi(retrofit: Retrofit): AvatarApiService {
        return retrofit.create(AvatarApiService::class.java)
    }
}