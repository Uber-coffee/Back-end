package auth.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
public class CreateUserRequest {

    @Email
    @NotBlank
    private String email;

    @Pattern(regexp = "^[A-Za-zА-Яа-я -.']+$")
    @NotBlank
    private String firstName;

    @Pattern(regexp = "^[A-Za-zА-Яа-я -.']+$")
    @NotBlank
    private String lastName;

    @Pattern(regexp = "^(\\+\\d{10,15})|(8\\d{10})$")
    @NotBlank
    private String phoneNumber;

}
