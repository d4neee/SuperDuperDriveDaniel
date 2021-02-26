package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {

    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public File getFile(int fileId) {
        return fileMapper.getFile(fileId);
    }

    public List<File> getAllFiles(int userId) {
        return fileMapper.getAllFiles(userId);
    }

    public int insertFile(MultipartFile fileUpload, int userId) throws IOException {
        File file = new File();

        file.setUserId(userId);
        file.setFilename(fileUpload.getOriginalFilename());
        file.setContentType(fileUpload.getContentType());
        file.setFileSize(Long.toString(fileUpload.getSize()));
        file.setFileData(fileUpload.getBytes());

        return fileMapper.insertFile(file);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }

    public boolean doesFileExist(MultipartFile fileUpload) {
        return fileMapper.getFileByFileName(fileUpload.getOriginalFilename()) != null;
    }
}
