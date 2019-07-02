package com.example.calendar

import android.app.ProgressDialog.show
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlinx.android.synthetic.main.activity_main.button as button1


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button1.setOnClickListener {
            addFragmentTransaction()
        }
    }


    private fun addFragmentTransaction() {
        val transaction = supportFragmentManager
        val current = Date()
        val expiry = Date().addDays(50)
        val calendarFragment = CalendarFragment.newInstance(current, expiry)
        if (!calendarFragment.isAdded) {
            calendarFragment.show(transaction, "fragment_edit_name");
        }

    }


}