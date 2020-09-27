package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import rmanager.commons.entity.UserToken;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, String> {

    UserToken getUserTokenByToken(String token);

    @Modifying
    @Transactional
    @Query("delete from UserToken u where u.userId = ?1")
    void clearTokensForUser(Integer userId);

}
