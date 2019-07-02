package com.example.calendar

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment

open class FullScreenDialogFragment:DialogFragment(){

    protected open fun onBackPressed(){
        dismiss()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val dialog = createFullScreenDialog(activity)
        dialog!!.setOnKeyListener { _, keyCode, event -> //replaced with non-null asserted
            if (keyCode== KeyEvent.KEYCODE_BACK){
                onBackPressed()
            }
            true
        }
        return dialog

    }

    override fun onResume() {
        setFullScreen()
        super.onResume()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        val activity =activity
        if (activity is DialogInterface.OnDismissListener){
            (activity as DialogInterface.OnDismissListener).onDismiss(dialog)
        }
    }

}