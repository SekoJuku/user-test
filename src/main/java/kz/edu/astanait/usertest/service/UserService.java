package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.annotation.Loggable;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.exception.domain.BadUserRequestException;
import kz.edu.astanait.usertest.exception.domain.UserNotFoundException;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.Role;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.repository.ImageRepository;
import kz.edu.astanait.usertest.repository.RoleRepository;
import kz.edu.astanait.usertest.repository.UserRepository;
import kz.edu.astanait.usertest.utils.HttpUtils;
import kz.edu.astanait.usertest.utils.ImageUtils;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
@Loggable
@Log4j2
public class UserService implements UserDetailsService {
    private final RestTemplate restTemplate;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CountryService countryService;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;

    public User getById(Long id) {
        Optional<User> oUser = userRepository.findById(id);
        if(oUser.isEmpty())
            throw new UserNotFoundException("User by " + id + " is not found");
        return oUser.get();
    }

    public String getIp() {
        return HttpUtils.getIP(restTemplate, RequestContextHolder.currentRequestAttributes());
    }


    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Image getImageByUserId(Long id) {
        return getById(id).getImage();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
            .orElseThrow(() -> new BadUserRequestException(String.format("Role with id: %d is not found",id)));
    }

    @SneakyThrows
    public User create(UserDtoRequest request) {
        User user = UserFacade.userDtoToUser(request);
        if(userRepository.existsByEmail(user.getEmail())) {
            throw new BadUserRequestException(String.format("User with email: %s exists",user.getEmail()));
        }
        log.info(request.getPassword());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String countryName = getIp();
        Country country = countryService.findByName(countryName);
        if(country == null)
            country = countryService.add(countryName);
        user.setCountry(country);
        user.setRole(getRoleById(request.getRoleId()));
        Image image = null;
        if (request.getImage() != null) {
             image = imageRepository.save(request.getImage());
        }
        user.setImage(image);
        return userRepository.save(user);
    }

    @SneakyThrows
    public User edit(UserDtoRequest userDtoRequest) {
        User user = UserFacade.userDtoToUser(userDtoRequest);
        User oldUser = getById(user.getId());
        user.setId(oldUser.getId());
        user.setCountry(oldUser.getCountry());
        if(user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        Image image = null;
        if (userDtoRequest.getImage() != null) {
            image = imageRepository.save(userDtoRequest.getImage());
        }
        user.setImage(image);
        user.setRole(getRoleById(userDtoRequest.getRoleId()));
        return userRepository.save(user);
    }

    @SneakyThrows
    private Image saveImageIfNeeded(UserDtoRequest userDtoRequest, MultipartFile file) {
        userDtoRequest.setImage(null);
        if (file != null) {
            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            log.info( file.getOriginalFilename() + " " + Arrays.toString(file.getOriginalFilename().split("\\.")) +split.length + " " + Arrays.toString(split));
            Image image = Image.builder()
                .name(split[0])
                .extension(split[1])
                .contentType(file.getContentType())
                .data(ImageUtils.compressImage(file.getBytes()))
                .build();
            userDtoRequest.setImage(image);
        }
        return userDtoRequest.getImage();
    }
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException(String.format("User with email: %s is not found!", email)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByEmail(username);
    }

    public User create(UserDtoRequest userDtoRequest, MultipartFile file) {
        saveImageIfNeeded(userDtoRequest, file);
        return create(userDtoRequest);
    }

    public User edit(UserDtoRequest userDtoRequest, MultipartFile file) {
        saveImageIfNeeded(userDtoRequest, file);
        return edit(userDtoRequest);
    }
}
