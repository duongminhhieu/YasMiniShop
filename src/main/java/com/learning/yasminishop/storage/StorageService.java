package com.learning.yasminishop.storage;

import com.google.cloud.storage.Acl;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import com.learning.yasminishop.common.entity.Storage;
import com.learning.yasminishop.common.exception.AppException;
import com.learning.yasminishop.common.exception.ErrorCode;
import com.learning.yasminishop.storage.dto.response.StorageResponse;
import com.learning.yasminishop.storage.mapper.StorageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StorageService {

    private final StorageRepository storageRepository;
    private final StorageMapper storageMapper;


    @Transactional
    public StorageResponse saveFile(MultipartFile file, String folder) {
        try {
            String name = file.getOriginalFilename();
            String url = saveFileToFirebase(file, folder + "/" + name);
            Storage storage = Storage.builder()
                    .name(name)
                    .type(file.getContentType())
                    .url(url)
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

        return blob.getMediaLink();
    }


}
