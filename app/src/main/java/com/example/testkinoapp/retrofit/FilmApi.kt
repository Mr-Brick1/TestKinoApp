package com.example.testkinoapp.retrofit

import retrofit2.Response
import retrofit2.http.GET

interface FilmApi {
    @GET("films.json")
    suspend fun getNestedFilms(): Response<NestedFilms>


}