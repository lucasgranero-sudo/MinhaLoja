# MinhaLoja

App Android feito para o desafio da aula: loja de produtos usando **Retrofit** (Fake Store API),
**Firebase Authentication** (login e cadastro com e-mail e senha) e **Cloud Firestore** (favoritos de cada usuário).

## Integrantes do grupo

- Lucas Loiro - RA: 206363
- Arthur Lima - RA: 243318

## O que o app faz

- Tela de login com e-mail e senha, botões **Entrar** e **Criar conta**
- Mensagens de erro em português (senha errada, e-mail inválido, senha com menos de 6 caracteres, e-mail já cadastrado)
- Se o usuário já estiver logado, o app abre direto na lista de produtos
- Lista de produtos (`GET /products`) em um `RecyclerView` com título, preço e categoria
- Aviso de carregamento e mensagem de erro com botão "Tentar novamente"
- Tocar em um produto salva nos favoritos do usuário no Firestore
- Tela **Meus favoritos** (do mais recente para o mais antigo) com botão **Remover**
- Botão **Sair** que desloga e volta para o login

## Estrutura do código

```
app/src/main/java/br/unisanta/minhaloja/
├── Produto.kt            -> data class do produto (+ Rating)
├── Favorito.kt           -> data class do favorito salvo no Firestore
├── ProdutoService.kt     -> interface do Retrofit (GET /products)
├── RetrofitClient.kt     -> cria o Retrofit com a URL base
├── LoginActivity.kt      -> login e cadastro (Firebase Auth)
├── ProdutosActivity.kt   -> lista de produtos e salvar favorito
├── FavoritosActivity.kt  -> lista e remove favoritos
├── ProdutoAdapter.kt     -> adapter do RecyclerView de produtos
└── FavoritoAdapter.kt    -> adapter do RecyclerView de favoritos
```

Estrutura no Firestore:

```
usuarios/{uid}/favoritos/{id do produto}
    id, titulo, preco, categoria, salvoEm
```

## Como rodar o projeto

1. Clone o repositório e abra a pasta no **Android Studio**.
2. No [Console do Firebase](https://console.firebase.google.com/), crie um projeto e adicione um app Android
   com o pacote **`br.unisanta.minhaloja`**.
3. Baixe o `google-services.json` e coloque dentro da pasta `app/`
   (ele não está no repositório porque está no `.gitignore`).
4. No Firebase, ative **Authentication → Método de login → E-mail/senha**.
5. Crie o **Firestore Database** em modo de teste.
6. Clique em **Sync Project with Gradle Files** e depois rode o app no emulador ou celular.

## Regras de segurança (extra)

O arquivo [`firestore.rules`](firestore.rules) tem regras para cada usuário só ler e gravar
em `usuarios/{seu uid}`. Para usar, copie o conteúdo para a aba **Regras** do Firestore no console.
