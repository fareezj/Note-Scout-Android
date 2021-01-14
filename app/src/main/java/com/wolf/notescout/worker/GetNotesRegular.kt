package com.wolf.notescout.worker

import android.app.Application
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wolf.notescout.MainApp.Companion.application
import com.wolf.notescout.ui.dashboard.NoteViewModel
import com.wolf.notescout.util.SharedPreferencesUtil
import timber.log.Timber

class GetNotesRegular (context: Context, params: WorkerParameters): Worker(context, params) {

    private val viewModel = NoteViewModel(application)

    override fun doWork(): Result {
        return try {
            viewModel.handleGetNotesByGroupId(SharedPreferencesUtil.groupId)
            Result.success()
        }catch (throwable: Throwable) {
            Timber.e(throwable, "Error applying blur")
            Result.failure()
        }
    }


}