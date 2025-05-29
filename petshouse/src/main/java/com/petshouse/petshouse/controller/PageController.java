package com.petshouse.petshouse.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.petshouse.petshouse.entity.Pet;
import com.petshouse.petshouse.entity.User;
import com.petshouse.petshouse.enums.PetType;
import com.petshouse.petshouse.mapper.UserMapper;
import com.petshouse.petshouse.security.JwtAuthentication;
import com.petshouse.petshouse.service.AuthService;
import com.petshouse.petshouse.service.FavoriteService;
import com.petshouse.petshouse.service.MessageService;
import com.petshouse.petshouse.service.PetService;
import com.petshouse.petshouse.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class PageController {

    private final AuthService authService;

    private final UserService userService;

    private final PetService petService;

    private final MessageService messageService;

    private final FavoriteService favoriteService;

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        List<String> countries = getCountryNames();
        model.addAttribute("countries", countries);
        return "register";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model) {
        return "login";
    }

    
    @GetMapping("/main")
    public String getMainPage(Model model, Authentication authentication) {
        model.addAttribute("petTypes", PetType.values());
        List<Pet> pets = petService.getAllAvailablePets();
        model.addAttribute("pets", pets);

        JwtAuthentication auth = authService.getAuthInfo();
        if (auth != null && auth.isAuthenticated()) {
            String login = auth.getLogin();
            User user = userService.getUserByLogin(login);
            if (user == null) {
                model.addAttribute("currentUser", null);
            } else {
                model.addAttribute("currentUser", UserMapper.toDto(user));
            }
        } else {
            model.addAttribute("currentUser", null);
        }

        return "main";
    }

    @GetMapping("/profile")
    public String getProfilePage(Model model) {
        JwtAuthentication auth = authService.getAuthInfo();
        if (auth != null && auth.isAuthenticated()) {
            String login = auth.getLogin();
            User user = userService.getUserByLogin(login);
            if (user != null) {
                model.addAttribute("currentUser", UserMapper.toDto(user));
            } else {
                model.addAttribute("currentUser", null);
            }
        } else {
            model.addAttribute("currentUser", null);
        }

        List<String> countries = getCountryNames();
        model.addAttribute("countries", countries);
        
        return "profile";
    }
    
    @GetMapping("/messages")
    public String getMessagesPage(Model model) {
        JwtAuthentication auth = authService.getAuthInfo();
        if (auth != null && auth.isAuthenticated()) {
            String login = auth.getLogin();
            User user = userService.getUserByLogin(login);
            if (user != null) {
                model.addAttribute("currentUser", UserMapper.toDto(user));
                model.addAttribute("messages", messageService.getAllMessagesByUser(user.getId()));
            } else {
                model.addAttribute("currentUser", null);
                model.addAttribute("messages", Collections.emptyList());
            }
        } else {
            model.addAttribute("currentUser", null);
            model.addAttribute("messages", Collections.emptyList());
        }

        return "messages";
    }

    @GetMapping("/favorites")
    public String getFavoritesPage(Model model) {
        JwtAuthentication auth = authService.getAuthInfo();
        if (auth != null && auth.isAuthenticated()) {
            String login = auth.getLogin();
            User user = userService.getUserByLogin(login);
            if (user != null) {
                model.addAttribute("currentUser", UserMapper.toDto(user));
                model.addAttribute("favoritePets", favoriteService.getFavoritePetsByUser(user));
            } else {
                model.addAttribute("currentUser", null);
                model.addAttribute("favoritePets", Collections.emptyList());
            }
        } else {
            model.addAttribute("currentUser", null);
            model.addAttribute("favoritePets", Collections.emptyList());
        }

        return "favorites";
    }

    private List<String> getCountryNames() {
        String[] countryCodes = Locale.getISOCountries();
        List<String> countryNames = new ArrayList<>();

        for (String code : countryCodes) {
            Locale locale = new Locale("", code);
            countryNames.add(locale.getDisplayCountry(Locale.ENGLISH));
        }

        Collections.sort(countryNames);
        return countryNames;
    }
}
