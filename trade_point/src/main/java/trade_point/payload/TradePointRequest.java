package trade_point.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class TradePointRequest {
    @Pattern(regexp = "^[A-Za-zА-Яа-я -.']+$")
    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @Pattern(regexp = "^(\\d{4})$")
    @NotBlank
    private Double latitude;

    @Pattern(regexp = "^(\\d{4})$")
    @NotBlank
    private Double longitude;
}
