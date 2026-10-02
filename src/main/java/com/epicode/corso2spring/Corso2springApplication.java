package com.epicode.corso2spring;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan.
 * Avvia il server web incorporato e scansiona questo package alla ricerca di
 * @RestController, @Service, @Repository ecc.
 */
@SpringBootApplication
// Abilita i metodi @Scheduled (usati dal repository per il salvataggio periodico su file)
@EnableScheduling
public class Corso2springApplication {

    public static void main(String[] args) {
    SpringApplication.run(Corso2springApplication.class, args);
    }

    /**
     * @Bean: il valore restituito viene registrato nel contesto Spring.
     * springdoc lo legge per riempire titolo e descrizione della documentazione.
     */
    @Bean
    public OpenAPI apiInfo() {
    return new OpenAPI().info(new Info()
    .title("API Conti")
    .version("1.0")
    .description("API REST per gestire conti correnti (dati in memoria, persistiti su file txt)"));
    }

}