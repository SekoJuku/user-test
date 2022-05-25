package kz.edu.astanait.usertest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "images")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String extension;

    private String contentType;

    private byte[] data;

    @Override
    public String toString() {
        return "Image {" +
            "id: " + id + "," +
            "name: " + name + "," +
            "extension: " + extension + "," +
            "contentType: " + contentType + "}";
    }
}
