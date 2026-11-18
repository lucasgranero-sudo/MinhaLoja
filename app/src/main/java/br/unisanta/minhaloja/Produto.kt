package br.unisanta.minhaloja

/**
 * Produto que vem da Fake Store API (GET /products).
 * O campo rating e um objeto dentro do objeto, por isso tem a data class Rating.
 */
data class Produto(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
)

data class Rating(
    val rate: Double,
    val count: Int
)
