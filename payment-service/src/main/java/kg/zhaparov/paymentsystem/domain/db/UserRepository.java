package kg.zhaparov.paymentsystem.domain.db;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = "payments")
    @Query("select u from UserEntity u")
    List<UserEntity> findAllWithUsers();

    boolean existsByEmailIgnoreCase(String email);

}
