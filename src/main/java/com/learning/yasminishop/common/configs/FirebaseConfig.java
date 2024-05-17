package com.learning.yasminishop.common.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;


@Configuration
public class FirebaseConfig {

    @Value("${application.firebase.json-file}")
    private String serviceAccountKeyPath ;

    @Value("${application.firebase.storage-bucket}")
    private String storageBucket;

    @Bean
    public FirebaseApp firebaseInitialization() throws IOException {
        FileInputStream serviceAccount =
                new FileInputStream(serviceAccountKeyPath);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build();

        return FirebaseApp.initializeApp(options);
    }
}
