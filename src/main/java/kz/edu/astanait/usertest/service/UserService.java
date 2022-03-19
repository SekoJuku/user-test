package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.dto.UserDtoRequest;
import kz.edu.astanait.usertest.exception.UserNotFoundException;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.utils.HttpUtils;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class UserService {
    private final UserRepository userRepository;

    public User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isEmpty())
            throw new UserNotFoundException("User by " + id + " is not found");
        return user.get();
    }

    public String getIp() {
        return HttpUtils.getIP(RequestContextHolder.currentRequestAttributes());
    }

    public User create(UserDtoRequest request) {
        request.setIp(getIp());
        return userRepository.save(UserFacade.UserDtoToUser(request));
    }

    public User edit(UserDtoRequest request) {
        User oldUser = getById(request.getId());
        User newUser = UserFacade.UserDtoToUser(request);
        newUser.setId(oldUser.getId());
        newUser.setIp(oldUser.getIp());
        return userRepository.save(newUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
