package br.unisanta.minhaloja

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

/**
 * Tela "Meus favoritos": le os favoritos do usuario no Firestore
 * (do mais recente para o mais antigo) e permite remover.
 */
class FavoritosActivity : AppCompatActivity() {

    private lateinit var recyclerFavoritos: RecyclerView
    private lateinit var progressFavoritos: ProgressBar
    private lateinit var txvMensagem: TextView

    private val favoritos = mutableListOf<Favorito>()
    private lateinit var adapter: FavoritoAdapter

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        recyclerFavoritos = findViewById(R.id.recycler_favoritos)
        progressFavoritos = findViewById(R.id.progress_favoritos)
        txvMensagem = findViewById(R.id.txv_mensagem_favoritos)

        adapter = FavoritoAdapter(favoritos) { favorito -> removerFavorito(favorito) }
        recyclerFavoritos.layoutManager = LinearLayoutManager(this)
        recyclerFavoritos.adapter = adapter

        findViewById<Button>(R.id.btn_voltar).setOnClickListener { finish() }

        carregarFavoritos()
    }

    // referencia da subcolecao favoritos do usuario logado
    private fun favoritosRef() = db.collection("usuarios")
        .document(auth.currentUser!!.uid)
        .collection("favoritos")

    private fun carregarFavoritos() {
        progressFavoritos.visibility = View.VISIBLE
        txvMensagem.visibility = View.GONE

        favoritosRef()
            .orderBy("salvoEm", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { resultado ->
                favoritos.clear()
                for (doc in resultado.documents) {
                    val favorito = Favorito(
                        id = doc.getLong("id")?.toInt() ?: 0,
                        titulo = doc.getString("titulo") ?: "",
                        preco = doc.getDouble("preco") ?: 0.0,
                        categoria = doc.getString("categoria") ?: "",
                        salvoEm = doc.getTimestamp("salvoEm")?.toDate()
                    )
                    favoritos.add(favorito)
                }
                adapter.notifyDataSetChanged()
                progressFavoritos.visibility = View.GONE

                if (favoritos.isEmpty()) {
                    txvMensagem.text = getString(R.string.nenhum_favorito)
                    txvMensagem.visibility = View.VISIBLE
                }
            }
            .addOnFailureListener { erro ->
                progressFavoritos.visibility = View.GONE
                txvMensagem.text = getString(R.string.erro_carregar_favoritos, erro.message)
                txvMensagem.visibility = View.VISIBLE
            }
    }

    private fun removerFavorito(favorito: Favorito) {
        favoritosRef().document(favorito.id.toString())
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, R.string.favorito_removido, Toast.LENGTH_SHORT).show()
                carregarFavoritos()
            }
            .addOnFailureListener { erro ->
                Toast.makeText(this, getString(R.string.erro_remover_favorito, erro.message), Toast.LENGTH_LONG).show()
            }
    }
}
