package kz.edu.astanait.usertest.dto.request;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonAutoDetect
@Builder
public class UserDtoRequest {
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    private String phoneNumber;
    private String password;
    private String email;
    private Long roleId;
    private Country country;
    private Image image;

    @Override
    public String toString() {
        return "UserDtoRequest{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", surname='" + surname + '\'' +
            ", middlename='" + middlename + '\'' +
            ", sex='" + sex + '\'' +
            ", phoneNumber='" + phoneNumber + '\'' +
            ", password='" + password + '\'' +
            ", email='" + email + '\'' +
            ", roleId=" + roleId +
            '}';
    }
}
