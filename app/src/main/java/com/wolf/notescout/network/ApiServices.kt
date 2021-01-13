package com.wolf.notescout.network

import com.wolf.notescout.data.model.NoteRestData
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServices {

    @GET("/groceries/")
    fun getGroceries(): Observable<List<NoteRestData.NoteData>>

    @GET("/groceries/group/{groupId}")
    fun getNoteItemByGroup(@Path("groupId") groupId: Int): Observable<List<NoteRestData.NoteData>>

    @PUT("/groceries")
    fun addNoteItem(@Body noteData: NoteRestData.NoteData): Observable<NoteRestData.NoteData>

    @PUT("/groceries/isChecked/{id}")
    fun updateIsChecked(@Body isChecked: Boolean, @Path("id") id: Long): Observable<NoteRestData.NoteData>

    @DELETE("/groceries/{id}")
    fun deleteNoteItem(@Path("id") id: Long): Observable<NoteRestData.NoteData>

    companion object {
        fun getServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://grocery-app-service.herokuapp.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }

}