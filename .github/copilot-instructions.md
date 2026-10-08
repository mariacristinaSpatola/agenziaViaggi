# Progetto agenzia viaggi — istruzioni per Copilot
 
## Stack
Java 26, Spring Boot 4, Maven, JUnit 5. , swagger-ui Nessuna libreria oltre a quelle nel pom.
 
## Struttura
controller/ parla HTTP e non contiene logica di business
service/    contiene le regole; non conosce HTTP
entity/     entita' per db
repository/ accesso ai dati
dto/        record di ingresso e di uscita; l'entita' interna non esce mai

 
## Convenzioni
- I DTO sono record, non classi.
- La validazione dell'input usa le annotazioni jakarta.validation con @Valid.
- Gli errori passano dal @RestControllerAdvice e restituiscono ProblemDetail.
- I nomi dei test descrivono il comportamento, in italiano
- L'accesso al database usa Spring Data JPA: i repository sono interfacce che estendono `JpaRepository`.
- Le query si definiscono con i metodi derivati dal nome o con `@Query` usando JPQL; evita JDBC e SQL nativo, salvo necessità esplicita.
 
## Comandi
mvn test           esegue i test
mvn spring-boot:run  avvia in locale sulla 8080