package com.example.myapplication5

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication5.ui.theme.MyApplication5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val IconSet = Icons.TwoTone

@Composable
fun Oof(navController: NavHostController, steps: List<Step>) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Scaffold(
            topBar = { TopBar(scope, scaffoldState) },
            drawerContent = { DrawerContent(scope, scaffoldState) },
            bottomBar = { BottomNavigation(navController) },
            scaffoldState = scaffoldState,
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),

                ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.verticalScroll(state = scrollState)
                ) {
                    steps.forEach {
                        Text(
                            text = it.title,
                            fontSize = 64.sp,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomNavigation(navController: NavHostController) {
    BottomNavigation {
        BottomNavigationItem(
            selected = true,
            onClick = { navController.popBackStack() },
            icon = { Icon(IconSet.ArrowBack, "Back") }
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
        Row {
            Button(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(IconSet.Menu, "Open drawer")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Default2Preview() {
    val navController = rememberNavController()
    MyApplication5Theme {
        Oof(
            navController,
            steps =  (1..50).map {
                Step("${it.toString()} Oooof")
            }
        )
    }
}