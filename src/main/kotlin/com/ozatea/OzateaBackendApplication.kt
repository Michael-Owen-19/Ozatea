package com.ozatea

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class OzateaBackendApplication

fun main(args: Array<String>) {
	runApplication<OzateaBackendApplication>(*args)
}
