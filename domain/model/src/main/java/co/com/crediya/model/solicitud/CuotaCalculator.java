package co.com.crediya.model.solicitud;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public final class CuotaCalculator {

    private static final int DECIMAL_PLACES = 2;

    public static BigDecimal calcularCuota(BigDecimal monto, BigDecimal tasaInteresMensual, int plazoMeses) {
        if (monto == null || tasaInteresMensual == null || plazoMeses <= 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal i = tasaInteresMensual;
        if (i.compareTo(BigDecimal.ZERO) == 0) {
            return monto.divide(BigDecimal.valueOf(plazoMeses), DECIMAL_PLACES, RoundingMode.HALF_UP);
        }
        BigDecimal potencia = BigDecimal.ONE.add(i).pow(plazoMeses, MathContext.DECIMAL64);
        BigDecimal cuota = monto.multiply(i, MathContext.DECIMAL64)
                .divide(BigDecimal.ONE.subtract(BigDecimal.ONE.divide(potencia, MathContext.DECIMAL64), MathContext.DECIMAL64), MathContext.DECIMAL64);
        return cuota.setScale(DECIMAL_PLACES, RoundingMode.HALF_UP);
    }

    private CuotaCalculator() {
    }
}
