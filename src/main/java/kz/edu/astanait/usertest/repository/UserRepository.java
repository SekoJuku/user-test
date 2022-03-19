package kz.edu.astanait.usertest.repository;

import kz.edu.astanait.usertest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
