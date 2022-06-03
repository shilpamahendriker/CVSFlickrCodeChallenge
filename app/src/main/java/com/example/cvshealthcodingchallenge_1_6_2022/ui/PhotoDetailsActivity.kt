package com.example.cvshealthcodingchallenge_1_6_2022.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import coil.compose.rememberImagePainter
import com.example.cvshealthcodingchallenge_1_6_2022.R

class PhotoDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageUrl: String? = intent.getStringExtra(IMAGE_URL)
        val imageTitle: String? = intent.getStringExtra(IMAGE_TITLE)
        val imageDesc: String? = intent.getStringExtra(IMAGE_DESC)
        val imageAuthor: String? = intent.getStringExtra(IMAGE_AUTHOR)

        setContent {
            Scaffold(
                topBar = { DetailsTopBar() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = imageUrl,
                        ),
                        contentDescription = imageDesc,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Text(
                        text = "Title: ${imageTitle ?: "Unknown"}",
                        modifier = Modifier.padding(8.dp)
                    )
                    Text(
                        text = "Description",
                        modifier = Modifier.padding(8.dp)
                    )
                    HtmlText(
                        html = imageDesc.toString(),
                        modifier = Modifier.padding(8.dp))
                    Text(
                        text = "Author: ${imageAuthor ?: "unknown author"}",
                        modifier = Modifier.padding(8.dp)
                    )

                }
            }

        }
    }
 companion object {

     private  const val IMAGE_URL = "image_url"
     private  const val IMAGE_TITLE = "image_title"
     private  const val IMAGE_DESC = "image_description"
     private  const val IMAGE_AUTHOR = "image_author"

     fun flickrDetailIntent(
        context: Context,
        imageUrl: String?,
        title: String?,
        description: String?,
        author: String?

     ): Intent {
         return Intent(context, PhotoDetailsActivity::class.java ).apply {
             putExtra(IMAGE_URL,imageUrl)
             putExtra(IMAGE_TITLE,title)
             putExtra(IMAGE_DESC,description)
             putExtra(IMAGE_AUTHOR,author)
         }
     }
 }

}

@Composable
fun DetailsTopBar() {
    val activity = LocalContext.current as PhotoDetailsActivity
    TopAppBar(
        title = { Text(text = "Details") },
        navigationIcon = {
            Icon(painter = painterResource(id = R.drawable.ic_back),
                contentDescription = "go back",
                modifier = Modifier
                    .padding(16.dp)
                    .clickable {
                        activity.finish()
                    }
            )

        }
    )
}

@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context) },
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}