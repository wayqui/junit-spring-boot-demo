package com.wayqui.demo.config;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@Slf4j
@EnableWebMvc
public class ApplicationConfig implements WebMvcConfigurer {

    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        log.info("Configuring new Gson message converter...");

        GsonHttpMessageConverter gsonConverter = new GsonHttpMessageConverter();
        gsonConverter.setGson(gson());
        converters.add(gsonConverter);
    }

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDate.class,
                        (JsonSerializer<LocalDate>) (localDate, type, context)
                        -> new JsonPrimitive(localDate.format(DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT))))
                .registerTypeAdapter(LocalDate.class,
                        (JsonDeserializer<LocalDate>) (jsonElement, type, context)
                        -> LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ofPattern(LOCAL_DATE_FORMAT)))
                .create();
    }
}
