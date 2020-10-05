package rmanager.commons.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import rmanager.commons.entity.Img;
import rmanager.commons.exception.SaveImgException;
import rmanager.commons.property.ImgProperty;
import rmanager.commons.repository.ImgRepository;
import rmanager.commons.service.ImgService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

@Service
public class ImgServiceImpl implements ImgService {

    private final Path fileStorageLocation;

    private ImgProperty imgProperty;
    private ImgRepository imgRepository;

    @Autowired
    public ImgServiceImpl(ImgProperty imgProperty,
                          ImgRepository imgRepository) throws SaveImgException {
        this.imgProperty = imgProperty;
        this.imgRepository = imgRepository;
        this.fileStorageLocation = Paths.get(imgProperty.getDir()).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new SaveImgException(ex.getMessage());
        }
    }

    @Override
    public Img getById(Integer id) {
        return imgRepository.findById(id).orElse(null);
    }

    @Override
    public List<Img> getAll() {
        return imgRepository.findAll();
    }

    @Override
    public Img save(Img img) {
        return imgRepository.saveAndFlush(img);
    }

    @Override
    public void delete(Img img) {
        imgRepository.delete(img);
    }

    @Override
    public String saveImg(MultipartFile imgFile) throws SaveImgException {
        String fileName = StringUtils.cleanPath(imgFile.getOriginalFilename());
        fileName = fileName.replaceAll("\\s+","");
        fileName = new Date().getTime() + "_" + fileName;
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new SaveImgException("Name contains invalid characters");
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(imgFile.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();

        } catch (IOException ex) {
            throw new SaveImgException(ex.getMessage());
        }
    }
}
