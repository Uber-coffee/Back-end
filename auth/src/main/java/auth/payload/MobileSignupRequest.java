package auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileSignupRequest {

    @NotBlank
    private String phoneNumber;

    private String verifyCode;

    private String sessionID;
}
