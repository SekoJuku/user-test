package kz.edu.astanait.usertest.repository;

import kz.edu.astanait.usertest.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    public Optional<Country> findByName(String name);
}
