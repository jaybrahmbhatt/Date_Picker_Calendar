package com.example.calendar
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.importre.unwrap.unwrap
import com.squareup.timessquare.CalendarPickerView
import kotlinx.android.synthetic.main.fragment_calendar.*
import kotlinx.android.synthetic.main.toolbar.*
import kotlinx.android.synthetic.main.fragment_calendar.tv_end_date
import kotlinx.android.synthetic.main.fragment_calendar.tv_start_date
import java.util.Date


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CalendarFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CalendarFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */

interface OnPromotionDatesUpdatedListener {
    fun promotionDatesUpdated(promotionStart: Date, promotionExpiry: Date)
}

enum class DateSelectionState {
    NONE,
    START_SELECTED,
    END_SELECTED;
}


class CalendarFragment : FullScreenDialogFragment(), CalendarPickerView.OnDateSelectedListener {


    private lateinit var decorator: CellDecorator


    private lateinit var savedExpiry:Date
    private lateinit var expiry: Date
    private lateinit var savedStart: Date
    private lateinit var start: Date


    private lateinit var selectionState: DateSelectionState

    val current = Date()

    //Have to add an extra day since Times Square has max date exclusive

    val maxDate= Date().addDays(DEFAULT_NUM_DAYS_ENABLED + 1)

    companion object {
        fun newInstance(initialStart: Date, initialExpiry: Date): CalendarFragment {
            val fragment = CalendarFragment()

            val args = Bundle()
            args.apply {
                putSerializable(INITIAL_EXPIRY, initialExpiry)
                putSerializable(INITIAL_START, initialStart)
            }
            fragment.arguments = args
            return fragment
        }

        val INITIAL_EXPIRY = "initial_expiry"
        val INITIAL_START = "initial_start"
        val TAG = "calendar_fragment"
        val TEXT_FADE_DURATION = 250L
        val DEFAULT_NUM_DAYS_ENABLED = 90 // Max scheduling 30 days
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val argExpiry = arguments?.getSerializable(INITIAL_EXPIRY) as? Date
        val argStart = arguments?.getSerializable(INITIAL_START) as? Date

       unwrap(argStart,argExpiry){ start,expiry ->
           savedStart = start
           savedExpiry= expiry

       }

        start = savedStart
        expiry = savedExpiry


        // only read argument once
        arguments?.clear()
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
    inflater.inflate(R.layout.fragment_calendar,container,false)
        // Inflate the layout for this fragment






    //Ask if you need to use  OnAttach functions like choose currency fragment





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.setNavigationOnClickListener {dismiss() }
        toolbar.setNavigationIcon(R.drawable.ic_close_icon)
        toolbar.title = resources.getString(R.string.expiry_picker_title)


        btn_save.apply {
            isClickable = true
            setOnClickListener {
                saveExpiry()
                onBackPressed()

            }
        }
        updateExpiry(savedStart, savedExpiry, initial = true)
        setUpCalendar()

    }

    private fun setUpCalendar(){
        context?.let { activity->
            decorator=CellDecorator(start,expiry,current,maxDate,activity)

        }

        square_calendar.apply {
            setCustomDayView(CustomDayViewAdapter())
            decorators= listOf(decorator)

            init(current,maxDate).inMode(CalendarPickerView.SelectionMode.RANGE)

            // set initial date selection
            // using small delay given by handler to bypass issue of text being cut off on initial selection
            post {
                selectDates(start,expiry)
            }
            selectionState= DateSelectionState.END_SELECTED

            setOnDateSelectedListener(this@CalendarFragment)
            //remove default listener which puts up a toast
            setOnInvalidDateSelectedListener(null)

        }
    }
   override fun onDateSelected(date: Date){
       val dates=square_calendar.selectedDates
       val start= dates.first()
       var end: Date?= dates.last()

       /**
        * For single day promotion, User needs to tap twice to confirm start & end dates.
        * Below code handles double tap selections.
        */
       if (dates.size == 1){
           when (selectionState){
               DateSelectionState.START_SELECTED,DateSelectionState.END_SELECTED->{
                   if (start !=this.start){
                       end=null
                       selectionState= DateSelectionState.START_SELECTED
                       setSaveButtonEnable(false)
                   }else{
                       selectionState=DateSelectionState.END_SELECTED
                       setSaveButtonEnable(true)
                   }
               }
               else->{
                   end = null
                   selectionState= DateSelectionState.START_SELECTED
                   setSaveButtonEnable(false)
               }
           }
       }else{
           selectionState=DateSelectionState.END_SELECTED
           setSaveButtonEnable(true)
       }
       decorator.updateSelection(start,end?:start)
       updateExpiry(start,end)
   }
    override fun onDateUnselected(date: Date)=Unit
    //dummy function

    private fun updateExpiry(start:Date,end:Date?,initial:Boolean=false){
        if (start!= this.start||initial){
            this.start=start
//            with(tv_start_date){
//                //animateOpacity(to=0f,duration= TEXT_FADE_DURATION)
//                {
//                    this.text=start.toCalendarExpiryFormat()
//                    animateOpacity(to=1f,duration= TEXT_FADE_DURATION)
//                }
//            }
        }
    //    var endDateText= activity?.getString(R.string.promotion_no_end_date_selection)
        if (end!= null){
      //      endDateText=end.toCalendarExpiryFormat()
            expiry=end
        }
        with(tv_end_date){
//            if (this.text!=endDateText){
//                animateOpacity(to=0f,duration= TEXT_FADE_DURATION){
//                    this.text=endDateText
//                    animateOpacity(to=0f,duration= TEXT_FADE_DURATION)
//                }
//            }
        }

    }
    /**
     * Actually save the new expiration and update the calling activity
     */
    private fun saveExpiry() {
//        TrackingUtlis.track(Tracking.Promotion.TAP_PROMOTION_SETTINGS){
//            with(Tracking){
//                OLD_VALUE prop savedExpiry.toPromotionExpiryRequest()
//                NEW_VALUE prop expiry.toPromotionExpiryRequest()
//                CHANGED prop (savedExpiry != expiry)
//            }
//        }

        savedStart = start
        savedExpiry= expiry

        val listener= activity as? OnPromotionDatesUpdatedListener
        listener?.promotionDatesUpdated(savedStart, savedExpiry)
    }
    private fun setSaveButtonEnable(enabled:Boolean){
        btn_save.apply {
            isClickable=enabled
            isEnabled=enabled
        }
    }

    fun CalendarPickerView.selectDates(start: Date,end: Date){
        clearChoices()
        selectDate(start)
        selectDate(end)
    }
}
