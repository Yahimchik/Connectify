package org.example.socialnetwork.repo;

import org.example.socialnetwork.entities.User;
import org.example.socialnetwork.entities.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, UUID> {

    Optional<VerificationToken> findByToken(String token);

    @Query("select v.user from VerificationToken v where v.token = :token")
    Optional<User> findUserByToken(@Param("token") String token);

    @Modifying
    void deleteByToken(String token);
}
