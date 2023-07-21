package vttp2023.batch3.assessment.paf.bookings;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import vttp2023.batch3.assessment.paf.bookings.models.SearchInput;
import vttp2023.batch3.assessment.paf.bookings.repositories.ListingsRepository;

@SpringBootApplication
public class BookingsApplication implements CommandLineRunner{

	@Autowired
	private ListingsRepository repo;
	public static void main(String[] args) {
		SpringApplication.run(BookingsApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception {
		List<Document> result = repo.getListingsByInput(new SearchInput("australia", 2, 100f, 100f));
		for(Document s : result){
			System.out.println(s.toString());
		}
		// System.exit(0);
	}

}
