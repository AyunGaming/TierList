package fr.esgi.tier_list.application.storage;

import fr.esgi.tier_list.domain.port.ObjectStoragePort;
import fr.esgi.tier_list.infrastructure.config.MinioProperties;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class StockageService {

    private final ObjectStoragePort objectStoragePort;
    private final MinioProperties minioProperties;

    public void uploadPdf(String filename, InputStream data) {
        objectStoragePort.upload(
                minioProperties.getBucket(),
                filename,
                data,
                "application/pdf"
        );
    }
}
