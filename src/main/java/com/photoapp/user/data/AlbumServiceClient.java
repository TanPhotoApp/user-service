package com.photoapp.user.data;

import com.photoapp.user.api.model.AlbumResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "album-service")
public interface AlbumServiceClient {

    @GetMapping("users/{id}/albums")
    @CircuitBreaker(name = "album-service", fallbackMethod = "getAlbumsFallBack")
    List<AlbumResponse> getAlbums(@PathVariable String id);

    default List<AlbumResponse> getAlbumsFallBack(String id, Throwable cause) {
        System.err.printf("Get albums with id=%s got error: %s%n", id, cause.getMessage());

        return List.of();
    }

}
