package com.example.flightsearch.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName="favorite")
data class Favorite(
    @PrimaryKey
    val id: Long,

    @ColumnInfo(name = "codigo_de_salida")
    val codigoDeSalida: String,

    @ColumnInfo(name = "destination_code")
    val destinationCode: String
)