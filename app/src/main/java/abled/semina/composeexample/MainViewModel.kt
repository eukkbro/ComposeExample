package abled.semina.composeexample

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.YearMonth

class MainViewModel : ViewModel() {

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    // 이전 달로 이동
    fun goToPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    // 다음 달로 이동
    fun goToNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

}