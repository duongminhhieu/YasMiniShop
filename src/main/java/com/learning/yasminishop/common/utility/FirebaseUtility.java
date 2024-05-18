package com.learning.yasminishop.common.utility;

import lombok.NonNull;
import org.springframework.stereotype.Component;

@Component
public class FirebaseUtility {

    public String generatePublicUrl(@NonNull String bucketName, @NonNull String fileName) {
        return String.format("https://storage.googleapis.com/%s/%s", bucketName, fileName);
    }

    public String getExtension(@NonNull String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
}
