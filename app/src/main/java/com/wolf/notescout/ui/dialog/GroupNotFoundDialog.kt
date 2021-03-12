package com.wolf.notescout.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.wolf.notescout.R
import kotlinx.android.synthetic.main.group_not_found_dialog.*

class GroupNotFoundDialog(context: Context): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.group_not_found_dialog)
        initView()
    }

    private fun initView() {

        btn_ok_group_not_found_alert.setOnClickListener {
            dismiss()
        }
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

}