package kz.edu.astanait.usertest.dto.request;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetIpDtoRequest {
    private String ip;
    @JsonProperty("country_name")
    private String countryName;
}