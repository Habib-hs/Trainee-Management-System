package com.backend.tms.utlis;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileService {
    public static String uploadFile(MultipartFile file, String UPLOAD_DIR) {
        if (file != null && !file.isEmpty()) {
            try {
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                File destinationDir = new File(UPLOAD_DIR);
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs(); // Create the directory if it doesn't exist
                }
                File destinationFile = new File(destinationDir, fileName);
                file.transferTo(destinationFile);
                return destinationFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String uploadImage(MultipartFile imageFile, String UPLOAD_DIR) {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Check if the uploaded file is an image
            if (!isValidImageContentType(imageFile.getContentType())) {
                throw new IllegalArgumentException("Only image files are allowed");
            }

            try {
                String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                File destinationDir = new File(UPLOAD_DIR);
                if (!destinationDir.exists()) {
                    destinationDir.mkdirs(); // Create the directory if it doesn't exist
                }
                File destinationFile = new File(destinationDir, fileName);
                imageFile.transferTo(destinationFile);
                return destinationFile.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static boolean isValidImageContentType(String contentType) {
        return contentType != null && contentType.startsWith("image/");
    }

}
