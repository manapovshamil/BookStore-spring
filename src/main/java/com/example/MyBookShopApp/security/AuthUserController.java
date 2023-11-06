package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.security.jwt.JwtBlacklistService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class AuthUserController {

    private final BookstoreUserRegister userRegister;
    private final JwtBlacklistService jwtBlacklistService;

    @Autowired
    public AuthUserController(BookstoreUserRegister userRegister, JwtBlacklistService jwtBlacklistService) {
        this.userRegister = userRegister;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @GetMapping("/signin")
    public String handleSignin() {
        return "signin";
    }

    @GetMapping("/signup")
    public String handleSignUp(Model model) {
        model.addAttribute("regForm", new RegistrationForm());
        return "signup";
    }

    @PostMapping("/requestContactConfirmation")
    @ResponseBody
    public ContactConfirmationResponse handleRequestContactConfirmation(@RequestBody ContactConfirmationPayload contactConfirmationPayload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/reg")
    public String handleUserRegistration(RegistrationForm registrationForm, Model model) {
        userRegister.registerNewUser(registrationForm);
        model.addAttribute("regOk", true);
        return "signin";
    }

    @PostMapping("/approveContact")
    @ResponseBody
    public ContactConfirmationResponse handleApproveContact(@RequestBody ContactConfirmationPayload payload) {
        ContactConfirmationResponse response = new ContactConfirmationResponse();
        response.setResult("true");
        return response;
    }

    @PostMapping("/login")
    @ResponseBody
    public ContactConfirmationResponse handleLogin(@RequestBody ContactConfirmationPayload payload,
                                                   HttpServletResponse httpServletResponse) {
        try {
            ContactConfirmationResponse loginResponse = userRegister.jwtLogin(payload);
            if (loginResponse.getResult() != null) {
                Cookie cookie = new Cookie("token", loginResponse.getResult());
                httpServletResponse.addCookie(cookie);
            }
            return loginResponse;
        }catch (Exception e){
            return new ContactConfirmationResponse();
        }
    }

    @GetMapping("/my")
    public String handleMy() {
        return "my";
    }

    @GetMapping("/profile")
    public String handleProfile(Model model) {
        model.addAttribute("curUsr", userRegister.getCurrentUser());
        return "profile";
    }

    @GetMapping("/logout")
    public String handleLogout() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // Получение JWT токена из заголовка Authorization
        String authorizationHeader = request.getHeader("Authorization");
        String currentToken = null;
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            currentToken = authorizationHeader.substring(7); // Извлекаем токен без префикса "Bearer "
        }

        if (currentToken != null) {
            Claims claims = Jwts.parser().setSigningKey(currentToken).parseClaimsJws(currentToken).getBody();
            Date expiration = claims.getExpiration();
            // Добавление текущего токена в черный список
            jwtBlacklistService.addTokenToBlacklist(currentToken, expiration); // Ваш код для получения срока годности токена);
        }
/*
        HttpSession session = request.getSession();
        SecurityContextHolder.clearContext();
        if (session != null) {
            session.invalidate();
        }

        for (Cookie cookie : request.getCookies()) {
            cookie.setMaxAge(0);

        }*/

        return "redirect:/";
    }

}
