package com.totw.totw

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
import com.totw.totw.channelID
import java.util.Calendar
const val channelID = "channel1"
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
    @SuppressLint("NewApi")
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
    @RequiresApi(Build.VERSION_CODES.S)
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

        val isAlarmEnabled = alarmManager.canScheduleExactAlarms()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isAlarmEnabled) {
                // Open the app notification settings if notifications are not enabled
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().packageName)
                requireContext().startActivity(intent)
                return false
            }
        }else{
            return false
        }
        return true
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "General"
        val descriptionText = "Notifications from TOP."
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
            set(Calendar.HOUR_OF_DAY,9)
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

