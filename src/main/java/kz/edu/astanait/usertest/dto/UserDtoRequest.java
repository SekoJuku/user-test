package kz.edu.astanait.usertest.dto;


import lombok.Data;

@Data
public class UserDtoRequest {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    private String phone_number;
    private String email;
    private String ip;
}
