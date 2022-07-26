package com.example.myapplication5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.materialIcon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDirections
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication5.ui.theme.MyApplication5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Oof(navController: NavHostController) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberScaffoldState()

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
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {


                    Text(
                        text = "Oooof",
                        fontSize = 64.sp,
                    )
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
            icon = { Icon(Icons.Filled.ArrowBack, "Back") }
        )
    }
}

@Composable
fun DrawerContent(scope: CoroutineScope, scaffoldState: ScaffoldState) {
    Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
        Icon(Icons.Filled.ArrowBack, "Close drawer")
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
                Icon(Icons.Filled.Menu, "Open drawer")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Default2Preview() {
    val navController = rememberNavController()
    MyApplication5Theme {
        Oof(navController)
    }
}