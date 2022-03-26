package kz.edu.astanait.usertest.dto.request;


import kz.edu.astanait.usertest.model.Country;
import lombok.Data;

@Data
public class UserDtoRequest {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
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
