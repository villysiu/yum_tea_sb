package com.villysiu.yumtea.service.storage;

import org.springframework.web.multipart.MultipartFile;

// https://spring.io/guides/gs/uploading-files
public interface StorageService {
    void init();

    void store(MultipartFile file);

//    Stream<Path> loadAll();
//
//    Path load(String filename);
//
//    Resource loadAsResource(String filename);
//
//    void deleteAll();
}
