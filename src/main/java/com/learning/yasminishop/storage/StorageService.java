package com.learning.yasminishop.storage;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.learning.yasminishop.common.entity.Storage;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.common.utility.FirebaseUtility;
import com.learning.yasminishop.storage.dto.response.StorageResponse;
import com.learning.yasminishop.storage.mapper.StorageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StorageService {

    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;

    private final FirebaseUtility firebaseUtility;


    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public StorageResponse saveFile(MultipartFile file, String folder) {
        try {
            String name = UUID.randomUUID().toString().concat(firebaseUtility.getExtension(Objects.requireNonNull(file.getOriginalFilename())));
            String url = saveFileToFirebase(file, folder + "/" + name);
            Storage storage = Storage.builder()
                    .name(name)
                    .type(file.getContentType())
                    .url(url)
                    .size(file.getSize())
                    .build();

            return storageMapper.toStorageResponse(storageRepository.save(storage));
        } catch (IOException e) {
            log.error("Error saving file: {}", e.getMessage());
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    private String saveFileToFirebase(MultipartFile file, String name) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();
        Blob blob = bucket.create(name, file.getBytes(), file.getContentType());
        blob.createAcl(Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));

        return firebaseUtility.generatePublicUrl(bucket.getName(), name);
    }



}
