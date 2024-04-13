package com.przemekm;

import io.quarkus.test.junit.QuarkusTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.TimeZone;

@Slf4j
@QuarkusTest
class RepositoryTest {

    @Inject
    Repository repository;

    @Test
    void test() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Warsaw"));
        //Przy stworzeniu instant ma offset UTC, jest 20:00Z
        Instant instant = Instant.now();
        log.info("Instant: " + instant);

        long id = repository.saveEntity(instant);

        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        FirstEntity entity = repository.getEntity(id);

        //22:00+09:00 !!!
        log.info("After OffsetDateTime: " + entity.getCreateDate());
        log.info("After OffsetDateTime: " + entity.getCreateDate().withOffsetSameInstant(ZoneOffset.UTC));
        //22:00+09:00 !!!
        log.info("After Timestamp: " + entity.getCreateTimestamp());
        //Ponizsza konwersja bierze pod uwage timezone JVM
        log.info("After Timestamp: " + OffsetDateTime.ofInstant(entity.getCreateTimestamp().toInstant(), ZoneId.of("UTC")));
        //13:00Z !!!
        log.info("After Instant: " + entity.getCreateTimestampInstant());
        log.info("After Instant: " + OffsetDateTime.ofInstant(entity.getCreateTimestampInstant(), ZoneId.of("UTC")));
        //Mozna naprawic to robiac zapis/odczyt z quarkus.hibernate-orm.jdbc.timezone=UTC !!!

        //Jesli zapis zostal zjebany i juz tam weszly dane bez UTC (np. Europe/Warsaw) i potem bedziemy chcieli te dane odczytac, to nie mamy szans odgadnac jaka byla strefa czasowa zapisu
        //Dane sa zapisane bez strefy czasowej
        //To, ze fix dziala pobierajac SESSIONTIMEZONE z bazy (nadajac kontekst pobranej dacie na jego podstawie) to czysty przypadek,
        // moze na bazie w Korei jest domyslnie Europe/Warsaw albo jest nadpisywane przez ALTER SESSION SET TIME_ZONE?
        //Strefa czasowa bazy DBTIMEZONE nie ma (chyba) zadnego znaczenia przy normalnym zapisie/odczycie
        //Wniosek: chyba tam w Korei planning zapisuje do bazy z Europe/Warsaw i SESSIONTIMEZONE jest Europe/Warsaw, a timezone JVM jest Asia/Seoul
        Assertions.assertTrue(true);
    }

}