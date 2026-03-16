package br.com.curso.kotlinintroducao.ui

import br.com.curso.kotlinintroducao.model.ComidaNordestina
import br.com.curso.kotlinintroducao.repository.ComidaRepository
import java.util.Locale
import java.util.NoSuchElementException
import java.util.Scanner

class Menu(
    private val repository: ComidaRepository,
    private val scanner: Scanner
) {
    private class EntradaFinalizadaException : RuntimeException()

    private fun lerLinhaOuFinalizar(): String {
        return try {
            scanner.nextLine()
        } catch (_: NoSuchElementException) {
            throw EntradaFinalizadaException()
        }
    }

    fun iniciar() {
        try {
            while (true) {
                println(
                    """
                    |===============================
                    | 1) Cadastrar
                    | 2) Listar
                    | 3) Pesquisar (por id)
                    | 4) Alterar (por id)
                    | 5) Remover (por id)
                    | 6) Finalizar
                    |===============================
                    """.trimMargin()
                )

                when (lerOpcaoMenu()) {
                    1 -> cadastrar()
                    2 -> listar()
                    3 -> pesquisar()
                    4 -> alterar()
                    5 -> remover()
                    6 -> return
                    else -> println("Opção inválida.")
                }
            }
        } catch (_: EntradaFinalizadaException) {
            return
        }
    }

    private fun lerOpcaoMenu(): Int {
        print("Escolha uma opção: ")
        return lerLinhaOuFinalizar().trim().toIntOrNull() ?: -1
    }

    private fun cadastrar() {
        println("Cadastro de comida nordestina")
        val id = lerIntPositivo("Id")
        val nome = lerStringObrigatoria("Nome")
        val estado = lerStringObrigatoria("Estado de origem")
        val preco = lerDoubleNaoNegativo("Preço")

        val comida = ComidaNordestina(
            id = id,
            nome = nome,
            estadoOrigem = estado,
            preco = preco
        )

        val cadastrado = repository.cadastrar(comida)
        if (cadastrado) {
            println("Cadastrado com sucesso.")
        } else {
            println("Já existe uma comida cadastrada com esse id.")
        }
    }

    private fun listar() {
        val comidas = repository.listar()
        if (comidas.isEmpty()) {
            println("Nenhum registro cadastrado.")
            return
        }

        val localePtBr = Locale("pt", "BR")
        comidas.forEach { comida ->
            val precoFormatado = String.format(localePtBr, "%.2f", comida.preco)
            println("${comida.id} - ${comida.nome} (${comida.estadoOrigem}) - R$ $precoFormatado")
        }
    }

    private fun pesquisar() {
        val id = lerIntPositivo("Id para pesquisa")
        val encontrada = repository.buscarPorId(id)
        encontrada?.let { comida ->
            println("Encontrada: ${comida.id} - ${comida.nome} (${comida.estadoOrigem}) - R$ ${comida.preco}")
        } ?: println("Nenhuma comida encontrada com esse id.")
    }

    private fun alterar() {
        val id = lerIntPositivo("Id para alterar")

        repository.buscarPorId(id)?.let { comida ->
            println("Deixe em branco para manter o valor atual.")

            print("Novo nome (${comida.nome}): ")
            val novoNome = lerLinhaOuFinalizar().trim().ifBlank { null }

            print("Novo estado de origem (${comida.estadoOrigem}): ")
            val novoEstado = lerLinhaOuFinalizar().trim().ifBlank { null }

            print("Novo preço (${comida.preco}): ")
            val entradaPreco = lerLinhaOuFinalizar().trim().ifBlank { null }
            val novoPreco = entradaPreco?.toDoubleOrNull()

            val nomeFinal = (novoNome ?: comida.nome).trim()
            val estadoFinal = (novoEstado ?: comida.estadoOrigem).trim()
            val precoFinal = novoPreco ?: comida.preco

            if (nomeFinal.isBlank()) {
                println("Nome não pode ser vazio.")
                return
            }
            if (estadoFinal.isBlank()) {
                println("Estado de origem não pode ser vazio.")
                return
            }
            if (precoFinal < 0.0) {
                println("Preço não pode ser negativo.")
                return
            }

            comida.nome = nomeFinal
            comida.estadoOrigem = estadoFinal
            comida.preco = precoFinal

            println("Alterado com sucesso.")
        } ?: println("Nenhuma comida encontrada com esse id.")
    }

    private fun remover() {
        val id = lerIntPositivo("Id para remover")
        val removido = repository.removerPorId(id)
        if (removido) {
            println("Removido com sucesso.")
        } else {
            println("Nenhuma comida encontrada com esse id.")
        }
    }

    private fun lerIntPositivo(rotulo: String): Int {
        while (true) {
            print("$rotulo: ")
            val valor = lerLinhaOuFinalizar().trim().toIntOrNull()
            if (valor != null && valor > 0) return valor
            println("Valor inválido. Digite um número inteiro maior que 0.")
        }
    }

    private fun lerDoubleNaoNegativo(rotulo: String): Double {
        while (true) {
            print("$rotulo: ")
            val valor = lerLinhaOuFinalizar().trim().replace(",", ".").toDoubleOrNull()
            if (valor != null && valor >= 0.0) return valor
            println("Valor inválido. Digite um número maior ou igual a 0.")
        }
    }

    private fun lerStringObrigatoria(rotulo: String): String {
        while (true) {
            print("$rotulo: ")
            val texto = lerLinhaOuFinalizar().trim()
            if (texto.isNotBlank()) return texto
            println("Campo obrigatório.")
        }
    }
}

