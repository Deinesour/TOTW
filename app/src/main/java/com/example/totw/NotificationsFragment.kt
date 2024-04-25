package com.example.totw

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar
const val channelID = "channel1"
class NotificationsFragment : Fragment() {
    private lateinit var notifSwitch: Switch
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var alarmManager: AlarmManager
    private lateinit var notificationManager: NotificationManager
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
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize AlarmManager and NotificationManager
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create the notification channel
        createNotificationChannel()

        if (notifSwitch.isChecked){
            if (checkNotificationPermissions(requireContext())) {
                // Schedule a notification
                scheduleNotification()
            }
        }

    }
    fun checkNotificationPermissions(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val isEnabled = notificationManager.areNotificationsEnabled()
            if (!isEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                requireContext().startActivity(intent)
                return false
            }
        } else {
                val areEnabled =
                    NotificationManagerCompat.from(requireContext()).areNotificationsEnabled()

                if (!areEnabled) {
                    // Open the app notification settings if notifications are not enabled
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                    requireContext().startActivity(intent)
                    return false
                }
            }
        return true
    }
        @RequiresApi(Build.VERSION_CODES.O)
        private fun createNotificationChannel() {
            val name = "General"
            val descriptionText = "General notifications from TOP."
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification() {

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY)
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE,0)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(requireContext(), Notification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}




