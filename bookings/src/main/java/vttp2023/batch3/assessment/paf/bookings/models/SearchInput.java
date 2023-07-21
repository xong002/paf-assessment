package vttp2023.batch3.assessment.paf.bookings.models;

import org.hibernate.validator.constraints.Range;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class SearchInput {

    @NotEmpty(message = "Country cannot be empty")
    @NotNull(message = "Country cannot be null")
    private String country;

    @NotNull(message = "Number of person cannot be empty")
    @Range(min = 1, max = 10, message = "Number of person must be between 1 to 10 inclusive")
    private Integer pax;

    @NotNull(message = "Price range cannot be empty")
    @Range(min = 1, max = 10000, message = "Price range must be between 1 and 10000 inclusive")
    private Float min;

    @NotNull(message = "Price range cannot be empty")
    @Range(min = 1, max = 10000, message = "Price range must be between 1 and 10000 inclusive")
    private Float max;

    public SearchInput() {
    }
 

    public SearchInput(
            @NotEmpty(message = "Country cannot be empty") @NotNull(message = "Country cannot be null") String country,
            @NotNull(message = "Number of person cannot be empty") @Range(min = 1, max = 10, message = "Number of person must be between 1 to 10 inclusive") Integer pax,
            @NotNull(message = "Price range cannot be empty") @Range(min = 1, max = 10000, message = "Price range must be between 1 and 10000 inclusive") Float min,
            @NotNull(message = "Price range cannot be empty") @Range(min = 1, max = 10000, message = "Price range must be between 1 and 10000 inclusive") Float max) {
        this.country = country;
        this.pax = pax;
        this.min = min;
        this.max = max;
    }



    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPax() {
        return pax;
    }

    public void setPax(Integer pax) {
        this.pax = pax;
    }

    public Float getMin() {
        return min;
    }

    public void setMin(Float min) {
        this.min = min;
    }

    public Float getMax() {
        return max;
    }

    public void setMax(Float max) {
        this.max = max;
    }

    
}
