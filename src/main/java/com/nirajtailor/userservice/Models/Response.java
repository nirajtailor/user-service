package com.nirajtailor.userservice.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Response<T> {
    @Nullable
    private T body;
    private HttpStatus status;
}
