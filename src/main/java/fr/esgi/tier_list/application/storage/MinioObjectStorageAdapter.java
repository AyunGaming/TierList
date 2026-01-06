package fr.esgi.tier_list.application.storage;

import fr.esgi.tier_list.domain.port.ObjectStoragePort;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
public class MinioObjectStorageAdapter implements ObjectStoragePort {
    private MinioClient minioClient;

    public MinioObjectStorageAdapter(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public void upload(String bucket, String objectName, InputStream data, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectName)
                            .stream(data, -1, 10485760)
                            .contentType(contentType)
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Error while uploading to MinIO", e);
        }
    }
}
