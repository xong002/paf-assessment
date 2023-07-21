package vttp2023.batch3.assessment.paf.bookings.services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	

	//TODO: Task 5


}
