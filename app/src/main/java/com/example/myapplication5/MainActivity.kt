package com.example.myapplication5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication5.ui.theme.MyApplication5Theme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") { MyApplication5Theme { ArtSpace(navController) } }
                composable("secondary") { MyApplication5Theme { Oof(navController) } }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ArtPiece(onClick: () -> Unit) {
    Surface(
        elevation = 10.dp,
        modifier = Modifier.padding(10.dp),
        onClick = onClick,
    ) {
        Spacer(modifier = Modifier.size(150.dp))
    }
}

@Composable
fun ArtWall(navController: NavHostController) {
    Row(
        verticalAlignment = Alignment.Top,
    ) {
        ArtPiece() {
            navController.navigate("secondary")
        }
    }
}

@Composable
fun ArtDescription() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text = "Name")
        Text(text = "Artista")
    }
}

@Composable
fun ArtButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.padding(5.dp)
    ) {
        Text(text = text)
    }
}

@Composable
fun ArtControls() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        ArtButton(text = stringResource(id = R.string.prev_art_piece)) {
        }
        ArtButton(text = stringResource(id = R.string.next_art_piece)) {
        }
    }
}


@Composable
fun ArtSpace(navController: NavHostController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
        ) {
            ArtWall(navController)
            ArtDescription()
            ArtControls()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val navController = rememberNavController()
    MyApplication5Theme {
        ArtSpace(navController)
    }
}