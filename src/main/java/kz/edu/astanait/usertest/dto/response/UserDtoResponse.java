package kz.edu.astanait.usertest.dto.response;

import lombok.Data;

@Data
public class UserDtoResponse {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    private String phoneNumber; // Here it doesn't work if I name it phoneNumber, even though i've used it many times
    private String email;
    private String ip;
}
