package se.magnus.api.composite.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public record ServiceAddresses(String compositeAddress,
                               String productAddress,
                               String reviewAddress,
                               String recommendationAddress) {
}
