package br.unisanta.minhaloja

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Cria o Retrofit uma vez so e devolve o service pronto para usar.
 */
object RetrofitClient {

    private const val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val produtoService: ProdutoService by lazy {
        retrofit.create(ProdutoService::class.java)
    }
}
