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
    private lateinit var sharedPreferencesNotif: SharedPreferences
    private lateinit var sharedPreferencesGen: SharedPreferences
    private lateinit var sharedPreferencesOpinion: SharedPreferences
    private lateinit var sharedPreferencesEnviron: SharedPreferences
    private lateinit var sharedPreferencesSports: SharedPreferences
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

        sharedPreferencesNotif = requireContext().getSharedPreferences("switchState", Context.MODE_PRIVATE)
        sharedPreferencesGen = requireContext().getSharedPreferences("generalSwitchState", Context.MODE_PRIVATE)
        sharedPreferencesOpinion = requireContext().getSharedPreferences("opinionSwitchState", Context.MODE_PRIVATE)
        sharedPreferencesEnviron = requireContext().getSharedPreferences("environSwitchState", Context.MODE_PRIVATE)
        sharedPreferencesSports = requireContext().getSharedPreferences("sportsSwitchState", Context.MODE_PRIVATE)
        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        notifSwitch.isChecked = sharedPreferencesNotif.getBoolean("switchState", false)
        generalSwitch.isChecked = sharedPreferencesGen.getBoolean("generalSwitchState", false)
        opinionSwitch.isChecked = sharedPreferencesOpinion.getBoolean("opinionSwitchState", false)
        environSwitch.isChecked = sharedPreferencesEnviron.getBoolean("environSwitchState", false)
        sportsSwitch.isChecked = sharedPreferencesSports.getBoolean("sportsSwitchState", false)

        notifSwitch.setOnCheckedChangeListener{_, isChecked ->
            sharedPreferencesNotif.edit().putBoolean("switchState", isChecked).apply()
            if(isChecked)
                Notification.scheduleNotification(requireContext())
            else{

            }
        }
        generalSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesGen.edit().putBoolean("generalSwitchState", isChecked).apply()
            sharedViewModel.setSwitchState("general", isChecked)
        }
        opinionSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesOpinion.edit().putBoolean("opinionSwitchState", isChecked).apply()
            sharedViewModel.setSwitchState("opinion", isChecked)
        }
        environSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesEnviron.edit().putBoolean("environSwitchState", isChecked).apply()
            sharedViewModel.setSwitchState("environment", isChecked)
        }
        sportsSwitch.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferencesSports.edit().putBoolean("sportsSwitchState", isChecked).apply()
            sharedViewModel.setSwitchState("sports", isChecked)
        }
        return view
    }

}

