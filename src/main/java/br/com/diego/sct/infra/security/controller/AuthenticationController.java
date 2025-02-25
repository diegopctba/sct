package br.com.diego.sct.infra.security.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.diego.sct.infra.security.domain.service.AuthenticationService;


@AllArgsConstructor
@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    private AuthenticationService authenticationService;

    @PostMapping
    @Operation(summary = "Endpoint para autenticação que retorna um token JWT")
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }
}
