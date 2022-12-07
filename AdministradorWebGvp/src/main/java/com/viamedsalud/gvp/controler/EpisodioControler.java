package com.viamedsalud.gvp.controler;

import com.viamedsalud.gvp.modelo.Episodio;
import com.viamedsalud.gvp.serviceInterface.IEpisodioService;
import com.viamedsalud.gvp.util.EpisodioExporterPDF;
import com.lowagie.text.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@Controller
@RequestMapping
public class EpisodioControler {
	
	@Autowired
	private IEpisodioService service;

	@GetMapping("/Episodio/listar")
	public String listar(Model model) {
		model.addAttribute("episodios", service.listar());
		return "episodios";
	}
	@GetMapping("/Episodio/listar/{id}")
	public String listarId(@PathVariable int id,Model model) {
		model.addAttribute("episodio", service.listarId(id));
		return "modificarepisodio";
	}
	
	@GetMapping({"/Episodio/new", "","/"})
	public String newEpisodio(Model model) {
		model.addAttribute("episodio", new Episodio());
		return "crearepisodio";
	}
	
	@PostMapping("/Episodio/save")
	public String saveEpisodio(@Valid Episodio u, Model model) {
		int id=service.saveEpisodio(u);
		if(id==0) {
			return "modificarepisodio";
		}
		return "redirect:/Episodio/listar";
	}
	
	@GetMapping("/Episodio/delete/{id}")
	public String deleteEpisodio(@PathVariable int id,Model model) {
		service.delete(id);
		return "redirect:/Episodio/listar";
	}

	@GetMapping("/Episodio/detalles/{id}")
	public String detallesId(@PathVariable int id,Model model) {
		model.addAttribute("episodio", service.listarId(id));
		return "detallesepisodio";
	}

	@GetMapping("/Episodio/exportarPDF/{id}")
	public void exportarListadoDeEmpleadosEnPDF(@PathVariable int id,HttpServletResponse response) throws DocumentException, IOException {
		response.setContentType("application/pdf");

		String cabecera = "Content-Disposition";
		String valor = "attachment; filename=Episodio.pdf";

		response.setHeader(cabecera, valor);

		Episodio episodio = service.findByIdAndId(id,id);

		EpisodioExporterPDF exporter = new EpisodioExporterPDF(episodio);
		exporter.exportar(response);
	}
}
