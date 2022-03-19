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
    private String ip;
}
