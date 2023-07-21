package vttp2023.batch3.assessment.paf.bookings.controllers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import vttp2023.batch3.assessment.paf.bookings.models.Details;
import vttp2023.batch3.assessment.paf.bookings.models.Listing;
import vttp2023.batch3.assessment.paf.bookings.models.Reservation;
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

	// TODO: Task 3
	@GetMapping("/search")
	public String searchAccoms(HttpSession session, Model model, @Valid SearchInput searchInput, BindingResult br) {

		if (br.hasErrors()) {
			List<String> countryList = service.getCountryList();
			model.addAttribute("countryList", countryList);
			return "view1";
		}

		List<Listing> listings = service.getListingsByInput(searchInput);
		model.addAttribute("country", searchInput.getCountry());
		model.addAttribute("listings", listings);
		// display error if no listings found
		return "view2";
	}

	// TODO: Task 4
	@GetMapping("/details")
	public String getListingDetails(@RequestParam String listingId, Model model, HttpSession session) {
		Optional<Details> detailsOp = service.getDetailsById(listingId);
		if (detailsOp.isEmpty())
			return "error";
		Details details = detailsOp.get();
		model.addAttribute("details", details);
		model.addAttribute("rsv", new Reservation());
		session.setAttribute("details", details);
		// add: back to view2
		return "view3";
	}

	// TODO: Task 5
	@PostMapping("/book")
	public String addReservation(@ModelAttribute Reservation rsv, BindingResult br, Model model, HttpSession session) {
		rsv.setId(UUID.randomUUID().toString().substring(0, 8));
		Details details = (Details) session.getAttribute("details");
		rsv.setAccId(details.getId());
		System.out.println(rsv.toString());
		Optional<String> rsvIdOpt = service.createReservation(rsv);
		if (rsvIdOpt.isEmpty()) {
			session.setAttribute("details", details);
			model.addAttribute("details", details);
			model.addAttribute("rsv", new Reservation());
			ObjectError err = new ObjectError("globalError", "Accommodation is not available");
			br.addError(err);
			return "view3";
		}

		model.addAttribute("rsvId", rsvIdOpt.get());
		return "view4";
	}

}
