package com.recipes

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.recipes.model.*
import com.recipes.ui.theme.RecipesTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appContext = applicationContext

        NotificationHelper(appContext).setupNotificationChannels()

        setContent {
            RecipesTheme {
                val recipes by remember { mutableStateOf(DataSource().recipes) }
                RecipesUI(recipes)
            }
        }
    }

    companion object {
        lateinit var appContext: Context
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
            selected = false,
            onClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(0)
                }
            },
            icon = { Icon(Icons.Filled.VerticalAlignTop, "Back to top") }
        )
        BottomNavigationItem(
            selected = false,
            onClick = {
                scope.launch {
                    lazyListState.animateScrollToItem(getActiveComponentIndex())
                }
            },
            icon = { Icon(Icons.Filled.SkipNext, "Next step") }
        )
    }
}

fun getActiveComponentIndex(): Int {
    throw NotImplementedError()
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

    val nextTasks = remember {
        tasks.mapIndexed { index, task ->
            task.id to when (task.nextTask) {
                null -> if (index < tasks.size - 1) tasks[index + 1].id else null
                else -> task.nextTask
            }
        }.toMap()
    }

    val taskListState = rememberLazyListState()

    val defaultTaskState = { index: Int, task: Task ->
        task.id to TaskState(
            index = index,
            active = index == 0,
            done = false,
        )
    }

    val taskStateLookup = remember {
        mutableStateMapOf(
            *tasks.mapIndexed(defaultTaskState).toTypedArray()
        )
    }

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        state = taskListState,
    ) {
        item {
            RecipeSummary(recipe) {
                tasks.mapIndexed(defaultTaskState).forEach {
                    taskStateLookup[it.first] = it.second
                }
            }
        }

        val indexLookup = mutableMapOf<TaskId, Int>()

        itemsIndexed(
            items = recipe.tasks
        ) { index, task ->
            val id = task.id
            val taskState = taskStateLookup[id]!!
            indexLookup[id] = index
            val timerState by remember { mutableStateOf(TimerState.fromTask(task)) }

            TaskCard(recipe, task, taskState, timerState) {
                scope.launch {
                    timerState?.reset()

                    val nextItem = nextTasks[task.id]
                    taskStateLookup[id] = taskState.copy(active = false, done = true)
                    val nextIndex = if (nextItem != null) {
                        taskStateLookup[nextItem] =
                            taskStateLookup[nextItem]!!.copy(active = true)
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
    recipe: Recipe,
    task: Task,
    taskState: TaskState,
    timerState: TimerState?,
    onDoneClick: () -> Unit,
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
            AnimatedVisibility(visible = !taskState.done) {
                Column {
                    IngredientList(task.ingredients)
                    TaskControls(recipe, task, timerState, onDoneClick)
                }
            }
        }
    }
}

@Composable
fun TaskControls(recipe: Recipe, task: Task, timerState: TimerState?, onDoneClick: () -> Unit) {
    Row {
        Spacer(Modifier.weight(1f))
        timerState?.let {
            TimerButton(recipe, task, it)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Button(onClick = onDoneClick) {
            Column {
                Text("Done")
            }
        }
    }
}

@Composable
fun TimerButton(recipe: Recipe, task: Task, timerState: TimerState) {
    var timerSnapshot by remember { mutableStateOf(timerState.snapshot) }
    val scope = rememberCoroutineScope()

    Button(onClick = {
        if (timerSnapshot.isDone) {
            timerState.reset()
            timerSnapshot = timerState.snapshot
        } else if (!timerState.isRunning) {
            timerState.start()
            timerSnapshot = timerState.snapshot

            scope.launch {
                do {
                    NotificationHelper(MainActivity.appContext)
                        .showNotification(recipe, task, timerState, true)
                    delay(500)
                    timerSnapshot = timerState.snapshot
                } while (!timerSnapshot.isDone)

                NotificationHelper(MainActivity.appContext).dismissNotification(task)
                // TODO notification --> Done --> Scrollto
            }
        }
        /* else TODO Pause?!!!??? */
    }) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TimerProgressIndicator(timerSnapshot = timerSnapshot, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.size(5.dp))
            Text(
                when {
                    timerSnapshot.isDone -> "Restart"
                    !timerSnapshot.isRunning -> "Start ${timerSnapshot.remainingWholeSeconds} timer"
                    else -> "Wait ${timerSnapshot.remainingWholeSeconds}"
                }
                // TODO: Disabled while running
            )
        }
    }
}

@Composable
fun TimerProgressIndicator(timerSnapshot: TimerState.TimerSnapshot, modifier: Modifier = Modifier) {
    AnimatedVisibility(visible = timerSnapshot.isRunning && !timerSnapshot.isDone) {
        CircularProgressIndicator(
            progress = timerSnapshot.progress,
            color = MaterialTheme.colors.onSurface,
            modifier = modifier,
        )
    }
    AnimatedVisibility(visible = timerSnapshot.isDone) {
        Icon(Icons.Filled.Check, "Done", modifier = modifier)
    }
    AnimatedVisibility(visible = !timerSnapshot.isRunning) {
        Spacer(modifier = modifier)
    }
}

@Composable
fun IngredientList(ingredients: List<Ingredient>) {
    Column {
        ingredients.map {
            Ingredient(it)
            Divider(color = MaterialTheme.colors.onBackground, thickness = 1.dp)
        }
    }
}

@Composable
fun Ingredient(ingredient: Ingredient) {
    val unit = ingredient.unit
    Row {
        Text(
            text = ingredient.amount.asAmountString(),
            textAlign = TextAlign.End,
            modifier = Modifier.weight(.5f)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = (if (unit == MeasUnit.Custom) ingredient.customUnit else unit.name).toString(),
            modifier = Modifier.weight(.5f))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = ingredient.name)
            if (ingredient.description != null)
                Text(text = ingredient.description, style = MaterialTheme.typography.caption)
        }
    }
}

@Composable
fun RecipeSummary(recipe: Recipe, onReset: () -> Unit) {
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
            Spacer(modifier = Modifier.height(10.dp))
            RecipeIngredients(recipe)

            Button(onClick = onReset) {
                Text(text = "Restart")
            }
        }
    }
}

@Composable
private fun RecipeIngredients(recipe: Recipe) {
    var ingredientsVisible by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable { ingredientsVisible = !ingredientsVisible }
        ) {
            Text(text = "Ingredients", style = MaterialTheme.typography.caption)
            Spacer(modifier = Modifier.width(5.dp))
            // TODO aggregated Ingredients _OR_ separately; i.e. what to prepare _OR_ in how many bowls
            Icon(
                if (ingredientsVisible) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                if (ingredientsVisible) "Hide Ingredients" else "Show Ingredients",
                modifier = Modifier.height(with(LocalDensity.current) {
                    MaterialTheme.typography.caption.fontSize.toDp()
                })
            )
            Spacer(modifier = Modifier.width(5.dp))
        }
        AnimatedVisibility(visible = ingredientsVisible) {
            Row {
                Spacer(modifier = Modifier.width(20.dp))
                IngredientList(ingredients = recipe.allIngredients())
            }
        }
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
fun CardPreview() {
    RecipesTheme(darkTheme = true) {
        val recipes by remember { mutableStateOf(DataSource().recipes) }
        val timerState by remember { mutableStateOf(TimerState.fromTask(recipes[0].tasks[0])) }

        recipes[0].let {
            TaskCard(it, it.tasks[2], TaskState(0), timerState) {}
        }
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