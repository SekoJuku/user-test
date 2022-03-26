package kz.edu.astanait.usertest.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GetIpDtoRequest {
    private String ip;
    private String country_name;
}
