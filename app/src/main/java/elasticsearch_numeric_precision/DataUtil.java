package elasticsearch_numeric_precision;

import lombok.extern.slf4j.Slf4j;

import java.security.SecureRandom;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public final class DataUtil {

    private static final SecureRandom RANDOM = new SecureRandom();

    private DataUtil() {
        
    }

    public static List<String> generate(int number, int maxDigitsIntegral, int maxDigitsFractional) {
        List<String> lines = new LinkedList<>();

        for (int i = 0; i < number; i++) {
            long integralBound = getBoundForRandomDigit(maxDigitsIntegral + 1);
            long integral = RANDOM.nextLong(integralBound);

            long fractionalBound = getBoundForRandomDigit(maxDigitsFractional + 1);
            long fractional = RANDOM.nextLong(fractionalBound);

            String value = integral + "." + fractional;
            log.info("value: {}", value);

            lines.add(value);
        }

        log.info("lines: {}", lines.size());
        return lines;
    }

    /**
     * For 3 digits the bound is 1000 which means numbers from 0 to 999
     */
    private static long getBoundForRandomDigit(int maxDigits) {
        long digits = RANDOM.nextLong(maxDigits);
        return Math.round(Math.pow(10, digits));
    }

}
