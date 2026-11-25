package co.com.crediya.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SolicitudPageResponse(
        @JsonProperty("content") List<SolicitudResponse> content,
        @JsonProperty("page") int page,
        @JsonProperty("size") int size,
        @JsonProperty("total_elements") long totalElements
) {
}
