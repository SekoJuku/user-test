package kz.edu.astanait.usertest.repository;

import kz.edu.astanait.usertest.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CountryRepository extends JpaRepository<Country, Long> {
    public Optional<Country> findByName(String name);
}
