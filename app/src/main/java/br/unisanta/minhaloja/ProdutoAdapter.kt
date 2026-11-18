package br.unisanta.minhaloja

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

/**
 * Adapter da lista de produtos. Mostra titulo, preco e categoria.
 * Quando toca no item chama o onClick (que salva nos favoritos).
 */
class ProdutoAdapter(
    private val produtos: List<Produto>,
    private val onClick: (Produto) -> Unit
) : RecyclerView.Adapter<ProdutoAdapter.ProdutoViewHolder>() {

    class ProdutoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txvTitulo: TextView = view.findViewById(R.id.txv_titulo_produto)
        val txvPreco: TextView = view.findViewById(R.id.txv_preco_produto)
        val txvCategoria: TextView = view.findViewById(R.id.txv_categoria_produto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdutoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_produto, parent, false)
        return ProdutoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProdutoViewHolder, position: Int) {
        val produto = produtos[position]
        holder.txvTitulo.text = produto.title
        holder.txvPreco.text = String.format(Locale.US, "US$ %.2f", produto.price)
        holder.txvCategoria.text = produto.category
        holder.itemView.setOnClickListener { onClick(produto) }
    }

    override fun getItemCount(): Int = produtos.size
}
