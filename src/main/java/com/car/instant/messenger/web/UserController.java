package com.car.instant.messenger.web;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.car.instant.messenger.domain.entities.Conversation;
import com.car.instant.messenger.domain.models.binding.UserRegisterBindingModel;
import com.car.instant.messenger.domain.models.service.UserServiceModel;
import com.car.instant.messenger.service.UserService;

@Controller
public class UserController extends BaseController {
    private final UserService userService;

    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public ModelAndView login() {
        return this.view("login");
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return this.view("registration");
    }

    @PostMapping("/register")
    public ModelAndView registerPost(@ModelAttribute @Valid UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        //check for errors and if there are some errors return them to the user for displaying them
        if (this.userService.alreadyExistByEmail(userRegisterBindingModel.getEmail()) || (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) ||
                this.userService.alreadyExistByUsername(userRegisterBindingModel.getUsername()) || bindingResult.hasErrors()) {
            ModelAndView modelAndView = new ModelAndView();
            if (this.userService.alreadyExistByUsername(userRegisterBindingModel.getUsername())) {
                modelAndView.addObject("usernameAlready", true);
            }
            if (this.userService.alreadyExistByEmail(userRegisterBindingModel.getEmail())) {
                modelAndView.addObject("emailAlready", true);
            }
            if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
                modelAndView.addObject("match", true);
            }
            if (bindingResult.hasErrors()) {
                modelAndView.addObject("errors", bindingResult.getAllErrors());
            }
            return this.view("login", modelAndView);
        }

        this.userService.createUser(this.modelMapper.map(userRegisterBindingModel, UserServiceModel.class));
        return redirect("/login");
    }

}
