package br.com.curso.kotlinintroducao.repository

import br.com.curso.kotlinintroducao.model.ComidaNordestina

class ComidaRepository {
    private val comidas = mutableListOf<ComidaNordestina>()

    fun cadastrar(comida: ComidaNordestina): Boolean {
        if (comidas.any { it.id == comida.id }) return false
        comidas.add(comida)
        return true
    }

    fun listar(): List<ComidaNordestina> = comidas.toList()

    fun buscarPorId(id: Int): ComidaNordestina? = comidas.find { it.id == id }

    fun removerPorId(id: Int): Boolean {
        val indice = comidas.indexOfFirst { it.id == id }
        if (indice < 0) return false
        comidas.removeAt(indice)
        return true
    }
}

