package co.com.crediya.model.solicitud.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    TIPO_PRESTAMO_NO_ENCONTRADO("El tipo de préstamo indicado no existe");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}