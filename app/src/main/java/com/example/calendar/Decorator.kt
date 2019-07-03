package com.example.calendar

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import com.squareup.timessquare.CalendarCellDecorator
import com.squareup.timessquare.CalendarCellView
import com.squareup.timessquare.MonthView
import java.util.Calendar
import java.util.Date






class CellDecorator(start:Date, end:Date, min:Date, max: Date, context: Context):CalendarCellDecorator{

    val min = min.toCalendar()
    val max = max.toCalendar()

    val minDay = this.min.get(Calendar.DAY_OF_YEAR)
    val maxDay = this.max.get(Calendar.DAY_OF_YEAR) - 1 // exclusivity
    val minDayYear = this.min.get(Calendar.YEAR)
    val maxDayYear = this.max.get(Calendar.YEAR)
    var startDay = 0
    var endDay = 0

    // all them drawables
    val bgDefault = ColorDrawable(Color.WHITE)
    val bgBegin = ContextCompat.getDrawable(context, R.drawable.custom_date_background_begin) as Drawable
    val bgEnd = ContextCompat.getDrawable(context, R.drawable.custom_date_background_end) as Drawable
    val bgMiddle = ContextCompat.getDrawable(context, R.drawable.custom_date_background) as Drawable
    val bgCircle = ContextCompat.getDrawable(context, R.drawable.round_shape) as Drawable

    init {
        updateSelection(start,end)
    }

    override fun decorate(cellView: CalendarCellView, date: Date?) {

        val current= Date().toCalendar()
        val (currentDay,currentDayYear,currentDayOfMonth,lastDayOfMonth)=current.let {
            listOf(it.get(Calendar.DAY_OF_YEAR),
                it.get(Calendar.YEAR),
                it.get(Calendar.DAY_OF_MONTH),
                it.getActualMaximum(Calendar.DAY_OF_MONTH)

            )
        }

        //set text color
        val textColorRes = getTextColorRes(cellView ,currentDay,currentDayYear)
        val textcolor = getColor(cellView.context,textColorRes)
        cellView.dayOfMonthTextView.setTextColor(textcolor)
        cellView.dayOfMonthTextView.setTextSize(15.0F)


        //set background
        val drawable =getBackground(cellView, currentDay, currentDayOfMonth,lastDayOfMonth)
        cellView.background=drawable

    }
    fun updateSelection(start: Date,end: Date){
        startDay= start.toCalendar().get(Calendar.DAY_OF_YEAR)
        endDay=end.toCalendar().get(Calendar.DAY_OF_YEAR)

    }
    private fun getTextColorRes(cellView: CalendarCellView,currentDay: Int,currentDayYear:Int):Int{
        return when{
            !cellView.isCurrentMonth->android.R.color.transparent
            cellView.isSelected->R.color.mine_shaft
            (currentDay < minDay&& currentDayYear==minDayYear )||(currentDay > maxDay&& currentDayYear==maxDayYear)->R.color.splitter_grey
            else->R.color.mine_shaft

        }

    }
    private fun getBackground(cellView: CalendarCellView,currentDay: Int, currentDayOfMonth:Int,lastDayOfMonth:Int):Drawable{
        if (!cellView.isCurrentMonth ||!cellView.isSelected){
            return bgDefault
        }

        return when (currentDay){
            startDay->when (startDay){
                endDay->bgCircle
               lastDayOfMonth->bgCircle
                else->bgEnd
            }
            endDay->when(currentDayOfMonth){
                1->bgCircle
                else->bgEnd
            }
            else->when(currentDayOfMonth){
                1->bgBegin
                lastDayOfMonth->bgEnd
                else->bgMiddle
            }
        }
    }

}

