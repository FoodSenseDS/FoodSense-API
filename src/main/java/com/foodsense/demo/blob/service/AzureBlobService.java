package com.foodsense.demo.blob.service;

import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;

@Service
public class AzureBlobService {

    @Autowired
    private BlobContainerClient blobContainerClient;

    public String uploadImage(MultipartFile file) throws IOException {
        String uniqueFilename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobClient blob = blobContainerClient.getBlobClient(uniqueFilename);
        blob.upload(file.getInputStream(), file.getSize());
        return blob.getBlobUrl();
    }
}
