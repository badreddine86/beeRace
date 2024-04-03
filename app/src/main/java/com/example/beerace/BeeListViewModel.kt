package com.example.beerace

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BeeListViewModel(private val beeRepository: BeeRepository) : ViewModel() {

    val raceDuration = mutableStateOf<RaceDurationReponse?>(null)
    val bees = mutableStateOf<List<Bee>>(emptyList())
    val errorMessage = mutableStateOf<String?>(null)

    init {
        startTimer()
    }

    private fun startTimer() {
        viewModelScope.launch {
            while (true) {
                fetchRaceDurationAndBees()
                delay(5000)
            }
        }
    }

    private fun fetchRaceDurationAndBees() {
        viewModelScope.launch {
            beeRepository.getDuration(
                onSuccess = { fetchedRaceDuration ->
                    raceDuration.value = fetchedRaceDuration
                },
                onFailure = { error ->
                    errorMessage.value = error
                    Log.i("get error", error)
                }
            )

            beeRepository.getBees(
                onSuccess = { fetchedBees ->
                    bees.value = fetchedBees
                },
                onFailure = { error ->
                    errorMessage.value = error
                    Log.i("get error", error)
                }
            )
        }


    }
}
