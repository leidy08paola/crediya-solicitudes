package co.com.crediya.model.solicitud;

import java.util.List;

public record PageResult<T>(List<T> content, int page, int size, long totalElements) {
}
