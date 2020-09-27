package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rmanager.commons.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>  {

    Optional<User> getUserByLogin(String login);

    @Query("select u from User u where u.deleted is null or u.deleted is false")
    List<User> findAllNotDeleted();

}
