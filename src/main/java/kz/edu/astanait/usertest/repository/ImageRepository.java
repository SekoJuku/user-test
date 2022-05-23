package kz.edu.astanait.usertest.repository;

import kz.edu.astanait.usertest.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ImageRepository extends JpaRepository<Image, Long> {
    Optional<Image> findByUserId(Long id);
}
