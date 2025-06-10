package com.tsrecipe.recipe

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RecipeApplication

suspend fun main(args: Array<String>) {
	runApplication<RecipeApplication>(*args)
}
