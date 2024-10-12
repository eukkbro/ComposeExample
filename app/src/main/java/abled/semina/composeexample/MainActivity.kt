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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {
               SearchBar()
            }
        }
    }
}


// 검색창
@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = " ",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,  // 포커스된 상태에서 밑줄을 투명하게 설정
            unfocusedIndicatorColor = Color.Transparent // 포커스되지 않은 상태에서도 밑줄을 투명하게 설정
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}


@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    ComposeExampleTheme {
        SearchBar()
    }
}

@Composable
fun AlignYourBodyElement(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(drawable),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
        )
        Text(
            text = stringResource(text),
            modifier = Modifier.paddingFromBaseline(top = 24.dp, bottom = 8.dp),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyElementPreview() {
    ComposeExampleTheme {
        AlignYourBodyElement(
            text = R.string.ab1_inversion,
            drawable = R.drawable.ab1_inversions,
            modifier = Modifier.padding(8.dp)
        )
    }
}


@Composable
fun FavoriteCollectionCard(
    @DrawableRes drawable: Int,
    @StringRes text: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.width(255.dp)
        ) {
            Image(
                painter = painterResource(drawable),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(80.dp)
            )
            Text(text = stringResource(text),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun FavoriteCollectionCardPreview() {
    ComposeExampleTheme {
        FavoriteCollectionCard(
            drawable = R.drawable.fc2_nature_meditations,
            text = R.string.fc2_nature_meditation,
            modifier = Modifier.padding(8.dp)
        )
    }
}


data class AlignYourBodyItem(val drawable: Int, val text: Int)

@Composable
fun AlignYourBodyRow(
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {

        //item 리스트 형태로 넣어야 되는데 공부좀 해야될듯.
        val alignYourBodyData = listOf(
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion),
            AlignYourBodyItem(R.drawable.ab1_inversions, R.string.ab1_inversion)
        )

        items(alignYourBodyData) { item ->
            AlignYourBodyElement(item.drawable, item.text)
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun AlignYourBodyRowPreview() {
    ComposeExampleTheme {
        AlignYourBodyRow()
    }
}

@Composable
fun FavoriteCollectionsGrid(
    modifier: Modifier = Modifier
){
    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.height(168.dp)
    ){
        val favoriteCollectionsData = listOf(
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
            AlignYourBodyItem(R.drawable.fc2_nature_meditations, R.string.fc2_nature_meditation),
        )
        items(favoriteCollectionsData){ item ->
            FavoriteCollectionCard(drawable = item.drawable, text = item.text, Modifier.height(80.dp))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun FavoriteCollectionsGridPreview() {
    ComposeExampleTheme {
        FavoriteCollectionsGrid()
    }
}


@Composable
fun HomeSection(
    @StringRes title: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier) {
        Text(
            text = stringResource(title)
        )
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeSectionPreview() {
    ComposeExampleTheme {
        HomeSection(R.string.align_your_body) {
            AlignYourBodyRow()
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier){

    Column(modifier.verticalScroll(rememberScrollState())) {
        Spacer(modifier = Modifier.height(16.dp))
        SearchBar(Modifier.padding(horizontal = 16.dp))
        HomeSection(title = R.string.align_your_body) {
            AlignYourBodyRow()
        }
        HomeSection(title = R.string.favorite_collections) {
            FavoriteCollectionsGrid()
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun HomeScreenPreview(){
    ComposeExampleTheme {
        HomeScreen()
    }
}

@Composable
private fun SootheBottomNavigation(modifier: Modifier = Modifier){
    NavigationBar(
        modifier = modifier
    ) {
        //선택된 아이템의 인덱스를 상태로 관리
        var selectedIndex by remember { mutableStateOf(0) }


        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Spa,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_home)
                )
            },
            selected = selectedIndex == 0,
            onClick = {
                selectedIndex = 0
            }
            )
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null
                )
            },
            label = {
                Text(
                    text = stringResource(R.string.bottom_navigation_profile)
                )
            },
            selected = selectedIndex == 1,
            onClick = {
                selectedIndex = 1
            }
        )
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun SootheBottomNavigationPreview(){
    ComposeExampleTheme {
        SootheBottomNavigation()
    }
}

@Composable
fun MySootheAppPortrait() {
    ComposeExampleTheme {
        Scaffold(
            bottomBar = { SootheBottomNavigation() }
        ) { padding ->
            HomeScreen(Modifier.padding(padding))
        }
    }
}


@Preview(showBackground = true, backgroundColor = 0xFFF5F0EE)
@Composable
fun MySootheAppPortraitPreview(){
    ComposeExampleTheme {
        MySootheAppPortrait()
    }
}



