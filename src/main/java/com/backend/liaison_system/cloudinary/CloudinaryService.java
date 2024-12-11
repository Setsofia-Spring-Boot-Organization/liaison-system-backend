package com.backend.liaison_system.cloudinary;

import com.backend.liaison_system.exception.Error;
import com.backend.liaison_system.exception.LiaisonException;
import com.backend.liaison_system.exception.Message;
import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class CloudinaryService {

    @Value("${cloudinary.url}")
    private String CLOUDINARY_URL;

    public String uploadProfileImage(MultipartFile multipartFile) throws IOException {

        Cloudinary cloudinary = new Cloudinary(CLOUDINARY_URL);
        cloudinary.config.secure = true;

        byte[] bytes = multipartFile.getBytes();

        if (bytes.length > 10485760) {
            throw new LiaisonException(Error.FILE_SIZE_TOO_LARGE, new Throwable(Message.THE_FILE_SIZE_IS_BIGGER_THAN_10MB.label + "file name = " + multipartFile.getOriginalFilename() + " size = " + multipartFile.getSize()));
        }

        // Upload the image and retrieve its url
        var uploadResult = cloudinary.uploader().uploadLarge(bytes, Map.of("folder", "Liaison"));

        return uploadResult.get("secure_url").toString();
    }
}
