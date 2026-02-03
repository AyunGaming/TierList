package fr.esgi.tier_list.infrastructure.logo;

import fr.esgi.tier_list.domain.port.LogoApiPort;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.net.URI;
import java.util.Optional;

@Component
@Slf4j
public class LogoDevApiAdapter implements LogoApiPort {

    private static final String LOGO_DEV_API_URL = "https://img.logo.dev/%s?token=pk_gBxkfV3bSHCYWqRfPj3qUg";
    private final OkHttpClient httpClient;

    public LogoDevApiAdapter() {
        this.httpClient = new OkHttpClient();
    }

    @Override
    public Optional<InputStream> fetchLogo(String domain) {
        try {
            String url = String.format(LOGO_DEV_API_URL, domain);
            Request request = new Request.Builder()
                    .url(url)
                    .build();

            Response response = httpClient.newCall(request).execute();
            
            if (response.isSuccessful() && response.body() != null) {
                return Optional.of(response.body().byteStream());
            }
            
            log.warn("Failed to fetch logo for domain: {}, status: {}", domain, response.code());
            return Optional.empty();
            
        } catch (Exception e) {
            log.error("Error fetching logo for domain: {}", domain, e);
            return Optional.empty();
        }
    }
}
