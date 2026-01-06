package fr.esgi.tier_list.domain.port;

import org.springframework.context.annotation.Bean;

import java.io.InputStream;

public interface ObjectStoragePort {
    void upload(String bucket, String objectName, InputStream data, String contentType);
}
