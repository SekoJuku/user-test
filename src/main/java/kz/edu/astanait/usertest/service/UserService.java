package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.annotation.Loggable;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.Role;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.utils.HttpUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

@Service
@Loggable
public interface UserService extends UserDetailsService {

    String getName();

    User getById(Long id);

    String getIp();

    void delete(Long id);

    Image getImageByUserId(Long id);

    Role getRoleById(Long id);

    @SneakyThrows
    User create(UserDtoRequest request);

    @SneakyThrows
    User edit(UserDtoRequest userDtoRequest);

    @SneakyThrows
    void saveImageIfNeeded(UserDtoRequest userDtoRequest, MultipartFile file);

    User getUserByEmail(String email);

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User create(UserDtoRequest userDtoRequest, MultipartFile file);

    User edit(UserDtoRequest userDtoRequest, MultipartFile file);
}
