package vttp2023.batch3.assessment.paf.bookings.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import vttp2023.batch3.assessment.paf.bookings.models.Reservation;
import vttp2023.batch3.assessment.paf.bookings.models.SearchInput;
import vttp2023.batch3.assessment.paf.bookings.models.Vacancy;

@Repository
public class ListingsRepository {

	private static final String C_LISTINGS = "listings";

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	// TODO: Task 2
	// db.listings.distinct("address.country")
	public List<String> getCountryList() {
		return mongoTemplate.findDistinct(new Query(), "address.country", C_LISTINGS, String.class);
	}

	// TODO: Task 3
	/*
	 * db.listings.find(
	 * {
	 * "address.country": {$regex: "<country>", $options: "i"},
	 * accommodates: <pax>,
	 * price: {$gte: <min>, $lte: <max>}
	 * },
	 * { name: 1, "address.street": 1, price: 1, "images.picture_url": 1 }
	 * ).sort({price: -1})
	 */
	public List<Document> getListingsByInput(SearchInput input) {
		Criteria criteria = Criteria.where("address.country").regex(input.getCountry(), "i")
				.and("accommodates").is(input.getPax())
				.and("price").gte(input.getMin()).lte(input.getMax());

		Query query = new Query(criteria).with(Sort.by(Direction.DESC, "price"));

		query.fields().include("name", "address.street", "price", "images.picture_url");

		return mongoTemplate.find(query, Document.class, C_LISTINGS);
	}

	// TODO: Task 4
	/*
	 * db.listings.aggregate([
	 * {
	 * $match: { _id: "<listingId>"}
	 * }
	 * ,{
	 * $project: {
	 * _id: 1,
	 * description: 1,
	 * "address.street": 1,
	 * "address.suburb": 1,
	 * "address.country": 1,
	 * "images.picture_url": 1,
	 * price: 1,
	 * amenities: {
	 * $reduce: {
	 * input: "$amenities",
	 * initialValue: "",
	 * in: { $concat : ["$$value", "$$this", ", "] }
	 * }
	 * }
	 * }
	 * }
	 * ])
	 */
	public List<Document> getListingDetails(String id) {
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
								""")))
				.as("amenities");

		Aggregation pipeline = Aggregation.newAggregation(matchOp, projectOp);

		AggregationResults<Document> aggregate = mongoTemplate.aggregate(pipeline, C_LISTINGS, Document.class);

		return aggregate.getMappedResults();
	}

	// TODO: Task 5
	private static final String FIND_VACANCY_SQL = "select * from acc_occupancy where acc_id = ? ";
	private static final String ADD_RESERVATION_SQL = "insert into reservations (resv_id, name, email, acc_id, arrival_date, duration) values (?,?,?,?,?,?)";
	private static final String UPDATE_VACANCY_SQL = "update acc_occupancy set vacancy = ? where acc_id = ? ";

	public Optional<Integer> findVacancyById(String accId) {

		List<Vacancy> result = jdbcTemplate.query(FIND_VACANCY_SQL, BeanPropertyRowMapper.newInstance(Vacancy.class), accId);
		return Optional.ofNullable(result.get(0).getVacancy());
	}

	public Optional<String> insertReservation(Reservation rsv) {
		int update = jdbcTemplate.update(ADD_RESERVATION_SQL, rsv.getId(), rsv.getName(), rsv.getEmail(), rsv.getAccId(), rsv.getArrival(), rsv.getDays());
		if (update == 0) return Optional.empty();
		return Optional.ofNullable(rsv.getId());
	}

	public boolean updateVacancy(String accId, Integer vacancy){
		int update = jdbcTemplate.update(UPDATE_VACANCY_SQL, vacancy, accId);
		return update > 0;
	}

}
