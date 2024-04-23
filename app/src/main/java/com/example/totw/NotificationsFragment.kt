package com.example.totw

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider

class NotificationsFragment : Fragment() {
    private lateinit var notifSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedViewModel: SharedViewModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)
        notifSwitch = view.findViewById<Switch>(R.id.enableSwitch)
        val generalSwitch = view.findViewById<Switch>(R.id.generalSwitch)
        val opinionSwitch = view.findViewById<Switch>(R.id.opinionSwitch)
        val environSwitch = view.findViewById<Switch>(R.id.environSwitch)
        val sportsSwitch = view.findViewById<Switch>(R.id.sportsSwitch)
        sharedPreferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]
        notifSwitch.isChecked = sharedPreferences.getBoolean("switchState", false)

        notifSwitch.setOnCheckedChangeListener{_, isChecked ->
            sharedPreferences.edit().putBoolean("switchState", isChecked).apply()
            if(isChecked)
                Notification.scheduleNotification(requireContext())
            else{

            }
        }
        generalSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.setSwitchState("general", isChecked)
        }
        opinionSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.setSwitchState("opinion", isChecked)
        }
        environSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.setSwitchState("environment", isChecked)
        }
        sportsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedViewModel.setSwitchState("sports", isChecked)
        }
        return view
    }

}

