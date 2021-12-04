package pl.mrcwojcik.faction_torn.modules.login.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.mrcwojcik.faction_torn.modules.members.domain.Member;
import pl.mrcwojcik.faction_torn.modules.login.services.LoginService;

@CrossOrigin
@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/api/login/{apiKey}")
    public Member login(@PathVariable String apiKey){
        return loginService.login(apiKey);
    }

}
