package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.utils.ImageUtils;
import lombok.SneakyThrows;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class UserFacade {
    @SneakyThrows
    public static User userDtoToUser(UserDtoRequest request) {
        User response = new User();
        if (request.getId() != null) {
            response.setId(request.getId());
        }
        response.setName(request.getName());
        response.setSurname(request.getSurname());
        response.setMiddlename(request.getMiddlename());
        response.setSex(request.getSex());
        response.setCountry(request.getCountry());
        response.setEmail(request.getEmail());
        response.setPhoneNumber(request.getPhoneNumber());
        response.setPassword(request.getPassword());
        return response;
    }
    public static UserDtoRequest userToDtoRequest(User user) {
        UserDtoRequest response = UserDtoRequest.builder()
            .name(user.getName())
            .surname(user.getSurname())
            .middlename(user.getMiddlename())
            .sex(user.getSex())
            .country(user.getCountry())
            .email(user.getEmail())
            .password(user.getPassword())
            .phoneNumber(user.getPhoneNumber())
            .roleId(user.getRole().getId())
            .build();
        if(user.getId() != null)
            response.setId(user.getId());
        return response;
    }
    public static User createTestUser() {
        return new User(
                null,
                "Serikzhan",
                "Kuanyshev",
                "Azamatovich",
                "Male",
                "7477777777",
                "berik@gmail.com",
                "123",
                null,
                null,
                null
        );
    }
    @SneakyThrows
    public static User parseResultSetToUser(ResultSet resultSet, String prefix){
        Long id = resultSet.getLong(prefix+(!prefix.equals("")?".":"")+"id");
        String name = resultSet.getString(prefix+(!prefix.equals("")?".":"")+"name");
        String surname = resultSet.getString("surname");
        String middlename = resultSet.getString("middlename");
        String sex = resultSet.getString("sex");
        String phoneNumber = resultSet.getString("phone_number");
        String email = resultSet.getString("email");
        String password = resultSet.getString("password");
        return User.builder()
            .id(id)
            .name(name)
            .surname(surname)
            .middlename(middlename)
            .phoneNumber(phoneNumber)
            .sex(sex)
            .email(email)
            .password(password)
            .build();
    }

    public static void parseUserValuesToStatement(
        User user,
        Long countryId,
        Long roleId,
        Long imageId,
        PreparedStatement preparedStatement
    ) throws SQLException {
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getMiddlename());
        preparedStatement.setString(3, user.getName());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getPhoneNumber());
        preparedStatement.setString(6, user.getSex());
        preparedStatement.setString(7, user.getSurname());
        preparedStatement.setLong(8, countryId);
        preparedStatement.setLong(9, roleId);
        preparedStatement.setLong(10, imageId);
    }
}
