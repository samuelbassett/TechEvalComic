package com.tc.techevalcomic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.tc.techevalcomic.data.remote.XkcdComicResponse
import com.tc.techevalcomic.ui.theme.TechEvalComicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TechEvalComicTheme {
                XkcdApp()
            }
        }
    }
}

@Composable
fun XkcdApp() {
    val navController = rememberNavController()
    val viewModel: XkcdViewModel = hiltViewModel()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                onComicSelected = { selectedComic ->
                    viewModel.selectComic(selectedComic)
                    navController.navigate("details")
                }
            )
        }
        composable("details") {
            DetailsScreen()
        }
    }
}

@Composable
fun MainScreen(
    onComicSelected: (XkcdComicResponse) -> Unit,
    viewModel: XkcdViewModel = hiltViewModel()
) {
    val comics by viewModel.comics.collectAsState()

    LaunchedEffect(Unit) {
        for(comicNumber in 1..10) {
            viewModel.fetchComic(comicNumber)
        }
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(comics) { comic ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onComicSelected(comic) }
            ) {
                Text(text = "${comic.month}/${comic.year}")
            }
        }
    }
}

@Composable
fun DetailsScreen(viewModel: XkcdViewModel = hiltViewModel()) {
    val selectedComic by viewModel.selectedComic.collectAsState()

    selectedComic?.let { comic ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)) {
            Text(text = "Month: ${comic.month}")
            Text(text = "Year: ${comic.year}")
            Image(
                painter = rememberAsyncImagePainter(model = comic.img),
                contentDescription = comic.title,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}