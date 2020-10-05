package rmanager.commons.service;

import org.springframework.web.multipart.MultipartFile;
import rmanager.commons.entity.Img;
import rmanager.commons.exception.SaveImgException;

import java.util.List;

public interface ImgService {

    Img getById(Integer id);

    List<Img> getAll();

    Img save(Img img);
    void delete(Img Img);

    String saveImg(MultipartFile imgFile) throws SaveImgException;
}
