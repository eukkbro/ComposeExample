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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
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
fun Greeting(name: String, modifier: Modifier = Modifier) {\

    var expanded = false
    
Surface(color = MaterialTheme.colorScheme.primary,
modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)){
    Row(modifier = Modifier.padding(24.dp)){
        Column(modifier = Modifier.weight(1f)) {
            Text(text = "Hello")
            Text(text = name)
        }
        ElevatedButton(
            onClick = { expanded = !expanded }
        ){
            Text("Show more")
        }
    }

}
}

@Composable
fun MyApp(modifier: Modifier = Modifier,
names: List<String> = listOf("World", "Compose")
){
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background
    ){
        Column(modifier = modifier.padding(vertical = 4.dp)) {
            for(name in names){
                Greeting(name = name)
            }
        }
    }
}

//미리보기
@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    ComposeExampleTheme {
        MyApp()
    }
}