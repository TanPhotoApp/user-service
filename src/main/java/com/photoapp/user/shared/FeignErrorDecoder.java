package com.photoapp.user.shared;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        // MethodKey is in this format: <ClassSimpleName>#<MethodName>(<ParameterType>)
        // E.g: AlbumServiceClient#getAlbums(String)
        return switch (response.status()) {
            case 400 -> null; // handle bad request
            case 404 -> new ResponseStatusException(HttpStatus.valueOf(response.status()), "Client endpoint not found");
            default -> null;
        };
    }

}
