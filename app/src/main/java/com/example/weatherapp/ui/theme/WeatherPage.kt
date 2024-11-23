@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.weatherapp.ui.theme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness5
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.weatherapp.R
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    var city by remember { mutableStateOf("") }
    val weatherResult = viewModel.weatherResult.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        WeatherSearchBar(
            city = city,
            onCityChange = { city = it },
            onSearch = { viewModel.getData(city) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        when (val result = weatherResult.value) {
            is NetworkResponse.Error -> {
                ErrorView(message = result.message)
            }
            NetworkResponse.Loading -> {
                LoadingView()
            }
            is NetworkResponse.Success -> {
                WeatherDetails(data = result.data)
            }
            null -> {}
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun WeatherSearchBar(
    city: String,
    onCityChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val focusManager = LocalFocusManager.current
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp , top =16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            OutlinedTextField(
                value = city,
                singleLine = true ,
                onValueChange = onCityChange,
                placeholder = { Text("Enter Your city") },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black, // Purple
                    unfocusedBorderColor = Color.Gray,
                    cursorColor = Color.DarkGray ,
                    focusedLabelColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done), // Set Enter action as "Done"
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                shape = RoundedCornerShape(12.dp)
            )
            IconButton(
                onClick = onSearch,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = Color.Black)
                    .size(55.dp , 55.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint  = Color.White
                )
            }
        }
    }


@Composable
fun LoadingView() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        LottieAnimation(
            composition = composition,
            progress = progress,
            modifier = Modifier.size(150.dp) // Animation size
        )
    }
}

@Composable
fun ErrorView(message: String) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.error))
    val progress by animateLottieCompositionAsState(composition)

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally, // Center text and animation
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(200.dp) // Animation size
            )
            Spacer(modifier = Modifier.height(16.dp)) // Space between animation and text
            Text(
                text = message,
                color = Color.Red, // Text color for better visibility
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


@Composable
fun WeatherDetails(data: WeatherModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        // Location Card

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding( bottom =  16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f/9f)  // Set 16:9 aspect ratio for the container
            ) {
                // Background Lottie Animation with 16:9 aspect ratio
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.background))
                LottieAnimation(
                    composition = composition,
                    iterations = LottieConstants.IterateForever,
                    modifier = Modifier
                        .matchParentSize(),
                    contentScale = ContentScale.Crop  // This will ensure the animation fills the space
                )


                // Content on top


                    Column(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(16.dp) ,
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top

                    ) {


                        Text(
                            text = data.location.name,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold ,
                            color = Color.Black
                        )
                        Text(
                            text = "${data.location.region}, ${data.location.country}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Black
                        )

                    }
                }

            }
        }

        // Current Weather Card
    Card(
        modifier = Modifier
            .fillMaxWidth().padding(bottom = 24.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Temperature Section
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${data.current.temp_c}°C",
                        fontSize = 48.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(top =20.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Feels like: ${data.current.feelslike_c}°C",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        modifier = Modifier.padding(top=16.dp)
                    )
                }

                // Weather Condition Section
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    AsyncImage(
                        model = "https:${data.current.condition.icon}",
                        contentDescription = data.current.condition.text,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                            .background(Color.Cyan.copy(alpha = 0.1f))
                            .padding(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = data.current.condition.text,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }



    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(listOf(
            Triple("Wind", "${data.current.wind_kph} km/h", Icons.Default.Navigation),
            Triple("Humidity", "${data.current.humidity}%", Icons.Default.WaterDrop),
            Triple("Pressure", "${data.current.pressure_mb} mb", Icons.Default.Speed),
            Triple("UV Index", "${data.current.uv}", Icons.Default.Brightness5),
            Triple("Visibility", "${data.current.vis_km} km", Icons.Default.RemoveRedEye)
        )) { (label, value, icon) ->
            WeatherDetailCard(
                label = label,
                value = value,
                icon = icon
            )
        }
    }


    }

@Composable
fun WeatherDetailCard(
    label: String,
    value: String,
    icon: ImageVector
) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .height(210.dp)
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp) ,
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )

            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color.Black
            )

            Text(
                text = value,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}