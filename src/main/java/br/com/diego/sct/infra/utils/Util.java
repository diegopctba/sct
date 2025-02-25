package br.com.diego.sct.infra.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Util {
    public static String LOG_PREFIX;

    @Value("[${spring.application.name}]")
    public void setPrefix(final String prefix) {
        LOG_PREFIX = prefix;
    }
}