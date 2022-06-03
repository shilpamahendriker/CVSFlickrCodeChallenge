package com.example.cvshealthcodingchallenge_1_6_2022.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.window.PopupProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModelProvider
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import coil.size.Size
import com.example.cvshealthcodingchallenge_1_6_2022.R
import com.example.cvshealthcodingchallenge_1_6_2022.backend.FlickrAPIService
import com.example.cvshealthcodingchallenge_1_6_2022.backend.Repository
import com.example.cvsheathcodechallenge.models.FlickrPhotoResponse
import com.example.cvsheathcodechallenge.models.Items

class PhotoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lateinit var photoActivityViewModel: PhotoActivityViewModel
        val retrofitService = FlickrAPIService.getInstance()
        val repository = Repository(retrofitService!!)
        photoActivityViewModel = ViewModelProvider(this, ViewModelFactory(repository = repository)).get(PhotoActivityViewModel::class.java)

        setContent {

            val state: MainViewState by photoActivityViewModel.mainViewStateFlow.collectAsState()
            MaterialTheme {
                Scaffold(
                    topBar = { MainTopBar() }
                ) {
                    Column() {
                        SearchHeader(photoActivityViewModel)
//                        AutoCompleteTextField(photoActivityViewModel)
                        state.let { mainState ->
                            when (mainState) {
                                is MainViewState.LandingState -> {
                                    LandingView()
                                }
                                is MainViewState.LoadingState -> {
                                    LoadingIndicator()
                                }
                                is MainViewState.NoPhotosState -> {
                                    EmptyStateView()
                                }
                                is MainViewState.PhotosState -> {
                                    PhotoStateView(
                                        photos = mainState.photos
                                        )
                                }
                                is MainViewState.ErrorState -> {
                                    ErrorStateView()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LandingView(){
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Arrangement.CenterHorizontally
        ) {
            Text(
                text = "Use the top search bar to search your favourite photos on Flickr",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun MainTopBar() {
    TopAppBar(
        title = { Text(text = "Photos") },

    )
}

@Composable
fun LoadingIndicator(){
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Arrangement.CenterHorizontally
        ) {
            CircularProgressIndicator()
        }
    }
}

@Composable
fun EmptyStateView(){
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Arrangement.CenterHorizontally
        ) {
            Text(
                text = "Did not match any images, try another search",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
fun ErrorStateView(){
    MaterialTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Arrangement.CenterHorizontally
        ) {
            Text(
                text = "Oops something went wrong, try again later",
                color = Color.Black,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterialApi::class)
@Composable
fun PhotoStateView( photos: List<Items> ){
    val context = LocalContext.current
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        items(photos) { photo ->
            Card(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                elevation = 8.dp,
                onClick = {
                    context.startActivity(
                        PhotoDetailsActivity.flickrDetailIntent(
                            context = context,
                            imageUrl = photo.media.m,
                            title = photo.title,
                            description = photo.description,
                            author = photo.author
                        )
                    )
                }
            ) {
                Column() {
                    val painter = rememberImagePainter(
                        data = photo.media.m,

                    )
                    Image(
                        painter = painter,
                        contentDescription = "Image loaded",
                        modifier = Modifier
                            .size(75.dp)
                            .padding(4.dp),
                        contentScale = ContentScale.FillBounds,

                    )
                    Text(
                        text = photo.title,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

// Tried to create an auto complete text view but did not work out nicely need some more time on this
@Composable
fun AutoCompleteTextField(photoActivityViewModel : PhotoActivityViewModel){
    var masterSuggestions = listOf ("porcupine", "tiger", "lion")
    var suggestions = remember { mutableStateListOf <String>() }
    var selectedText by remember { mutableStateOf("") }
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(12.dp)
    ) {
        Column() {
            OutlinedTextField(
                value = selectedText,
                onValueChange = {
                    suggestions.removeAll { true }
                    selectedText = it
                    for (name in masterSuggestions) {
                        if (name.startsWith(selectedText)) {
                            suggestions.add(name)
                        }

                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .weight(1.8f),
                maxLines = 1,
                label = { Text("Search") }
            )
            DropdownMenu(
                expanded = suggestions.isNotEmpty(),
                onDismissRequest = { },
                properties = PopupProperties(focusable = false),
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .weight(1.8f)
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(onClick = {
                        selectedText = label
                    }) {
                        Text(text = label)
                    }
                }
            }
        }
        Button(
            onClick = {
                if (selectedText == "") {
                    Toast.makeText(
                        context,
                        "Please enter a search.",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    photoActivityViewModel.onAction(MainViewAction.FetchPhotos(selectedText))
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .weight(1.2f)
                .height(40.dp)
        ) {
            Text(
                text = "Search",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}


@Composable
fun SearchHeader(photoActivityViewModel : PhotoActivityViewModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(12.dp)
    ) {
        var searchString by remember { mutableStateOf("") }
        TextField(
                value = searchString,
                onValueChange = { searchString = it },
                modifier = Modifier
                    .height(58.dp)
                    .weight(1.8f)
                    .padding(2.dp)
            )
        Button(
            onClick = {
                if (searchString == ""){
                    Toast.makeText(
                        context,
                        "Please enter a search.",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    photoActivityViewModel.onAction(MainViewAction.FetchPhotos(searchString))
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .weight(1.2f)
                .height(40.dp)

        ) {
            Text(
                text = "Search",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}



