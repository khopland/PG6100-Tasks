package org.tsdes.auth
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.annotation.Bean


@SpringBootApplication(scanBasePackages = ["org.tsdes"])
class Application{

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}