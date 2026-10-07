package universitySchoolOS.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import universitySchoolOS.model.enums.Roles;

@Data
public class RegisterUserDTO {

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", unique = true)
    private String password;

    @Column(name = "user_type", nullable = true)
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private Roles roles;

    @Column(name = "contact_number",  nullable = false)
    private String contactNumber;

}
