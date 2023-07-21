package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.SearchInput;

@Repository
public class ListingsRepository {

	private static final String C_LISTINGS = "listings";

	@Autowired
	private MongoTemplate mongoTemplate;

	//TODO: Task 2
	// db.listings.distinct("address.country")
	public List<String> getCountryList(){
		return mongoTemplate.findDistinct(new Query(), "address.country", C_LISTINGS, String.class);
	}
	
	//TODO: Task 3
	/* 	
	db.listings.find(
	{
		"address.country": {$regex: "australia", $options: "i"},
		accommodates: 2,
		price: {$gte: 1, $lte: 500}
	},
	{ name: 1, "address.street": 1, price: 1, "images.picture_url": 1 }
	).sort({price: -1})
	*/
	public List<Document> getListingsByInput(SearchInput input){
		Criteria criteria = Criteria.where("address.country").regex(input.getCountry(), "i")
			.and("accommodates").is(input.getPax())
			.and("price").gte(input.getMin()).lte(input.getMax());

		Query query = new Query(criteria).with(Sort.by(Direction.DESC , "price"));

		query.fields().include("name", "address.street", "price", "images.picture_url");

		return mongoTemplate.find(query, Document.class, C_LISTINGS);
	}	

	//TODO: Task 4
	

	//TODO: Task 5


}
