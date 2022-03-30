package kz.edu.astanait.usertest.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import kz.edu.astanait.usertest.model.Country;
import lombok.Data;

@Data
public class UserDtoRequest {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String email;
    private Country country;

    @Override
    public String toString() {
        return "UserDtoRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", sex='" + sex + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", countryId=" + country +
                '}';
    }
}
