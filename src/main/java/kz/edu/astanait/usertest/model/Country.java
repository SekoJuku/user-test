package kz.edu.astanait.usertest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "countries")
public class Country {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;

    @Override
    public String toString() {
        return "Country{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }

    public Country(String name) {
        this.setName(name);
    }
}
