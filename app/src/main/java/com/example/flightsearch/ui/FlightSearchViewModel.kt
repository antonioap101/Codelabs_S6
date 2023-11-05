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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.flightsearch.FlightSearchApp
import com.example.flightsearch.data.Airport
import com.example.flightsearch.data.Favorite
import com.example.flightsearch.data.FlightSearchDAO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FlightSearchViewModel(private val flightSearchDAO: FlightSearchDAO): ViewModel() {

    // Suponiendo que quieres obtener una lista de aeropuertos frecuentados, que podría ser similar a obtener un "horario completo"
    fun getFullSchedule(): Flow<List<Airport>> = flightSearchDAO.getMostFrequentedAirports()

    fun getScheduleFor(airportCode: String): Flow<List<Airport>> =
        flightSearchDAO.getAutocompleteSuggestions(airportCode)

    // Funcion para obtener todos los vuelos guardados en favoritos
    fun getAllFavorites(): Flow<List<Favorite>> =
        flightSearchDAO.getFavoriteFlights()

    fun isAirportFavorite(airportCode: String): Flow<Boolean> {
        return flightSearchDAO.getFavoriteFlights().map { favorites ->
            favorites.any { it.codigoDeSalida == airportCode }
        }
    }

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FlightSearchApp) // Cambia el nombre de la aplicación si es necesario
                FlightSearchViewModel(application.database.FlightSearchDAO())
            }
        }
    }
}



