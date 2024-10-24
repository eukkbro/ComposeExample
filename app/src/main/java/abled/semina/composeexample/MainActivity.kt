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
import android.annotation.SuppressLint
import android.graphics.Paint.Align
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.constraintlayout.widget.ConstraintLayout
import java.time.DayOfWeek
import java.time.LocalDate


class MainActivity : ComponentActivity() {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel()

        setContent {
            ComposeExampleTheme {
                Calendar(viewModel)
            }
        }
    }
}


//달력 구성
@Composable
fun Calendar(viewModel: MainViewModel) {

    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    Column(modifier = Modifier.padding(16.dp)) {

        Row( //첫번째줄
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            //이전 달 버튼
            IconButton(onClick = { viewModel.goToPreviousMonth() }) {
                Text("<") // 화살표 아이콘으로 대체 가능
            }

            // 선택된 날짜 표시 칸
            Text(
                text = "${currentMonth.year}년 ${currentMonth.monthValue}월",
                style = MaterialTheme.typography.titleLarge
            )

            //이후 달 버튼
            IconButton(onClick = { viewModel.goToNextMonth() }) {
                Text(">") // 화살표 아이콘으로 대체 가능
            }
        }

        // 두번째줄 - 일~토 요일 표시
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

        //세번째 줄 - 달력의 날짜들
        // 리사이클러뷰 느낌
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
//약간 리사이클러뷰의 아이템 뷰 느낌
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
                    isSelected -> Color.Blue //선택된 날짜 파랑
                    isToday -> Color.LightGray //오늘날짜 회색
                    else -> Color.Transparent // 나머지 투명
                },
                shape = CircleShape // 백그라운드 원형모양
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
        Calendar(MainViewModel())
    }
}


//한줄 달력 만들기
@Composable
fun OneLineCalendar(
    selectedDate: LocalDate,
    today: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // 무한 스크롤이므로 첫 시작점을 임의의 값으로 설정 (예: 1000일 전후로 설정)
    val startOffset = 1000

    //왼쪽이 이전날짜가 오도록 리스트 뒤집기
    val infiniteDates = (startOffset downTo -startOffset).map { offset ->
        today.minusDays(offset.toLong())
    }

    //LazyRow의 초기 스크롤 상태(4번째 아이템이 오는 날짜)
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = startOffset - 3)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp), // 각 날짜 셀 사이 간격
        contentPadding = PaddingValues(horizontal = 16.dp) // 좌우 여백
    ) {
        itemsIndexed(infiniteDates) { _, date ->
            OneLineDayCell(
                date = date,
                isSelected = date == selectedDate,
                isToday = date == today,
                dayColor = when (date.dayOfWeek) {
                    DayOfWeek.SUNDAY -> Color.Red
                    DayOfWeek.SATURDAY -> Color.Blue
                    else -> Color.Black
                },
                onClick = { onDateSelected(date) }
            )
        }
    }
}


//일간달력 셀
@Composable
fun OneLineDayCell(date: LocalDate,
                isSelected: Boolean,
                isToday: Boolean,
                dayColor: Color,
                onClick: () -> Unit){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .width(40.dp)
            .height(80.dp)
            .background(
                color = when {
                    isSelected -> Color.Blue //선택된 날짜 파랑
                    isToday -> Color.LightGray //오늘날짜 회색
                    else -> Color.Transparent // 나머지 투명
                },
                shape = CutCornerShape(5.dp) // 백그라운드 모서리 둥근모양
            )
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${ date.dayOfWeek } ".substring(0,3),
                color = if (isSelected) Color.White else dayColor
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${date.dayOfMonth}",
                color = if (isSelected) Color.White else dayColor
            )
        }
    }
}


//일주일달력 미리보기
@Preview(showBackground = true)
@Composable
fun WeekCalendarPreview() {
    ComposeExampleTheme {
        OneLineCalendar(LocalDate.now(), LocalDate.now(),{})
    }
}

//달력 다이얼로그
@Composable
fun CalendarDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
){
    val currentMonth by viewModel.currentMonth.collectAsState()
    val currentDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(modifier = Modifier.padding(8.dp)) {
                //달력 본문
                Calendar(viewModel = viewModel)
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(name: String, viewModel: MainViewModel) {

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(), // Box로 전체 공간 차지
                        contentAlignment = Alignment.Center // 제목을 중앙 정렬
                    ) {
                        Text(text = name, textAlign = TextAlign.Center)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { showDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth, //아이콘
                            contentDescription = "캘린더" //아이콘 설명
                        )
                    }
                }
            )
        },
        content = {
            paddingValues ->
            Column(modifier = Modifier.padding(paddingValues)){
                Text(text = "일간 달력 들어갈 예정")
            }
        }
    )

    // 다이얼로그 표시
    if (showDialog) {
        CalendarDialog(
            viewModel = viewModel,
            onDismiss = { showDialog = false }, // 다이얼로그 닫기
            onDateSelected = { date ->
                // 선택된 날짜 처리
                println("선택된 날짜: $date")
                showDialog = false // 날짜 선택 후 다이얼로그 닫기
            }
        )
    }

}


//상단바 미리보기
@Preview(showBackground = true)
@Composable
fun TopAppBarPreview() {
    ComposeExampleTheme {
        MainScreen("xxxx년 xx월 xx일", MainViewModel())
    }
}


//캘린더다이얼로그 미리보기
@Preview(showBackground = true)
@Composable
fun CalendarDialogPreview() {
    ComposeExampleTheme {
        CalendarDialog(MainViewModel(), {}, {})
    }
}

