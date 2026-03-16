package br.com.curso.kotlinintroducao

import br.com.curso.kotlinintroducao.repository.ComidaRepository
import br.com.curso.kotlinintroducao.ui.Menu
import java.util.Scanner

fun main() {
    val scanner = Scanner(System.`in`)
    val repository = ComidaRepository()
    val menu = Menu(repository, scanner)
    menu.iniciar()
}

