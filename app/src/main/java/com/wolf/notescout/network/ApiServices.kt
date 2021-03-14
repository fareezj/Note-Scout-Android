package com.wolf.notescout.network

import com.wolf.notescout.data.model.GroupRestData
import com.wolf.notescout.data.model.NoteRestData
import com.wolf.notescout.data.model.ResponseBody
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiServices {

    @GET("/scoutnote/{groupId}")
    fun getNoteItemByGroup(@Path("groupId") groupId: Int): Observable<List<NoteRestData.NoteData>>

    @GET("/scoutnote/checkGroup/{groupId}")
    fun checkGroupNoteExistence(@Path("groupId") groupId: Int): Observable<List<GroupRestData.GroupData>>

    @PUT("/scoutnote/save")
    fun addNoteItem(@Body noteData: NoteRestData.NoteData): Observable<NoteRestData.NoteData>

    @PUT("/scoutnote/isChecked/{id}")
    fun updateIsChecked(@Body isChecked: Boolean, @Path("id") id: Int): Observable<String>

    @DELETE("/scoutnote/{id}")
    fun deleteNoteItem(@Path("id") id: Int): Observable<NoteRestData.NoteData>

    @PUT("/scoutnote/addGroup/{groupId}/{groupOwner}")
    fun addNewGroupNote(@Path("groupId") groupId: Int, @Path("groupOwner") groupOwner: String): Observable<ResponseBody.Response>

    companion object {
        fun getServices(): ApiServices {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://note-scout-services.herokuapp.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiServices::class.java)
        }
    }

}