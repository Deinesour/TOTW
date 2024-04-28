package com.totw.totw

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
    private lateinit var generalSwitch: Switch
    private lateinit var opinionSwitch: Switch
    private lateinit var environSwitch: Switch
    private lateinit var sportsSwitch: Switch
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
        generalSwitch = view.findViewById<Switch>(R.id.generalSwitch)
        opinionSwitch = view.findViewById<Switch>(R.id.opinionSwitch)
        environSwitch = view.findViewById<Switch>(R.id.environSwitch)
        sportsSwitch = view.findViewById<Switch>(R.id.sportsSwitch)

        sharedPreferences = requireContext().getSharedPreferences("SwitchState", Context.MODE_PRIVATE)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        notifSwitch.isChecked = sharedPreferences.getBoolean("switchState", false)
        generalSwitch.isChecked = sharedViewModel.getSwitchState("general")
        opinionSwitch.isChecked = sharedViewModel.getSwitchState("opinion")
        environSwitch.isChecked = sharedViewModel.getSwitchState("environment")
        sportsSwitch.isChecked = sharedViewModel.getSwitchState("sports")


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

