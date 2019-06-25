package com.emt.laboratory.Labs.repositoryJPA;

import com.emt.laboratory.Labs.models.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile,Integer> {

    Optional<Profile> findByUsername(String username);

    Optional<Profile> findByEmail(String email);
}
