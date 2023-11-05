package com.example.flightsearch.data

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FlightSearchDAO {
    // Consulta para la función de autocompletar
    @Query(
        """
        SELECT * FROM airport 
        WHERE codigo_IATA LIKE :query OR nombre LIKE :query
        """
    )
    fun getAutocompleteSuggestions(query: String): Flow<List<Airport>>

    // Consulta para obtener los aeropuertos más frecuentados
    @Query(
        """
        SELECT * FROM airport 
        ORDER BY pasajeros DESC
        """
    )
    fun getMostFrequentedAirports(): Flow<List<Airport>>
}
