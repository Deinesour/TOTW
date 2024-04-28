package com.totw.totw

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _switchStates = MutableLiveData<Map<String, Boolean>>()
    val switchStates: LiveData<Map<String, Boolean>> = _switchStates

    init {
        // Initialize the map with default values (all switches unchecked)
        val initialSwitchStates = mutableMapOf<String, Boolean>()
        initialSwitchStates["general"] = true
        initialSwitchStates["opinion"] = false
        initialSwitchStates["environment"] = false
        initialSwitchStates["sports"] = false
        _switchStates.value = initialSwitchStates
    }

    fun setSwitchState(switchId: String, isChecked: Boolean) {
        val currentStates = _switchStates.value?.toMutableMap() ?: mutableMapOf()
        currentStates[switchId] = isChecked
        _switchStates.value = currentStates
    }

    fun getSwitchState(switchId: String): Boolean {
        val currentStates = _switchStates.value?.toMutableMap() ?: mutableMapOf()
        if (currentStates[switchId] == null) {
            return false
        }
        return currentStates[switchId]!!
    }
}