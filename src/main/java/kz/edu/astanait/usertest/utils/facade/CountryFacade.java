package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.model.Country;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class CountryFacade {
    @SneakyThrows
    public static Country parseResultSetToCountry(ResultSet resultSet, String prefix) {
        Long id = resultSet.getLong(prefix+(!prefix.equals("")?".":"") +"id");
        String name = resultSet.getString(prefix+(!prefix.equals("")?".":"") + "name");
        return Country.builder()
            .id(id)
            .name(name)
            .build();
    }
}
