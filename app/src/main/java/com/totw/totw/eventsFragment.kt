
package com.totw.totw

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.DateFormatSymbols
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class eventsFragment : Fragment() {
    private lateinit var calendarView: CalendarView
    private lateinit var selectedDateText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectedDateText = view.findViewById(R.id.selectedDateText)
        calendarView = view.findViewById(R.id.calendarView)
        val currentDate = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
        val dateString = dateFormat.format(currentDate)

        selectedDateText.text = dateString

        calendarView.setOnDateChangeListener{ view, year, month, dayOfMonth ->
            val selected = Calendar.getInstance()
            selected.set(year, month, dayOfMonth)
            val monthName = DateFormatSymbols().months[month]
            val selectedDate = "$monthName $dayOfMonth, $year"
            selectedDateText.text = selectedDate

        }
    }

}
