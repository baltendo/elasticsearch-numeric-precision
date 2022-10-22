package elasticsearch_numeric_precision;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
public final class BigDecimalUtil {

    private BigDecimalUtil() {

    }

    public static BigDecimal sum(List<BigDecimal> decimals) {
        return decimals.stream()
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public static BigDecimal withScale(double d, int scale, RoundingMode roundingMode) {
        return new BigDecimal(d).setScale(scale, roundingMode);
    }

    public static BigDecimal combine(BigDecimal integral, BigDecimal fractional, int scale, RoundingMode roundingMode) {
        BigDecimal bound = withScale(Math.pow(10, scale), scale, roundingMode);
        BigDecimal realFractional = fractional.divide(bound, roundingMode);
        return integral.add(realFractional);
    }

}
