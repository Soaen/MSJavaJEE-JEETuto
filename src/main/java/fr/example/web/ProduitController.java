package fr.example.web;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import fr.example.dao.ProduitRepository;
import fr.example.entities.Produit;

@Controller
public class ProduitController {
	@Autowired
	private ProduitRepository produitRepository;

	@RequestMapping(value = "/index")
	public String index(Model model, @RequestParam(name = "page", defaultValue = "0") int p,
			@RequestParam(name = "size", defaultValue = "10") int s,
			@RequestParam(name = "motCle", defaultValue = "") String mc) {
		Page<Produit> pageProduits = produitRepository.chercher("%" + mc + "%", PageRequest.of(p, s));

		model.addAttribute("listProduits", pageProduits.getContent());

		int[] pages = new int[pageProduits.getTotalPages()];
		model.addAttribute("pages", pages);
		model.addAttribute("size", s);
		model.addAttribute("pageCourante", p);
		model.addAttribute("motCle", mc);
		return "produits";
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public String delete(Long id, String motCle, int page, int size) {
		produitRepository.deleteById(id);
		return "redirect:/index?page=" + page + "&size=" + size + "&motCle=" + motCle;
	}

	@RequestMapping(value = "/form", method = RequestMethod.GET)
	public String formProduit(Model model) {
		model.addAttribute("produit", new Produit());
		return "FormProduit";
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String save(Model model,  @Valid  Produit produit, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			return "FormProduit";
		produitRepository.save(produit);
		return "Confirmation";
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public String edit(Model model, Long id) {
		Optional<Produit> p = produitRepository.findById(id);
		if (p.isPresent()) {
			Produit produit = p.get();
			model.addAttribute("produit", produit);
		}
		return "EditProduit";
	}
	
	
	@RequestMapping(value="/")
	public String home() {
		return "redirect:/index";
	}

	@RequestMapping(value="/403")
	public String accessDenied() {
		return "403";
	}
	
	@RequestMapping(value="/login")
	public String login() {
		return "login";
	}
	
}