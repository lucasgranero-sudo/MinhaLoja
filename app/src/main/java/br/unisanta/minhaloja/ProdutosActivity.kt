package br.unisanta.minhaloja

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

/**
 * Lista de produtos da Fake Store API. Tocar em um produto salva ele
 * nos favoritos do usuario logado no Firestore.
 */
class ProdutosActivity : AppCompatActivity() {

    private lateinit var recyclerProdutos: RecyclerView
    private lateinit var progressProdutos: ProgressBar
    private lateinit var txvMensagem: TextView
    private lateinit var btnTentarNovamente: Button

    private val produtos = mutableListOf<Produto>()
    private lateinit var adapter: ProdutoAdapter

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_produtos)

        recyclerProdutos = findViewById(R.id.recycler_produtos)
        progressProdutos = findViewById(R.id.progress_produtos)
        txvMensagem = findViewById(R.id.txv_mensagem_produtos)
        btnTentarNovamente = findViewById(R.id.btn_tentar_novamente)

        adapter = ProdutoAdapter(produtos) { produto -> salvarFavorito(produto) }
        recyclerProdutos.layoutManager = LinearLayoutManager(this)
        recyclerProdutos.adapter = adapter

        findViewById<Button>(R.id.btn_favoritos).setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        findViewById<Button>(R.id.btn_sair).setOnClickListener {
            auth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            // limpa a pilha pra nao voltar pra lista com o botao voltar
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        btnTentarNovamente.setOnClickListener { carregarProdutos() }

        carregarProdutos()
    }

    private fun carregarProdutos() {
        progressProdutos.visibility = View.VISIBLE
        txvMensagem.visibility = View.GONE
        btnTentarNovamente.visibility = View.GONE

        lifecycleScope.launch {
            try {
                val lista = RetrofitClient.produtoService.listarProdutos()
                produtos.clear()
                produtos.addAll(lista)
                adapter.notifyDataSetChanged()

                if (lista.isEmpty()) {
                    txvMensagem.text = getString(R.string.nenhum_produto)
                    txvMensagem.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
                txvMensagem.text = getString(R.string.erro_carregar_produtos)
                txvMensagem.visibility = View.VISIBLE
                btnTentarNovamente.visibility = View.VISIBLE
            } finally {
                progressProdutos.visibility = View.GONE
            }
        }
    }

    private fun salvarFavorito(produto: Produto) {
        val uid = auth.currentUser?.uid ?: return

        val dados = hashMapOf(
            "id" to produto.id,
            "titulo" to produto.title,
            "preco" to produto.price,
            "categoria" to produto.category,
            "salvoEm" to FieldValue.serverTimestamp()
        )

        // o id do produto e o nome do documento, assim nao duplica
        db.collection("usuarios").document(uid)
            .collection("favoritos").document(produto.id.toString())
            .set(dados)
            .addOnSuccessListener {
                Toast.makeText(this, R.string.favorito_salvo, Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { erro ->
                Toast.makeText(this, getString(R.string.erro_salvar_favorito, erro.message), Toast.LENGTH_LONG).show()
            }
    }
}
