package br.com.curso.kotlinintroducao.model

data class ComidaNordestina(
    val id: Int,
    var nome: String,
    var estadoOrigem: String,
    var preco: Double
) {
    init {
        require(id > 0) { "Id deve ser maior que 0." }
        require(nome.isNotBlank()) { "Nome não pode ser vazio." }
        require(estadoOrigem.isNotBlank()) { "Estado de origem não pode ser vazio." }
        require(preco >= 0.0) { "Preço não pode ser negativo." }
    }
}

