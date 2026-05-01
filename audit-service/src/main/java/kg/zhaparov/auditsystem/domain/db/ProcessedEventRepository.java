package kg.zhaparov.auditsystem.domain.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEventEntity, UUID> {


    @Modifying
    @Query(
            value = """
            INSERT INTO processed_events (id, processed_at)
            VALUES (:eventId, now())
            ON CONFLICT DO NOTHING
        """, nativeQuery = true
    )
    int insertIfNotExists(@Param("eventId") UUID eventId);

}
