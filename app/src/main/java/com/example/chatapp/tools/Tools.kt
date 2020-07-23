package com.example.chatapp.tools

import android.app.Dialog
import android.content.Context
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.example.chatapp.R
import kotlinx.android.synthetic.main.dialog_layout.*

object Tools {
    fun initDialog(context: Context, title: String, description: String) {
        val dialog = Dialog(context)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout)

        val params: WindowManager.LayoutParams = dialog.window!!.attributes
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog.window!!.attributes = params
        dialog.dialogTitle.text = title
        dialog.dialogDescription.text = description
        dialog.okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()

    }
}