package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;

public class UserFacade {
    public static User UserDtoToUser(UserDtoRequest request) {
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
}
