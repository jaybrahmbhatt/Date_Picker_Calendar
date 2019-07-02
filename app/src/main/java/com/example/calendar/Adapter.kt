package com.example.calendar

import android.view.LayoutInflater
import com.squareup.timessquare.CalendarCellView
import com.squareup.timessquare.DayViewAdapter
import kotlinx.android.synthetic.main.day_view.view.*


class CustomDayViewAdapter:DayViewAdapter{
    override fun makeCellView(parent: CalendarCellView?) {
        val layout = LayoutInflater.from(parent?.context).inflate(R.layout.day_view,null)
        parent?.addView(layout)
        parent?.dayOfMonthTextView=layout.day_view
    }
}