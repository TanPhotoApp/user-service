package com.photoapp.user.data;

import com.photoapp.user.api.model.AlbumResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "album-service")
public interface AlbumServiceClient {

    Logger log = LoggerFactory.getLogger(AlbumServiceClient.class);

    @GetMapping("users/{id}/albums")
    @Retry(name = "album-service")
    @CircuitBreaker(name = "album-service", fallbackMethod = "getAlbumsFallBack")
    List<AlbumResponse> getAlbums(@PathVariable String id);

    default List<AlbumResponse> getAlbumsFallBack(String id, Throwable cause) {
        log.error("Get albums with id={} got error={}, message: {}",
            id, cause.getClass().getSimpleName(), cause.getMessage());

        return List.of();
    }

}
