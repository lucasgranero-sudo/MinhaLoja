package br.unisanta.minhaloja

import java.util.Date

/**
 * Favorito salvo no Firestore em usuarios/{uid}/favoritos/{id do produto}.
 */
data class Favorito(
    val id: Int,
    val titulo: String,
    val preco: Double,
    val categoria: String,
    val salvoEm: Date?
)
