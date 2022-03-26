package kz.edu.astanait.usertest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) // имя, фамилия, отчество, пол, номер телефона, email
    private Long id;
    private String name;
    private String surname;
    private String middlename;
    private String sex;
    private String phoneNumber;
    private String email;
    @ManyToOne(targetEntity = Country.class, cascade = CascadeType.DETACH)
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    private Country country;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", sex='" + sex + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                '}';
    }
}
