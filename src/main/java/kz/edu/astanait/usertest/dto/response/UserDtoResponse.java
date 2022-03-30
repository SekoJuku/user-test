package kz.edu.astanait.usertest.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDtoResponse {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    @JsonProperty("phone_number") // doesn't work
    private String phoneNumber;
    private String email;
    private String ip;
}
