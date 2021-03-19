package com.wolf.notescout.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.wolf.notescout.R
import kotlinx.android.synthetic.main.existing_group_dialog.*

class ExistingGroupDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.existing_group_dialog)
        initView()
    }

    private fun initView() {

        btn_ok_existing_group_alert.setOnClickListener {
            dismiss()
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}