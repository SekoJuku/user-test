/*package kz.edu.astanait.usertest;

import kz.edu.astanait.usertest.model.Country;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class ApiTest {
    @Test
    public void rawIpTest() throws IOException {
        URL ipApi = new URL("https://ipapi.co/147.30.70.34/json/");

        URLConnection c = ipApi.openConnection();
        c.setRequestProperty("User-Agent", "java-ipapi-v1.02");
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(c.getInputStream())
        );
        Country country = new Country();

        String countryName = reader.readLine();
        System.out.println(countryName);
        System.out.println(reader.readLine());
        country.setName(countryName);
        Assertions.assertEquals("Kazakhstan", country.getName());
    }
}
*/