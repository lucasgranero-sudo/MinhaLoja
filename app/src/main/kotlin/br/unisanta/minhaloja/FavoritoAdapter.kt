package br.unisanta.minhaloja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Adapter da tela de favoritos. Cada item tem um botao "Remover".
 */
class FavoritoAdapter(
    private val favoritos: List<Favorito>,
    private val onRemover: (Favorito) -> Unit
) : RecyclerView.Adapter<FavoritoAdapter.FavoritoViewHolder>() {

    private val formatoData = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    class FavoritoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitulo: TextView = view.findViewById(R.id.txv_titulo_favorito)
        val txvPreco: TextView = view.findViewById(R.id.txv_preco_favorito)
        val txvCategoria: TextView = view.findViewById(R.id.txv_categoria_favorito)
        val txvSalvoEm: TextView = view.findViewById(R.id.txv_salvo_em)
        val btnRemover: Button = view.findViewById(R.id.btn_remover)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorito, parent, false)
        return FavoritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoritoViewHolder, position: Int) {
        val favorito = favoritos[position]
        holder.txvTitulo.text = favorito.titulo
        holder.txvPreco.text = String.format(Locale.US, "US$ %.2f", favorito.preco)
        holder.txvCategoria.text = favorito.categoria
        holder.txvSalvoEm.text = if (favorito.salvoEm != null) {
            "Salvo em " + formatoData.format(favorito.salvoEm)
        } else {
            ""
        }
        holder.btnRemover.setOnClickListener { onRemover(favorito) }
    }

    override fun getItemCount(): Int = favoritos.size
}
