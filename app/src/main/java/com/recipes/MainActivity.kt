package com.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.model.Recipe
import com.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecipesTheme {
                RecipesUI(DataSource.recipes)
            }
        }
    }
}

@Composable
private fun DrawerContent(
    recipes: List<Recipe>,
    onRecipeClick: (it: Recipe) -> Unit,
) {
    Surface(
        modifier = Modifier
            .background(MaterialTheme.colors.background)
    ) {
        Column {
            Text(
                "Recipes",
                style = MaterialTheme.typography.h1,
                modifier = Modifier.padding(5.dp)
            )
            Divider(thickness = 2.dp, color = MaterialTheme.colors.onBackground)
            RecipeList(recipes, onRecipeClick) /* TODO also hide drawer */
        }
    }
}

@Composable
private fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    recipe: Recipe?,
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
                text = recipe?.title ?: stringResource(R.string.app_name),
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
private fun BottomNavigation(
    scope: CoroutineScope,
    lazyListState: LazyListState,
) {
    BottomNavigation {
        BottomNavigationItem(
            selected = true,
            onClick = { /* TODO */ },
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
fun RecipesUI(recipes: List<Recipe>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val lazyListState = rememberLazyListState()
        var recipe: Recipe? by remember { mutableStateOf(null) }

        Scaffold(
            topBar = { TopBar(scope, scaffoldState, recipe) },
            drawerContent = { DrawerContent(recipes) { recipe = it } },
            bottomBar = { BottomNavigation(scope, lazyListState) },
            scaffoldState = scaffoldState,
        ) {
            Surface(
                modifier = Modifier
                    .padding(it)
                    .padding(8.dp)
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
            ) {
                RecipeUI(recipe)
            }
        }
    }
}

@Composable
fun RecipeUI(recipe: Recipe?) {
    if (recipe == null) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize(),
        ) {


            Text("No recipe selected", style = MaterialTheme.typography.h2)
        }
        return
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        recipe.tasks.map {
            item { Text(it.description) }
        }
    }
}

@Composable
private fun RecipeList(
    recipes: List<Recipe>,
    onItemClick: (recipe: Recipe) -> Unit,
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        recipes.map {
            item { RecipeItem(it, onItemClick) }
        }
    }
}

@Composable
private fun RecipeItem(
    recipe: Recipe,
    onClick: (it: Recipe) -> Unit,
) {
    Card(
        elevation = 5.dp,
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        Button(
            modifier = Modifier
                .padding(10.dp),
            onClick = { onClick(recipe) }
        ) {
            Text(text = recipe.title, style = MaterialTheme.typography.body1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    RecipesTheme(darkTheme = true) {
        DrawerContent(DataSource.recipes, {})
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecipesTheme(darkTheme = true) {
        RecipesUI(DataSource.recipes)
    }
}