package com.viamedsalud.gvp.controler;

import com.viamedsalud.gvp.modelo.User;
import com.viamedsalud.gvp.serviceInterface.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping
public class UserControler {
	
	@Autowired
	private IUserService service;

	@GetMapping("/User/listar")
	public String listar(Model model) {
		model.addAttribute("users", service.listar());
		return "usuarios";
	}
	@GetMapping("/User/listar/{id}")
	public String listarId(@PathVariable int id,Model model) {
		model.addAttribute("user", service.listarId(id));
		return "modificarusuario";
	}
	
	@GetMapping("/User/new")
	public String newUser(Model model) {
		model.addAttribute("user", new User());
		return "crearusuario";
	}
	
	@PostMapping("/User/save")
	public String saveUser(@Valid User u, Model model) {
		int id=service.saveUser(u);
		if(id==0) {
			return "modificarusuario";
		}
		return "redirect:/User/listar/";
	}
	
	@GetMapping("/User/delete/{id}")
	public String deleteUser(@PathVariable int id,Model model) {
		service.delete(id);
		return "redirect:/User/listar/";
	}
}
