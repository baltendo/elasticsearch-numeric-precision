package elasticsearch_numeric_precision;

import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

@Slf4j
public final class ResultUtil {

    private ResultUtil() {

    }

    public static Result difference(Result a, Result b) {
        return Result.builder()
            .sum(differenceAbsolute(a.getSum(), b.getSum()))
            .avg(differenceAbsolute(a.getAvg(), b.getAvg()))
            .build();
    }

    private static BigDecimal differenceAbsolute(BigDecimal a, BigDecimal b) {
        return a.subtract(b).abs();
    }
}
