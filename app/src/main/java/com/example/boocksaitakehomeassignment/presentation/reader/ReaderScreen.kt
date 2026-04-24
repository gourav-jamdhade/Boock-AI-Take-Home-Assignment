package com.example.boocksaitakehomeassignment.presentation.reader

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.boocksaitakehomeassignment.R
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun ReaderScreen(
    onNavigateBack: () -> Unit,
    viewmodel: ReaderViewmodel = hiltViewModel()
) {

    val book by viewmodel.book.collectAsState()
    val textSize by viewmodel.textSize.collectAsState()
    val context = LocalContext.current

    // We use a mutable state to track if we've initialized the scroll position yet
    var hasRestoredScroll by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollState, hasRestoredScroll, book) {
        // Observing the scroll position as a reactive flow
        snapshotFlow { scrollState.value }
            .distinctUntilChanged()
            .filter { hasRestoredScroll && book != null}
            .debounce { 500L }
            .collect { progress ->
                val progressToSave = if (progress == 0) 1 else progress
                viewmodel.saveProgress(progressToSave)

            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = book?.title ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    TextButton(onClick = { viewmodel.cycleText() }) {
                        Text(
                            text = "aA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { padding ->
        if (book == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val safeBook = book!!

            //Restore Scroll Position
            LaunchedEffect(safeBook.id) {
                if (!hasRestoredScroll) {

                    if(safeBook.readingProgress > 0){
                        delay(150)
                        scrollState.animateScrollTo(safeBook.readingProgress)
                    }
                    scrollState.scrollTo(safeBook.readingProgress)
                    hasRestoredScroll = true
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(scrollState)
                    .padding(16.dp)
            ) {
                Text(
                    text = safeBook.title,
                    fontSize = textSize.sizeSp.sp * 1.5f,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = safeBook.content,
                    fontSize = textSize.sizeSp.sp,
                    lineHeight = (textSize.sizeSp * 1.5f).sp,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(64.dp))
            }
        }
    }
}