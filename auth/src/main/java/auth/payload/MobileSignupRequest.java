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

    //@Pattern(regexp = "^[A-Za-zА-Яа-я -.']*$")
    //private String firstName;

    //@Pattern(regexp = "^[A-Za-zА-Яа-я -.']*$")
    //private String lastName;

    @NotBlank
    private String phoneNumber;

    private String verifyCode;

    private UUID sessionID;
}
