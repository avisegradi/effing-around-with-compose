package com.example.myapplication5

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication5.ui.theme.MyApplication5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val IconSet = Icons.Filled

@Composable
fun Oof(navController: NavHostController, steps: List<Step>) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val lazyListState = rememberLazyListState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold(
            topBar = { TopBar(scope, scaffoldState) },
            drawerContent = { DrawerContent(scope, scaffoldState) },
            bottomBar = { BottomNavigation(navController, scope, lazyListState) },
            scaffoldState = scaffoldState,
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.Start,
                    state = lazyListState,
                    modifier = Modifier.background(MaterialTheme.colors.background)
                ) {
                    steps.forEach {
                        item {
                            StepItem(it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StepItem(step: Step) {
    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(12)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
                .background(MaterialTheme.colors.surface),
        ) {
            Text(
                text = step.title,
                overflow = TextOverflow.Ellipsis,
                softWrap = false,
                style = MaterialTheme.typography.h2,
                modifier = Modifier.padding(5.dp)
            )
            Text(
                text = step.description,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun BottomNavigation(
    navController: NavHostController,
    scope: CoroutineScope,
    lazyListState: LazyListState,
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = true,
            onClick = { navController.popBackStack() },
            icon = { Icon(IconSet.ArrowBack, "Back") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(25, 0)
                }
            },
            icon = { Icon(IconSet.Home, "GO HOME") }
        )
    }
}

@Composable
fun DrawerContent(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
        Icon(IconSet.ArrowBack, "Close drawer")
    }
}

@Composable
private fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
) {
    TopAppBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Button(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(IconSet.Menu, "Open drawer")
            }
            Spacer(modifier = Modifier.weight(1f))
            Image(
                painterResource(R.mipmap.ic_launcher),
                contentDescription = null,
            )
        }
    }
}

@Composable
fun MyPreview(dark: Boolean) {
    val navController = rememberNavController()

    val a = (1..100).filter { it % 2 == 0 }
    MyApplication5Theme(darkTheme = dark) {
        Oof(
            navController,
            steps = a.map {
                Step(it.toString())
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Default2Preview() {
    MyPreview(dark = false)
}

@Preview(showBackground = true)
@Composable
fun Default3Preview() {
    MyPreview(dark = true)
}