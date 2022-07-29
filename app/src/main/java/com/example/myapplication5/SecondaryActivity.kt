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
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.myapplication5.ui.theme.MyApplication5Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

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
private fun DetailsButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            tint = MaterialTheme.colors.secondary,
            contentDescription = stringResource(R.string.expand_button_description),
        )
    }
}

@Composable
private fun StepDetails(step: Step) {
    Column() {
        Text(text = step.detailsPart1, style = MaterialTheme.typography.h3)
        Text(text = step.detailsPart2, style = MaterialTheme.typography.body1)
    }
}

@Composable
private fun StepItem(step: Step) {
    var expanded by remember { mutableStateOf(false) }
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
            Row() {
                Text(
                    text = step.title,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false,
                    style = MaterialTheme.typography.h2,
                    modifier = Modifier.padding(5.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                DetailsButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            Text(
                text = step.description,
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth(),
            )
            StepDetails(step)
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
            icon = { Icon(Icons.Filled.ArrowBack, "Back") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(25, 0)
                }
            },
            icon = { Icon(Icons.Filled.Home, "GO HOME") }
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.primary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = {
                    scope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(Icons.Filled.Menu, "Open drawer")
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Oof",
                style = MaterialTheme.typography.h1
            )
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