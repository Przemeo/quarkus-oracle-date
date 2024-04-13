package com.przemekm;

import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Random;

@Slf4j
@ApplicationScoped
public class Repository {

    @Inject
    EntityManager entityManager;

    @Transactional
    public long saveEntity(Instant instant) {
        OffsetDateTime offsetDateTime = OffsetDateTime.ofInstant(instant, ZoneId.of("UTC"));
        //20:00Z
        log.info("OffsetDateTime: " + offsetDateTime);
        Timestamp timestamp = Timestamp.from(instant);
        //22:00+02:00
        log.info("Timestamp: " + timestamp);

        long id = new Random().nextLong(1, 2000);
        FirstEntity firstEntity = new FirstEntity(id, offsetDateTime, timestamp, instant);

        //Zapisuje sie ZAWSZE czas LOKALNY bez strefy czasowej, czyli 22:00 !!!
        entityManager.persist(firstEntity);
        entityManager.flush();
        entityManager.clear();

        return id;
    }

    @Transactional
    public FirstEntity getEntity(long id) {
        //To zwraca Asia/Seoul, czyli to co idzie w skrypcie z /home/sql/init.sql
        log.info((String) entityManager.createNativeQuery("SELECT DBTIMEZONE FROM DUAL").getSingleResult());
        //To zwraca Europe/Warsaw - dziwne, timezone JVM jest inny
        log.info((String) entityManager.createNativeQuery("SELECT SESSIONTIMEZONE FROM DUAL").getSingleResult());

        return entityManager.find(FirstEntity.class, id);
    }

}