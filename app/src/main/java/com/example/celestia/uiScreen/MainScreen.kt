package com.example.celestia.uiScreen

import android.annotation.SuppressLint
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.celestia.ui.theme.Black40
import com.example.celestia.viewmodel.ApodViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.celestia.ui.theme.Orange26
import com.example.celestia.ui.theme.white2
import java.net.URLEncoder
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import com.example.celestia.data.ApodEntity
import com.example.celestia.model.ApodDataModel
import com.example.celestia.ui.theme.lightBlue
import com.example.celestia.ui.theme.lightBlue2
import com.example.celestia.ui.theme.mintGreen
import com.example.celestia.ui.theme.softGrayBlue
import com.example.celestia.ui.theme.white
import com.example.celestia.ui.theme.yellow2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ApodViewModel, navController: NavController) {


    val apods by viewModel.apods.collectAsState()
    val selectedApod by viewModel.selectedApod.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    LaunchedEffect(Unit) {
        viewModel.fetchToday()
    }

    val context = LocalContext.current

    val dataNotFound by viewModel.dataNotFound.collectAsState()

    LaunchedEffect(dataNotFound) {

        if (dataNotFound) {
            Toast.makeText(context, "There is no APOD data for the selected date.", Toast.LENGTH_SHORT).show()
            viewModel.resetDataNotFound() // tekrar çalışabilmesi için reset gerekir
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDate = datePickerState.selectedDateMillis?.let { millis ->
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                    }
                    selectedDate?.let {
                        viewModel.fetchByDate(it)
                        showDatePicker = false
                    }
                }) {
                    Text(text="Select this date", color = lightBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(text="Cancel", color = lightBlue)
                }
            }
        ) {
            DatePicker(state = datePickerState)


        }
    }

    Scaffold(

        containerColor = Black40,
        topBar = {
            ExpandableHeader(
                onNasaClick = { navController.navigate("main/nasa") },
                onNewsClick = { navController.navigate("main/news") },
                onCalendarClick = { showDatePicker = true }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 12.dp)
        ) {
            selectedApod?.let { apod ->
                ApodCard(apod = apod, navController = navController)
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { viewModel.clearSelectedApod() },
                        colors = ButtonDefaults.buttonColors(containerColor = lightBlue)
                    ) {
                        Text("Show All")
                    }
                }

            } ?: run {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        //.padding(padding)
                        .padding(horizontal = 3.dp, vertical = 5.dp )
                ) {
                    /*
                    items(apods) { entity ->
                        ApodCard(apod = entity.toDataModel(), navController = navController)
                    }*/
                    items(apods) { entity ->
                        ApodCard(entity = entity, navController = navController, viewModel = viewModel)
                    }

                }
            }
        }
    }
}


/*
@Composable
fun ApodCard(apod: ApodDataModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                val encodedTitle = URLEncoder.encode(apod.title, "UTF-8")
                val encodedExplanation = URLEncoder.encode(apod.explanation, "UTF-8")
                val encodedImageUrl = URLEncoder.encode(apod.url, "UTF-8")
                val encodedDate = URLEncoder.encode(apod.date, "UTF-8")
                navController.navigate("detail/$encodedTitle/$encodedExplanation/$encodedImageUrl/$encodedDate")
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .shadow(1.dp, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val encodedUrl = URLEncoder.encode(apod.url, "UTF-8")
                            navController.navigate("fullscreenImage/$encodedUrl")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(apod.url),
                        contentDescription = apod.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    //Text(text = apod.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = apod.title,
                        color = lightBlue,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2, // kaç satıra kadar izin verilecek
                        overflow = TextOverflow.Ellipsis // taşan yazıya "..." koyar
                    )

                    Text(text = apod.date, style = MaterialTheme.typography.labelSmall)
                }

                var isFavorite by remember { mutableStateOf(false) }

                IconButton(onClick = { isFavorite = !isFavorite }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) lightBlue else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = apod.explanation,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4
            )
        }
    }
}*/

