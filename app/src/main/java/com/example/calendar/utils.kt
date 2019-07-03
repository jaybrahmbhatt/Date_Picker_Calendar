package com.example.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import java.util.*

fun createFullScreenDialog(context:Context?):Dialog?{
    
    val dialog= context?.let { Dialog(it,R.style.Dialog_FullScreen) }
    dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)

    context?.let { ctx->
        dialog?.window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        dialog?.window?.statusBarColor=ContextCompat.getColor(ctx,R.color.cerulean)
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.TOP)

    }
 
    return dialog
}

fun DialogFragment.setFullScreen(){
    dialog.window?.apply {
        val params =attributes
        params.apply {
            width= WindowManager.LayoutParams.MATCH_PARENT
            height=WindowManager.LayoutParams.MATCH_PARENT
        }
        attributes=params
    }
}

fun Date.addDays(TOTAL_DAYS:Int):Date{

    val C: Calendar = Calendar.getInstance()
    C.add(Calendar.DAY_OF_MONTH,TOTAL_DAYS)

    return C.time

}

fun Date.toCalendar(): Calendar {
    val cal = Calendar.getInstance()
    cal.time = this
    return cal
}