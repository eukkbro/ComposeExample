@file:OptIn(ExperimentalMaterial3Api::class)

package abled.semina.composeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import abled.semina.composeexample.ui.theme.ComposeExampleTheme
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import java.time.LocalDate
import java.time.YearMonth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {

            }
        }
    }
}


//날짜 칸
@Composable
fun DayCell(
    date: LocalDate,
    isSelected: Boolean,
    isToday: Boolean,
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
            color = if (isSelected) Color.White else Color.Black
        )
    }
}


//미리보기
@Preview(showBackground = true)
@Composable
fun DayCellPreview() {
    ComposeExampleTheme {
        DayCell(
            date = LocalDate.now(),  // 미리보기 날짜
            isSelected = false,      // 선택 여부
            isToday = true,          // 오늘 여부
            onClick = {}             // 빈 클릭 이벤트
        )
    }
}


//달력
@Composable
fun Calendar() {
    val currentMonth = YearMonth.now()
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // 요일 헤더
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf("일", "월", "화", "수", "목", "금", "토").forEach { day ->
                Text(text = day, textAlign = TextAlign.Center, modifier = Modifier.weight(1f))
            }
        }

        // 날짜 셀
        val firstDayOfMonth = currentMonth.atDay(1)
        val daysInMonth = currentMonth.lengthOfMonth()
        val startOffset = firstDayOfMonth.dayOfWeek.value % 7

        LazyColumn {
            items((0 until (daysInMonth + startOffset)).chunked(7)) { week ->
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    week.forEach { index ->
                        val day = index - startOffset + 1
                        if (day > 0 && day <= daysInMonth) {
                            val date = currentMonth.atDay(day)
                            DayCell(
                                date = date,
                                isSelected = date == selectedDate,
                                isToday = date == currentDate,
                                onClick = { selectedDate = date }
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}


//달력 미리보기
@Preview(showBackground = true)
@Composable
fun CalendarPreview() {
    ComposeExampleTheme {
        Calendar()
    }
}

