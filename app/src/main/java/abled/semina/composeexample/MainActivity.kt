@file:OptIn(ExperimentalMaterial3Api::class)

package abled.semina.composeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import abled.semina.composeexample.ui.theme.ComposeExampleTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import java.time.DayOfWeek
import java.time.LocalDate


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {
                CalendarScreen()
            }
        }
    }
}


//달력 구성
@Composable
fun CalendarScreen(viewModel: MainViewModel = MainViewModel()) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.goToPreviousMonth() }) {
                Text("<") // 화살표 아이콘으로 대체 가능
            }
            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                style = MaterialTheme.typography.titleLarge
            )
            IconButton(onClick = { viewModel.goToNextMonth() }) {
                Text(">") // 화살표 아이콘으로 대체 가능
            }
        }

        // 요일 헤더
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEachIndexed { index, day ->
                val color = when (index) {
                    0 -> Color.Red    // 일요일
                    6 -> Color.Blue   // 토요일
                    else -> Color.Black
                }
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    color = color,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 날짜 셀
        val firstDayOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val startOffset = firstDayOfMonth.dayOfWeek.value % 7
        val totalCells = daysInMonth + startOffset
        val rows = (totalCells + 6) / 7 // 전체 셀을 7로 나누어 행 수 계산

        LazyColumn {
            items(rows) { rowIndex ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    for (columnIndex in 0..6) {
                        val cellIndex = rowIndex * 7 + columnIndex
                        if (cellIndex >= startOffset && cellIndex < daysInMonth + startOffset) {
                            val day = cellIndex - startOffset + 1
                            val date = currentMonth.atDay(day)
                            val dayOfWeek = date.dayOfWeek

                            DayCell(
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = date == currentDate,
                                onClick = { selectedDate = date },
                                dayColor = when (dayOfWeek) {
                                    DayOfWeek.SUNDAY -> Color.Red
                                    DayOfWeek.SATURDAY -> Color.Blue
                                    else -> Color.Black
                                }
                            )
                        } else {
                            // 빈 칸 추가
                            Spacer(modifier = Modifier.size(40.dp))
                        }
                    }
                }
            }
        }
    }
}


//달력의 한칸
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
    dayColor: Color,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(40.dp)
            .background(
                color = when {
                    isSelected -> Color.Blue
                    isToday -> Color.LightGray
                    else -> Color.Transparent
                },
                shape = CircleShape
            )
            .clickable { onClick() }
    ) {
        Text(
            text = "${date.dayOfMonth}",
            color = if (isSelected) Color.White else dayColor
        )
    }
}


//달력 미리보기
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    ComposeExampleTheme {
        CalendarScreen()
    }
}

