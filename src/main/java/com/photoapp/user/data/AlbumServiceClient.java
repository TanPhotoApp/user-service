package com.photoapp.user.data;

import com.photoapp.user.api.model.AlbumResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "album-service")
public interface AlbumServiceClient {

    @GetMapping("users/{id}/albumss")
    List<AlbumResponse> getAlbums(@PathVariable String id);

}
