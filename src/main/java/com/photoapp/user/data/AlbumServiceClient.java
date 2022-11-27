package com.photoapp.user.data;

import com.photoapp.user.api.model.AlbumResponse;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(
    name = "album-service",
//    fallback = AlbumServiceClient.AlbumServiceFallback.class
    fallbackFactory = AlbumServiceClient.AlbumServiceFallbackFactory.class
)
public interface AlbumServiceClient {

    @GetMapping("users/{id}/albums")
    List<AlbumResponse> getAlbums(@PathVariable String id);

    // Use fallback
//    @Slf4j
//    @Component
//    class AlbumServiceFallback implements AlbumServiceClient {
//
//        @Override
//        public List<AlbumResponse> getAlbums(String id) {
//            log.warn("Album service is down, fallback is used");
//            return List.of();
//        }
//    }

    // Use fallbackFactory
    @Component
    class AlbumServiceFallbackFactory implements FallbackFactory<AlbumServiceClient> {

        @Override
        public AlbumServiceClient create(Throwable cause) {
            return new AlbumServiceFallback(cause);
        }

    }

    @Slf4j
    @RequiredArgsConstructor
    class AlbumServiceFallback implements AlbumServiceClient {

        private final Throwable cause;

        @Override
        public List<AlbumResponse> getAlbums(String id) {
            if (cause instanceof FeignException && ((FeignException) cause).status() == 404) {
                 log.error("Error Not Found when calling getAlbums with userId: {}, cause: {}", id, cause.getMessage());
            } else {
                log.error("Unknown error when calling getAlbums: {}", cause.getMessage());
            }

            return List.of();
        }

    }

}
