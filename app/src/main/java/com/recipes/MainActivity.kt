package com.recipes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.model.Ingredient
import com.recipes.model.Recipe
import com.recipes.model.Task
import com.recipes.model.TaskId
import com.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class TaskState(
    val active: Boolean = false,
    val done: Boolean = false,
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RecipesTheme {
                val recipes by remember { mutableStateOf(DataSource().recipes) }
                RecipesUI(recipes)
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
            RecipeList(recipes, onRecipeClick)
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

@Composable
private fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    recipe: Recipe?,
) {
    TopAppBar {
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
fun RecipesUI(recipes: List<Recipe>, startingRecipe: Recipe? = null) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        val scope = rememberCoroutineScope()
        val scaffoldState = rememberScaffoldState()
        val lazyListState = rememberLazyListState()
        var recipe: Recipe? by remember { mutableStateOf(startingRecipe) }
        val uiRecipes by remember { mutableStateOf(recipes) }

        Scaffold(
            topBar = { TopBar(scope, scaffoldState, recipe) },
            drawerContent = {
                DrawerContent(uiRecipes) {
                    recipe = it
                    scope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            },
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
                RecipeUI(recipe, scope)
            }
        }
    }
}

@Composable
fun RecipeUI(recipe: Recipe?, scope: CoroutineScope) {
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

    val tasks = recipe.tasks
    //TODO: maybe should be part of Recipe?
    val nextTasks = tasks.mapIndexed { index, task ->
        task.id to when (task.nextTask) {
            null -> if (index < tasks.size - 1) tasks[index + 1].id else null
            else -> task.nextTask
        }
    }.toMap()

    val taskListState = rememberLazyListState()

    var first = true
    val taskStateLookup = remember {
        mutableStateMapOf(
            *tasks.map {
                val active = first
                first = false
                it.id to TaskState(active, false)
            }.toTypedArray()
        )
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        state = taskListState,
    ) {
        item { RecipeSummary(recipe) }

        val indexLookup = mutableMapOf<TaskId, Int>()

        itemsIndexed(
            items = recipe.tasks
        ) { index, task ->
            val id = task.id
            val taskState = taskStateLookup[id]!!
            indexLookup[id] = index

            TaskCard(task, taskState) {
                scope.launch {
                    val nextItem = nextTasks[task.id]
                    taskStateLookup[id] = TaskState(false, true)
                    val nextIndex = if (nextItem != null) {
                        taskStateLookup[nextItem] = taskStateLookup[nextItem]!!.copy(active = true)
                        indexLookup[nextItem]!!
                    } else {
                        taskListState.layoutInfo.totalItemsCount - 1
                    }

                    taskListState.scrollToItem(nextIndex)
                }
            }
        }

        item {
            Text(text = "Concralluations", style = MaterialTheme.typography.h1)
        }
    }
}

@Composable
fun RecipeCard(
    background: Color = MaterialTheme.colors.background,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier
            .padding(5.dp),
        shape = RoundedCornerShape(5.dp),
        elevation = 5.dp,
    ) {
        Column(
            modifier = Modifier
                .background(background)
                .padding(10.dp)
        ) {
            content()
        }
    }
}

@Composable
fun TaskCard(
    task: Task,
    taskState: TaskState,
    onClick: () -> Unit,
) {
    val cardBackgroundColor by animateColorAsState(targetValue = when {
        taskState.active -> MaterialTheme.colors.secondary
        taskState.done -> MaterialTheme.colors.surface
        else -> MaterialTheme.colors.background
    })
    RecipeCard(
        background = cardBackgroundColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = task.description)
            // TODO Ingredients
            // TODO collapsed when done
            AnimatedVisibility(visible = !taskState.done) {
                IngredientList(task.ingredients)
                Row {
                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = onClick,
                    ) {
                        Text("Done")
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientList(ingredients: List<Ingredient>) {
    // TODO
}

@Composable
fun Ingredient(ingredient: Ingredient) {
    // TODO
}

@Composable
fun RecipeSummary(recipe: Recipe) {
    RecipeCard {
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = recipe.title, style = MaterialTheme.typography.h1)
            Text(text = "Feeds ${recipe.persons}",
                 style = MaterialTheme.typography.caption)
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = recipe.description, style = MaterialTheme.typography.body1)

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Restart")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CardPreview() {
    RecipesTheme(darkTheme = true) {
        val recipes by remember { mutableStateOf(DataSource().recipes) }

        TaskCard(recipes[0].tasks[0], TaskState()) {}
    }
}

@Preview(showBackground = true)
@Composable
fun RecipePreview() {
    RecipesTheme(darkTheme = true) {
        val recipes by remember { mutableStateOf(DataSource().recipes) }
        RecipesUI(recipes, recipes[0])
    }
}

@Preview(showBackground = true)
@Composable
fun DrawerPreview() {
    RecipesTheme(darkTheme = true) {
        val recipes by remember { mutableStateOf(DataSource().recipes) }
        DrawerContent(recipes) {}
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RecipesTheme(darkTheme = true) {
        val recipes by remember { mutableStateOf(DataSource().recipes) }
        RecipesUI(recipes)
    }
}