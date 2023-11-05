/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.flightsearch.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.flightsearch.R
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.ui.theme.FlightSearchTheme
import kotlinx.coroutines.flow.Flow

// ... tus importaciones anteriores

@Composable
fun FlightSearchApp(
    viewModel: FlightSearchViewModel = viewModel(factory = FlightSearchViewModel.factory)
) {
    val navController = rememberNavController()
    val mostFrequentedAirports by viewModel.getFullSchedule().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            // Personaliza tu TopAppBar si es necesario
        }
    ) { innerPadding ->
        NavHost(navController = navController, startDestination = "full_schedule") {
            composable("full_schedule") {
                FullScheduleScreen(
                    airports = mostFrequentedAirports,
                    onAirportClick = { airportCode ->
                        // Acciones al hacer clic en un aeropuerto, por ejemplo, navegar a detalles
                    },
                    isAirportFavorite = { airportCode ->
                        // Aquí puedes recolectar el estado del Flow<Boolean> si necesitas el valor actual o pasarlo como un Flow
                        viewModel.isAirportFavorite(airportCode)
                    },
                    contentPadding = innerPadding
                )
            }
            // Aquí pueden ir otras composables de tu navegación si las necesitas
        }
    }
}

@Composable
fun FullScheduleScreen(
    airports: List<Airport>,
    isAirportFavorite: (String) -> Flow<Boolean>, // Función que verifica si un aeropuerto es favorito.
    onAirportClick: (String) -> Unit,
    contentPadding: PaddingValues
) {
    Column(modifier = Modifier.padding(contentPadding)) {
        Text(
            text = "Flights from FCO",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn {
            items(airports) { airport ->
                val isFavorite by isAirportFavorite(airport.codigoIATA).collectAsState(initial = false)
                AirportRow(airport, isFavorite, onAirportClick)
            }
        }
    }
}


@Composable
fun AirportRow(
    airport: Airport,
    isFavorite: Boolean, // Asumiendo que tienes una manera de saber si es favorito o no
    onAirportClick: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onAirportClick(airport.codigoIATA) }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "DEPART",
                    style = MaterialTheme.typography.titleMedium
                )
                if (isFavorite) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorite",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = "${airport.nombre} (${airport.codigoIATA})",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "ARRIVE",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Destination Name", // Aquí se debería utilizar el nombre real de destino si está disponible
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun FavoritesScreen(
    favorites: List<Favorite>,
    onFavoriteClick: (Favorite) -> Unit,
    contentPadding: PaddingValues
) {
    LazyColumn(contentPadding = contentPadding) {
        items(favorites) { favorite ->
            FavoriteCard(favorite, onFavoriteClick)
        }
    }
}

@Composable
fun FavoriteCard(
    favorite: Favorite,
    onFavoriteClick: (Favorite) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onFavoriteClick(favorite) }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Salida: ${favorite.codigoDeSalida} - Destino: ${favorite.destinationCode}",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Flight",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Composable
fun FavoriteRow(
    favorite: Favorite,
    onFavoriteClick: (Favorite) -> Unit
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .clickable { onFavoriteClick(favorite) }
        .padding(16.dp)) {
        Text(
            text = "Salida: ${favorite.codigoDeSalida} - Destino: ${favorite.destinationCode}",
            style = MaterialTheme.typography.labelMedium
        )
    }
}
//.. resto de tus Composables, si es necesario.
@Preview(showBackground = true)
@Composable
fun FavoritesListScreenPreview() {
    // Asume que tienes un tema para tu aplicación que puedes aplicar, reemplaza FlightSearchTheme con MaterialTheme si no existe.
    FlightSearchTheme {
        // Puedes utilizar PaddingValues() con valores predeterminados para la vista previa
        FavoritesScreen(
            favorites = List(3) { index ->
                Favorite(index.toLong(), "FCO", "SVO") // Datos de muestra para la vista previa
            },
            onFavoriteClick = {}, // Implementación vacía para la vista previa
            contentPadding = PaddingValues() // Padding predeterminado
        )
    }
}

