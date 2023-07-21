package vttp2023.batch3.assessment.paf.bookings.models;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    private String id;
    private String accId;
    private String name;
    private String email;
    private Date arrival;
    private Integer days;

}
