package kz.edu.astanait.usertest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // имя, фамилия, отчество, пол, номер телефона, email
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    @JsonProperty("phone_number")
    private String phoneNumber;
    private String email;
    @ManyToOne(targetEntity = Country.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;
    @Column(name = "image", length = 100000)
    private byte[] image;

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name=\"" + name + "\"" +
                ", surname=\"" + surname + "\"" +
                ", middlename=\"" + middlename + "\"" +
                ", sex=\"" + sex + "\"" +
                ", phoneNumber=\"" + phoneNumber + "\"" +
                ", email=\"" + email + "\"" +
                ", country=" + country +
                ", image=" + Arrays.toString(image) +
                "}";
    }
}
