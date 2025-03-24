package com.villysiu.yumtea.service.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Service
public class FileSystemStorageService implements StorageService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if(properties.getLocation().trim().length() == 0){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        logger.info("Initializing file system storage.");
        try {

            Files.createDirectories(rootLocation);
            logger.info("Filesystem storage directory created.");
        }
        catch (IOException e) {
            logger.error(e.getMessage());
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public void store(MultipartFile file) {
        logger.info("Storing file: {}", file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                logger.error("File is empty.");
                throw new StorageException("Failed to store empty file.");
            }

            Path destinationFile = this.rootLocation.resolve(
                            Paths.get(file.getOriginalFilename()))
                    .normalize().toAbsolutePath();


            if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
                logger.error("failed to store file outside of storage directory.");
                // This is a security check
                throw new StorageException(
                        "Cannot store file outside current directory.");
            }
            logger.info("Saving file: {}", file.getOriginalFilename());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
            logger.info("Successfully stored file: {}", file.getOriginalFilename());
        }
        catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

//    @Override
//    public Stream<Path> loadAll() {
//        try {
//            return Files.walk(this.rootLocation, 1)
//                    .filter(path -> !path.equals(this.rootLocation))
//                    .map(this.rootLocation::relativize);
//        }
//        catch (IOException e) {
//            throw new StorageException("Failed to read stored files", e);
//        }
//
//    }
//
//    @Override
//    public Path load(String filename) {
//        return rootLocation.resolve(filename);
//    }
//
//    @Override
//    public Resource loadAsResource(String filename) {
//        try {
//            Path file = load(filename);
//            Resource resource = new UrlResource(file.toUri());
//            if (resource.exists() || resource.isReadable()) {
//                return resource;
//            }
//            else {
//                throw new StorageFileNotFoundException(
//                        "Could not read file: " + filename);
//
//            }
//        }
//        catch (MalformedURLException e) {
//            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
//        }
//    }
//
//    @Override
//    public void deleteAll() {
//        FileSystemUtils.deleteRecursively(rootLocation.toFile());
//    }
//

}
