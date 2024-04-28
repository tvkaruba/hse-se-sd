package spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class SpringApplication

fun main(args: Array<String>) {
	runApplication<SpringApplication>(*args)
}
