package org.mkab.EventManagement.repository;


import org.mkab.EventManagement.entity.Jamat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JamatRepository extends JpaRepository<Jamat, Long> {
    boolean existsByName(String name);
}