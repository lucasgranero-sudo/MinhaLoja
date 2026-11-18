package br.unisanta.minhaloja

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

/**
 * Tela de login: entra ou cria conta com e-mail e senha (Firebase Auth).
 * Se o usuario ja estiver logado, vai direto para a lista de produtos.
 */
class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var edtEmail: TextInputEditText
    private lateinit var edtSenha: TextInputEditText
    private lateinit var btnEntrar: Button
    private lateinit var btnCriarConta: Button
    private lateinit var txvErro: TextView
    private lateinit var progressLogin: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        // ja esta logado? entao pula o login
        if (auth.currentUser != null) {
            abrirProdutos()
            return
        }

        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edt_email)
        edtSenha = findViewById(R.id.edt_senha)
        btnEntrar = findViewById(R.id.btn_entrar)
        btnCriarConta = findViewById(R.id.btn_criar_conta)
        txvErro = findViewById(R.id.txv_erro_login)
        progressLogin = findViewById(R.id.progress_login)

        btnEntrar.setOnClickListener { entrar() }
        btnCriarConta.setOnClickListener { criarConta() }
    }

    private fun entrar() {
        val email = edtEmail.text.toString().trim()
        val senha = edtSenha.text.toString()
        if (!validarCampos(email, senha)) return

        mostrarCarregando(true)
        auth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                mostrarCarregando(false)
                abrirProdutos()
            }
            .addOnFailureListener { erro ->
                mostrarCarregando(false)
                mostrarErro(traduzirErro(erro))
            }
    }

    private fun criarConta() {
        val email = edtEmail.text.toString().trim()
        val senha = edtSenha.text.toString()
        if (!validarCampos(email, senha)) return

        mostrarCarregando(true)
        auth.createUserWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                mostrarCarregando(false)
                abrirProdutos()
            }
            .addOnFailureListener { erro ->
                mostrarCarregando(false)
                mostrarErro(traduzirErro(erro))
            }
    }

    // validacao antes de mandar pro Firebase
    private fun validarCampos(email: String, senha: String): Boolean {
        if (email.isEmpty() || senha.isEmpty()) {
            mostrarErro(getString(R.string.erro_campos_vazios))
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mostrarErro(getString(R.string.erro_email_invalido))
            return false
        }
        if (senha.length < 6) {
            mostrarErro(getString(R.string.erro_senha_curta))
            return false
        }
        txvErro.visibility = View.GONE
        return true
    }

    // transforma o erro do Firebase em uma mensagem em portugues
    private fun traduzirErro(erro: Exception): String {
        return when (erro) {
            is FirebaseAuthWeakPasswordException -> getString(R.string.erro_senha_curta)
            is FirebaseAuthUserCollisionException -> getString(R.string.erro_email_em_uso)
            is FirebaseAuthInvalidUserException -> getString(R.string.erro_usuario_nao_existe)
            is FirebaseAuthInvalidCredentialsException -> getString(R.string.erro_credenciais)
            is FirebaseNetworkException -> getString(R.string.erro_sem_internet)
            else -> erro.message ?: getString(R.string.erro_generico)
        }
    }

    private fun mostrarErro(mensagem: String) {
        txvErro.text = mensagem
        txvErro.visibility = View.VISIBLE
    }

    private fun mostrarCarregando(carregando: Boolean) {
        progressLogin.visibility = if (carregando) View.VISIBLE else View.GONE
        btnEntrar.isEnabled = !carregando
        btnCriarConta.isEnabled = !carregando
    }

    private fun abrirProdutos() {
        startActivity(Intent(this, ProdutosActivity::class.java))
        finish()
    }
}
