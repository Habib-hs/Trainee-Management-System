package com.backend.tms.utlis;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

public class FileService {
    public String uploadFile(MultipartFile file, String UPLOAD_DIR) {
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
}
