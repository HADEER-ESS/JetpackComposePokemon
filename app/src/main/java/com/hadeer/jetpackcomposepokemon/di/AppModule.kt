package com.hadeer.jetpackcomposepokemon.di

import com.hadeer.jetpackcomposepokemon.data.remote.Constant.API_SERVICE
import com.hadeer.jetpackcomposepokemon.data.remote.ServerApi
import com.hadeer.jetpackcomposepokemon.repository.PokemonRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module  //<= refer to THIS object is the ine that defining all of our dependencies
@InstallIn(SingletonComponent::class) //<= define how that OBJECT will work in our project
//SingletonComponent => create a SINGLE copy of object for ENTIRE app
//ActivityComponent  => create object that will be LIVE as long as activity DOES, can only be SHARED by activity
object AppModule{

    @Provides
    @Singleton
    fun providePokemonRepoImpl(
        api: ServerApi
    ) = PokemonRepoImpl(api)


    @Provides
    @Singleton
    //annotation add as param =>
    //@ActivityContext        => allow use to use CONTEXT
    fun provideRetrofit() : ServerApi{
        return Retrofit.Builder()
            .baseUrl(API_SERVICE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ServerApi::class.java)
    }
}