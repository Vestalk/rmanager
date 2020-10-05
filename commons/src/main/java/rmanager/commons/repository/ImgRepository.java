package rmanager.commons.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rmanager.commons.entity.Img;

@Repository
public interface ImgRepository extends JpaRepository<Img, Integer> {
}
