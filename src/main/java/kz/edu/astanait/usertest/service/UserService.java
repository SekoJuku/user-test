package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.annotation.Loggable;
import kz.edu.astanait.usertest.exception.UserNotFoundException;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.utils.HttpUtils;
import kz.edu.astanait.usertest.utils.ImageUtils;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.awt.*;
import java.util.Optional;

@Service
@AllArgsConstructor
@Loggable
public class UserService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final  CountryService countryService;

    public User getById(Long id) {
        Optional<User> oUser = userRepository.findById(id);
        if(oUser.isEmpty())
            throw new UserNotFoundException("User by " + id + " is not found");
        return oUser.get();
    }

    public String getIp() {
        return HttpUtils.getIP(restTemplate, RequestContextHolder.currentRequestAttributes());
    }

    public User create(User user) {
        String countryName = getIp();
        Country country = countryService.findByName(countryName);
        if(country == null)
            country = countryService.add(countryName);
        user.setCountry(country);
        User savedUser = userRepository.save(user);
        if( savedUser.getImage().length == 0)
            savedUser.setImage(ImageUtils.decompressImage(savedUser.getImage()));
        return savedUser;
    }

    public User edit(User user) {
        User oldUser = getById(user.getId());
        user.setId(oldUser.getId());
        user.setCountry(oldUser.getCountry());
        if(user.getImage().length == 0)
            user.setImage(oldUser.getImage());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public byte[] getImageByUserId(Long id) {
        User user = getById(id);
        return ImageUtils.decompressImage(user.getImage());
    }
}
