package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.annotation.Loggable;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.exception.UserNotFoundException;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.ImageRepository;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.utils.HttpUtils;
import kz.edu.astanait.usertest.utils.ImageUtils;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Loggable
public class UserService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final CountryService countryService;
    private final ImageRepository imageRepository;

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
        return userRepository.save(user);
    }

    public User edit(User user) {
        User oldUser = getById(user.getId());
        user.setId(oldUser.getId());
        user.setCountry(oldUser.getCountry());
        return userRepository.save(user);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Image getImageByUserId(Long id) {
        Optional<Image> oImage = imageRepository.findByUserId(id);
        if(oImage.isEmpty()) {
            throw new BadCredentialsException(String.format("User with id: %d doesn't have image", id));
        }
        return oImage.get();
    }

    @SneakyThrows
    public User create(UserDtoRequest userDtoRequest) {
        User user = create(UserFacade.userDtoToUser(userDtoRequest));
        return saveImageIfNeeded(userDtoRequest, user);
    }

    @SneakyThrows
    public User edit(UserDtoRequest userDtoRequest) {
        User user = edit(UserFacade.userDtoToUser(userDtoRequest));
        return saveImageIfNeeded(userDtoRequest, user);
    }

    @SneakyThrows
    private User saveImageIfNeeded(UserDtoRequest userDtoRequest, User user) throws IOException {
        if (!userDtoRequest.getImage().isEmpty()) {
            String[] split = Objects.requireNonNull(userDtoRequest.getImage().getOriginalFilename()).split("\\.");
            Image image = Image.builder()
                .name(split[0])
                .extension(split[1])
                .contentType(userDtoRequest.getImage().getContentType())
                .data(ImageUtils.compressImage(userDtoRequest.getImage().getBytes()))
                .userId(user.getId())
                .build();
            imageRepository.save(image);
        }
        return user;
    }
}
