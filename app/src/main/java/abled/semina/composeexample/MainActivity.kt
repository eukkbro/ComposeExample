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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import java.time.DayOfWeek
import java.time.LocalDate


class MainActivity : ComponentActivity() {

    //뷰모델 늦은 초기화
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            setVariables()

            //네비컨트롤러 초기화
            val navController = rememberNavController()
            AppNavHost(navController = navController)

            ComposeExampleTheme {

                //메인화면
                MainScreen(navController = navController, viewModel = viewModel)

            }


        }
    }



    //변수 초기화
    private fun setVariables(){

        //뷰모델 초기화
        viewModel = MainViewModel()

    } // setReference()

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


//네비
@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController, startDestination = "home") {
        composable("home") { MainScreen(navController) }
        composable("detail") { DetailScreen() }
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

    val startOffset = 1000 // 스크롤 범위를 크게 설정 (10000일 전후)

    //왼쪽이 이전날짜가 오도록 리스트 뒤집기
    val infiniteDates = (-startOffset .. startOffset).map { offset ->
        today.minusDays(offset.toLong()) // 왼쪽(과거)부터 오른쪽(미래)까지 날짜 나열
    }.reversed() // 리스트를 반대로 뒤집어서 왼쪽이 과거, 오른쪽이 미래가 되도록

    // 오늘 날짜가 리스트의 첫 번째 인덱스에 오도록 설정
    val todayIndex = infiniteDates.indexOf(today)
    val lazyListState = rememberLazyListState(initialFirstVisibleItemIndex = todayIndex)

    Column(
        modifier = Modifier.fillMaxWidth()
    ){

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            horizontalArrangement = Arrangement.spacedBy(8.dp), // 각 날짜 셀 사이 간격
            contentPadding = PaddingValues(horizontal = 8.dp) // 좌우 여백
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



//달력 다이얼로그
@Composable
fun CalendarDialog(
    viewModel: MainViewModel,
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit
){

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
fun MainScreen(navController: NavHostController, viewModel: MainViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {

    var showDialog by remember { mutableStateOf(false) }

    // selectedDate 상태를 상위에서 관리
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }

    //비동기 처리 변수
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
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
                // 선택된 날짜의 년월을 표시
                Text(
                    text = "${selectedDate.year}년 ${selectedDate.monthValue}월",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )

                // 한줄 달력에서 날짜 클릭 시 selectedDate 업데이트
                OneLineCalendar(selectedDate = selectedDate, today = LocalDate.now()) { date ->
                    selectedDate = date
                }

                //비동기 처리상태
                Text(text = uiState)
                Button(onClick = {navController.navigate("detail")}) {
                    Text(text = "상세 화면으로 이동")
                }
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

} //MainScreen()


// 자세히 보기 화면
@Composable
fun DetailScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("상세 화면입니다.")
    }
} // DetailScreen()


//상단바 미리보기
@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ComposeExampleTheme {
        val naviController = rememberNavController()
        AppNavHost(navController = naviController)

        MainScreen(naviController, MainViewModel())
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

