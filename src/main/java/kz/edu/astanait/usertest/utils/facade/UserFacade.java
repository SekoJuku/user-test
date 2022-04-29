package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.User;

public class UserFacade {
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
                "serikzhan@serik.com",
                null
        );
    }
}
