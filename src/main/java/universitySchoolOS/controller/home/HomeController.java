package universitySchoolOS.controller.home;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import universitySchoolOS.model.request.LoginReqDTO;
import universitySchoolOS.model.request.RegisterUserDTO;
import universitySchoolOS.model.response.LoginResponse;
import universitySchoolOS.service.UserService;

@RestController
@RequestMapping("/v1/api/auth")
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String auth() {
        return "auth controller";
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterUserDTO registerUserDTO) {
        return userService.registerUser(registerUserDTO);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginReqDTO loginReqDTO) {
        LoginResponse loginResponse = userService.verifyUser(loginReqDTO);

        return loginResponse;

    }

}
