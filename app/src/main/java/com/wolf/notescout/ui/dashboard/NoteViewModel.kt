package com.wolf.notescout.ui.dashboard

import android.app.Application
import android.text.BoringLayout
import android.util.Log
import androidx.lifecycle.*
import com.wolf.notescout.data.model.NoteRestData
import com.wolf.notescout.network.ApiServices
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class NoteViewModel(application: Application): AndroidViewModel(application) {

    private var completedNote: ArrayList<NoteRestData.NoteData> = arrayListOf()

    private var noteAPI: ApiServices = ApiServices.getServices()
    private var subscription = CompositeDisposable()

    private val _allNotesData = MutableLiveData<List<NoteRestData.NoteData>>()
    val allNotesData: LiveData<List<NoteRestData.NoteData>> = _allNotesData

    private val _completedTask = MutableLiveData<Int>(0)
    val completedTask: LiveData<Int> = _completedTask

    private fun getAllNotesFromApi(): Observable<List<NoteRestData.NoteData>>{
        return noteAPI.getGroceries()
    }

     fun getCompletedNote() {

         _completedTask.value = 0
        val filteredNote = completedNote.filter { it.isChecked }
        _completedTask.value = filteredNote.size
        Log.i("FILTETED DONE TASK:", filteredNote.size.toString())

    }

    fun handleAddNote(item: String, isChecked: Boolean, username: String, groupID: Long) {
        val subscribe = noteAPI.addNoteItem(
            NoteRestData.NoteData(
                    item = item,
                    isChecked = isChecked,
                    username = username,
                    groupID = groupID
            )
        )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                handleGetAllNotesFromApi()
            }, { err -> var msg = err.localizedMessage
                Log.i("DATA", msg.toString())
            })
        subscription.add(subscribe)
    }

    fun handleNoteItemIsChecked(isChecked: Boolean, id: Long){
        val subscribe = noteAPI.updateIsChecked(isChecked, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                }, { err -> var msg = err.localizedMessage
                    Log.i("DATA", msg.toString())
                })
        subscription.add(subscribe)
    }

    fun handleDeleteNoteItem(id: Long){
        val subscribe = noteAPI.deleteNoteItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    handleGetAllNotesFromApi()
                }, { err -> var msg = err.localizedMessage
                    Log.i("DATA", msg.toString())
                })
        subscription.add(subscribe)
    }

    fun handleGetAllNotesFromApi() {
        val subscribe = getAllNotesFromApi()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                if(it != null) {
                    _allNotesData.value = it
                    completedNote.clear()
                    it.map {
                        completedNote.add(it)
                    }
                    
                }else{
                    Log.i("DATA", "NULL")
                }
            }, {
                    err -> var msg = err.localizedMessage
                Log.i("DATA", msg.toString())
            })
        subscription.add(subscribe)
    }
}