@Composable
fun ApodCard(entity: ApodEntity, navController: NavController, viewModel: ApodViewModel) {
    val apod = entity.toDataModel()
    var isFavorite by remember { mutableStateOf(entity.isFavorite) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                val encodedTitle = URLEncoder.encode(apod.title, "UTF-8")
                val encodedExplanation = URLEncoder.encode(apod.explanation, "UTF-8")
                val encodedImageUrl = URLEncoder.encode(apod.url, "UTF-8")
                val encodedDate = URLEncoder.encode(apod.date, "UTF-8")
                navController.navigate("detail/$encodedTitle/$encodedExplanation/$encodedImageUrl/$encodedDate")
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .shadow(1.dp, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val encodedUrl = URLEncoder.encode(apod.url, "UTF-8")
                            navController.navigate("fullscreenImage/$encodedUrl")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(apod.url),
                        contentDescription = apod.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = apod.title,
                        color = lightBlue,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(text = apod.date, style = MaterialTheme.typography.labelSmall)
                }

                IconButton(onClick = {
                    isFavorite = !isFavorite
                    viewModel.toggleFavorite(entity.date, entity.isFavorite)
                }) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) lightBlue else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = apod.explanation,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4
            )
        }
    }
}


// Extension function: ApodEntity → ApodDataModel
fun ApodEntity.toDataModel(): ApodDataModel {
    return ApodDataModel(
        date = this.date,
        title = this.title,
        explanation = this.explanation,
        url = this.url
    )
}


@Composable
fun ExpandableHeader(onNasaClick: () -> Unit,
                     onNewsClick: () -> Unit,
                     onCalendarClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Orange26)
        .padding(horizontal = 16.dp, vertical = 12.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row( // Sadece burası tıklanabilir
                modifier = Modifier
                    .weight(1f)
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Astronomy Picture of the Day",
                    color = Color.White,
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(end = 8.dp)
                )

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = "Expand Icon",
                    tint = Color.White
                )
            }

            IconButton(onClick =onCalendarClick,
                modifier = Modifier
                    .size(40.dp) // Butonun boyutu
                    .background(color = lightBlue.copy(alpha = 1f), shape = RoundedCornerShape(50)) // Arka plan + yuvarlaklık
                    .clip(RoundedCornerShape(50))) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Calendar",
                    tint = white
                )
            }
        }



        // Açılan Kısım
        AnimatedVisibility(visible = expanded) {
            Column(modifier = Modifier.padding(top = 12.dp)) {
                /*Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { onNasaClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = white2)
                ) {
                    Text("NASA Image and Video Library", modifier = Modifier.padding(12.dp), color= lightBlue2)
                }*/

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clickable { onNewsClick() },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = white2)
                ) {
                    Text("NEWS", modifier = Modifier.padding(12.dp), color= lightBlue2)
                }
            }
        }
    }

}

@Composable
fun ApodCard(apod: ApodDataModel, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .clickable {
                val encodedTitle = URLEncoder.encode(apod.title, "UTF-8")
                val encodedExplanation = URLEncoder.encode(apod.explanation, "UTF-8")
                val encodedImageUrl = URLEncoder.encode(apod.url, "UTF-8")
                val encodedDate = URLEncoder.encode(apod.date, "UTF-8")
                navController.navigate("detail/$encodedTitle/$encodedExplanation/$encodedImageUrl/$encodedDate")
            },
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .shadow(1.dp, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            val encodedUrl = URLEncoder.encode(apod.url, "UTF-8")
                            navController.navigate("fullscreenImage/$encodedUrl")
                        }
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(apod.url),
                        contentDescription = apod.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = apod.title,
                        color = lightBlue,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(text = apod.date, style = MaterialTheme.typography.labelSmall)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = apod.explanation,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 4
            )
        }
    }
}


