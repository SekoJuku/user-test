package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.utils.ImageUtils;
import lombok.SneakyThrows;

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
    public static UserDtoRequest UserToDtoRequest(User user) {
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
}
