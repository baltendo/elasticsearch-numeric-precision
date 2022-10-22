package elasticsearch_numeric_precision;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Model {

    private String currency;

    private long line;

    @JsonIgnore
    private BigDecimal bigDecimal;

    @JsonProperty("amount.double")
    private double amountDouble;

    @JsonProperty("amount.float")
    private double amountFloat;

    @JsonProperty("amount.scaled_float")
    private double amountScaledFloat;

    @JsonProperty("amount.integral")
    private long amountIntegral;

    @JsonProperty("amount.fractional")
    private long amountFractional;
}
