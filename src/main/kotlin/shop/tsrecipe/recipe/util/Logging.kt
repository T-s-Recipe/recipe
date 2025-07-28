package shop.tsrecipe.recipe.util

import mu.KLogger
import mu.KotlinLogging

interface Logging {
    val logger: KLogger
        get() = KotlinLogging.logger(this::class.java.name)
}