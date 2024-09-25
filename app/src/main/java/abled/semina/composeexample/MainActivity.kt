package abled.semina.composeexample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import abled.semina.composeexample.ui.theme.ComposeExampleTheme
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {
                // A surface container using the 'background' color from the theme
               MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

// 구성 가능한 함수
// 함수가 내부에서 다른 @Composable을 호출할 수 있다.
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
Surface(color = MaterialTheme.colorScheme.primary){
    Text(
        text = "Hello $name!",
        modifier = modifier.padding(24.dp)
    )
}
}

@Composable
fun MyApp(modifier: Modifier = Modifier){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ){
        Greeting("Hello")
    }
}

//미리보기
@Preview(showBackground = true, name = "Text preview")
@Composable
fun GreetingPreview() {
    ComposeExampleTheme {
        MyApp()
    }
}