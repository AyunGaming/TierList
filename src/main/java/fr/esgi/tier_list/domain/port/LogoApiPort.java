package fr.esgi.tier_list.domain.port;

import java.io.InputStream;
import java.util.Optional;

public interface LogoApiPort {
    Optional<InputStream> fetchLogo(String domain);
}
