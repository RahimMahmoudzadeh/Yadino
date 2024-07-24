package com.rahim.ui.alarmHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahim.data.modle.Rotin.Routine
import com.rahim.data.repository.routine.RepositoryRoutine
import com.rahim.utils.enums.error.ErrorMessageCode
import com.rahim.utils.resours.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val routineRepository: RepositoryRoutine,
) : ViewModel() {
    private var _flowRoutines = MutableStateFlow<Resource<List<Routine>>>(Resource.Loading())
    val flowRoutines: StateFlow<Resource<List<Routine>>> = _flowRoutines
    val haveAlarm :Flow<Boolean> = routineRepository.haveAlarm()


    fun getAllRoutine() {
        viewModelScope.launch {
            runCatching {
                routineRepository.getAllRoutine()
            }.onSuccess {
                _flowRoutines.value = Resource.Success(it)
            }.onFailure {
                _flowRoutines.value = Resource.Error(ErrorMessageCode.ERROR_GET_PROCESS)
            }
        }
    }


}