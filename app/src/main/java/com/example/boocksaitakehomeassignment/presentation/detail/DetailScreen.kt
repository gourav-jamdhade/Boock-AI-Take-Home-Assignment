package com.example.boocksaitakehomeassignment.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.boocksaitakehomeassignment.R
import com.example.boocksaitakehomeassignment.domain.model.Book

@Composable
fun DetailScreen(
    onNavigateBack: () -> Unit,
    onReadClick: (String) -> Unit,
    viewModel: DetailViewModel = hiltViewModel()
) {

    val book by viewModel.book.collectAsState()
    val context = LocalContext.current

    DetailScreenContent(
        book = book,
        onNavigateBack = onNavigateBack,
        onReadClick = onReadClick,
        onToggleSave = { safeBook ->
            viewModel.toggleSavedStatus()
            val message = if (safeBook.isSaved) "Removed from Library" else "Added to Library"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreenContent(
    book: Book?,
    onNavigateBack: () -> Unit,
    onReadClick: (String) -> Unit,
    onToggleSave: (Book) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    book?.let { safeBook ->
                        IconButton(onClick = {
                            onToggleSave(safeBook)
                        }) {
                            Icon(
                                painter = if (safeBook.isSaved) painterResource(R.drawable.baseline_favorite_24) else painterResource(
                                    R.drawable.baseline_favorite_border_24
                                ),
                                contentDescription = "Toggle Save",
                                tint = if (safeBook.isSaved) MaterialTheme.colorScheme.primary else LocalContentColor.current
                            )
                        }

                    }
                }
            )
        }
    )
    { padding ->
        if (book == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                //Cover Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .aspectRatio(0.66f)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Cover", color = MaterialTheme.colorScheme.onSurfaceVariant)

                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = book.title,
                    style = MaterialTheme.typography.headlineMedium,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = book.author,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                //The CAT button logic
                Button(
                    onClick = { onReadClick(book.id) },
                    modifier = Modifier.fillMaxWidth(0.6f),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    val ctaText =
                        if (book.readingProgress > 0) "Continue Reading" else "Start Reading"
                    Text(text = ctaText, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = book.summary,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Justify
                )
            }
        }

    }
}