package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vttp2023.batch3.assessment.paf.bookings.models.Details;
import vttp2023.batch3.assessment.paf.bookings.models.Listing;
import vttp2023.batch3.assessment.paf.bookings.models.SearchInput;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@Service
public class ListingsService {

	@Autowired
	private ListingsRepository repo;

	//TODO: Task 2
	public List<String> getCountryList(){
		return repo.getCountryList();
	}
	
	//TODO: Task 3
	public List<Listing> getListingsByInput(SearchInput input){
		List<Document> docList = repo.getListingsByInput(input);
		List<Listing> listings = new ArrayList<>();
		for(Document d : docList){
			Listing l = new Listing();
			l.setId(d.getString("_id"));
			l.setName(d.getString("name"));
			l.setPrice(d.getDouble("price"));
			l.setImage(d.get("images",Document.class).getString("picture_url"));
			l.setAddress(d.get("address", Document.class).getString("street"));
			listings.add(l);
		}
		return listings;

	}

	//TODO: Task 4
	public Optional<Details> getDetailsById(String id){
		List<Document> docList = repo.getListingDetails(id);
		if(docList.isEmpty()) return Optional.empty();
		Document d = docList.get(0);
		Details details = new Details();
		details.setId(d.getString("_id"));
		details.setDescription(d.getString("description"));
		details.setStreet(d.getString("street"));
		details.setSuburb(d.getString("suburb"));
		details.setCountry(d.getString("country"));
		details.setImage(d.getString("picture_url"));
		details.setPrice(d.getDouble("price"));
		details.setAmenities(d.getString("amenities"));
		return Optional.ofNullable(details);
	}
	

	//TODO: Task 5


}
