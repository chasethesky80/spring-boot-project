package se.magnus.api.composite.product;

import lombok.Builder;

@Builder
public record ServiceAddresses(String compositeAddress,
                               String productAddress,
                               String reviewAddress,
                               String recommendationAddress) {
}
