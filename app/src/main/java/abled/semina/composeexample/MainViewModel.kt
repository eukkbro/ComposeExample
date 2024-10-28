package abled.semina.composeexample

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.YearMonth

class MainViewModel : ViewModel() {

    // 상태를 관리할 StateFlow
    private val _uiState = MutableStateFlow("초기 상태")
    val uiState: StateFlow<String> = _uiState

    private val _currentMonth = MutableStateFlow(YearMonth.now())
    val currentMonth: StateFlow<YearMonth> = _currentMonth

    init {
        fetchData() // 데이터 비동기 요청
    }

    // 비동기 작업 (예: 네트워크 요청할수도)
    private fun fetchData() {
        viewModelScope.launch {
            // 예시: 비동기 작업 처리 후 상태 업데이트
            delay(2000)
            _uiState.value = "업데이트된 상태"
        }
    }

    // 이전 달로 이동
    fun goToPreviousMonth() {
        _currentMonth.value = _currentMonth.value.minusMonths(1)
    }

    // 다음 달로 이동
    fun goToNextMonth() {
        _currentMonth.value = _currentMonth.value.plusMonths(1)
    }

}