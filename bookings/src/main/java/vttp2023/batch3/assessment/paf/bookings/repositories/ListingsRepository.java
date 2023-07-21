package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoExpression;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
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
	/* 	
	db.listings.aggregate([
	{
		$match: { _id: "27498126"}
	}
	,{
		$project: {
			_id: 1,
			description: 1,
			"address.street": 1,
			"address.suburb": 1,
			"address.country": 1,
			"images.picture_url": 1,
			price: 1,
			amenities: { 
				$reduce: {
				input: "$amenities",
				initialValue: "",
				in: { $concat : ["$$value", "$$this", ", "] }
				}
		}
		}
	}
	]) 
	*/
	public List<Document> getListingDetails(String id){
		MatchOperation matchOp = Aggregation.match(Criteria.where("_id").is(id));

		ProjectionOperation projectOp = Aggregation.project("_id",
		"description",
		"address.street",
		"address.suburb",
		"address.country",
		"images.picture_url",
		"price")
		.and(AggregationExpression.from(
			MongoExpression.create("""
					$reduce: {
						input: "$amenities",
						initialValue: "",
						in: { $concat : ["$$value", "$$this", ", "] }
					}
					""")
		)).as("amenities");

		Aggregation pipeline = Aggregation.newAggregation(matchOp, projectOp);

		AggregationResults<Document> aggregate = mongoTemplate.aggregate(pipeline, C_LISTINGS, Document.class);

		return aggregate.getMappedResults();
	}

	//TODO: Task 5


}
