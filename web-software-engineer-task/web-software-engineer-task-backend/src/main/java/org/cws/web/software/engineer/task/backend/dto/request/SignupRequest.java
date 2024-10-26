package org.cws.web.software.engineer.task.backend.dto.request;

import java.util.HashSet;
import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupRequest {

    @NotBlank
    @Size(min = 3, max = 20)
    private String      username;

    @NotBlank
    @Size(max = 50)
    @Email
    private String      email;

    private Set<String> role = new HashSet<>();

    @NotBlank
    @Size(min = 6, max = 40)
    private String      password;

}
