package br.unisanta.minhaloja

import retrofit2.http.GET

/**
 * Interface do Retrofit. A resposta de /products e uma LISTA de produtos.
 */
interface ProdutoService {

    @GET("products")
    suspend fun listarProdutos(): List<Produto>
}
