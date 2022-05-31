package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.model.Role;
import lombok.SneakyThrows;

import java.sql.ResultSet;

public class RoleFacade {
    @SneakyThrows
    public static Role parseResultSetToRole(ResultSet resultSet, String prefix) {
        Long id = resultSet.getLong(prefix+(!prefix.equals("")?".":"")+"id");
        String name = resultSet.getString(prefix+(!prefix.equals("")?".":"")+"name");
        return Role.builder()
            .id(id)
            .name(name)
            .build();
    }
    @SneakyThrows
    public static Role parseResultSetToRole(ResultSet resultSet) {
        return parseResultSetToRole(resultSet, "");
    }
}
