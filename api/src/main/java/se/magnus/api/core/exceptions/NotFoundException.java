package se.magnus.api.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    private String message;
}
