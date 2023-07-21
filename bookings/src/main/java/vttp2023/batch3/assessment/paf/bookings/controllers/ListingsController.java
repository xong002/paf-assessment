package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import vttp2023.batch3.assessment.paf.bookings.models.Listing;
import vttp2023.batch3.assessment.paf.bookings.models.SearchInput;
import vttp2023.batch3.assessment.paf.bookings.services.ListingsService;

@Controller
@RequestMapping("/")
public class ListingsController {

	@Autowired
	private ListingsService service;

	// TODO: Task 2
	@GetMapping("/")
	public String landingPage(Model model) {
		List<String> countryList = service.getCountryList();
		model.addAttribute("searchInput", new SearchInput());
		model.addAttribute("countryList", countryList);
		return "view1";
	}

	@GetMapping(path = "/search")
	public String searchAccoms(Model model, @Valid SearchInput searchInput, BindingResult br) {

		if (br.hasErrors()) {
			List<String> countryList = service.getCountryList();
			model.addAttribute("countryList", countryList);
			return "view1";
		}

		List<Listing> listings = service.getListingsByInput(searchInput);
		model.addAttribute("country", searchInput.getCountry());
		model.addAttribute("listings", listings);
		//display error if no listings found
		return "view2";
	}

	// TODO: Task 3


	// TODO: Task 4

	// TODO: Task 5

}
