package com.smart.controller;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
public class HomeController {
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "This is title sent from controller");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("abouttitle", "About--This is title sent from controller");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register");
		model.addAttribute("user",new User());
		return "signup";
	}
	@RequestMapping(value = "/do_register", method= RequestMethod.POST)
	public String registerUser(@Valid  @ModelAttribute("user") User user,BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model, HttpSession session) {
		
		try {
			if(!agreement) {
				System.out.println("You havn't accepted term's and condition... please check it...");
				throw new Exception("try again...");
			}
			
			if(bindingResult.hasErrors()) {
				System.out.printf("Error",bindingResult.toString());
				model.addAttribute("user",user);
				return "signup";
			}
			
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			System.out.println("Agreement" + agreement);
			System.out.println("USER "+ user);
			
			User result = this.userRepository.save(user);
			model.addAttribute("user", new User());
			
			session.setAttribute("message", new Message("Successfully Register","alert-success"));
			return "signup";
			
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Something Went Wrong!!" + e.getMessage(),"alert-danger"));
			return "signup";
		}
		
		
	}
	
	@RequestMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Login-Page....");
		return "login";
	}
	
	
	
	
}
