package auth.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MobileSignupRequest {

    @Pattern(regexp = "^[A-Za-zА-Яа-я -.']+$")
    @NotBlank
    private String firstName;

    @Pattern(regexp = "^[A-Za-zА-Яа-я -.']+$")
    @NotBlank
    private String lastName;

    @NotBlank
    private String idToken;
}
