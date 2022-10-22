package elasticsearch_numeric_precision;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Result {

    private BigDecimal sum;

    private BigDecimal avg;

    @Override
    public String toString() {
        return "Result{" +
            "sum=" + sum +
            ", avg=" + avg +
            '}';
    }
}
