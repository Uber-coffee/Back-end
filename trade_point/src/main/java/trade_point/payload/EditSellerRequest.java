package trade_point.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@NoArgsConstructor
public class EditSellerRequest {
    @NotBlank
    private Long idSeller;
}
