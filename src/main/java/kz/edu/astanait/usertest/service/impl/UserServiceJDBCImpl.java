package kz.edu.astanait.usertest.service.impl;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.exception.domain.BadUserRequestException;
import kz.edu.astanait.usertest.exception.domain.UserNotFoundException;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.Role;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.CountryService;
import kz.edu.astanait.usertest.service.UserService;
import kz.edu.astanait.usertest.utils.HttpUtils;
import kz.edu.astanait.usertest.utils.facade.CountryFacade;
import kz.edu.astanait.usertest.utils.facade.ImageFacade;
import kz.edu.astanait.usertest.utils.facade.RoleFacade;
import kz.edu.astanait.usertest.utils.facade.UserFacade;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@AllArgsConstructor
@Service
public class UserServiceJDBCImpl implements UserService {
    private final RestTemplate restTemplate;
    private final PasswordEncoder passwordEncoder;
    private final CountryService countryService;
    private final DataSource dataSource;

    @Autowired
    private Connection prepareConnection() {
        return DataSourceUtils.getConnection(this.dataSource);
    }

    @Override
    public String getName() {
        return "jdbc";
    }

    @SneakyThrows
    @Override
    public User getById(Long id) {
        Connection connection = prepareConnection();
        String sql = "SELECT * FROM users u " +
            "inner join countries c on c.id = u.country_id " +
            "inner join roles r on r.id = u.role_id " +
            "inner join images i on i.id = u.image_id " +
            "where u.id = ?;";
        User user;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            user = UserFacade.parseResultSetToUser(resultSet, "u");
            user.setRole(RoleFacade.parseResultSetToRole(resultSet, "r"));
            user.setImage(ImageFacade.parseResultSetToImage(resultSet, "i"));
            user.setCountry(CountryFacade.parseResultSetToCountry(resultSet, "c"));
        }
        if(user.getId() == null) {
            throw new UserNotFoundException(String.format("User with id: %d is not found!", id));
        }
        return user;
    }

    @Override
    public String getIp() {
        return HttpUtils.getIP(restTemplate, RequestContextHolder.getRequestAttributes());
    }

    @Override
    @SneakyThrows
    public void delete(Long id) {
        Connection connection = prepareConnection();
        String sql = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(id));
            boolean isExecuted = preparedStatement.execute();
            if(!isExecuted) {
                throw new BadUserRequestException(String.format("User with id: %d couldn't be deleted!", id));
            }
        }
    }

    @Override
    @SneakyThrows
    public Image getImageByUserId(Long id) {
        Connection connection = prepareConnection();
        String sql = "SELECT * FROM images i " +
            "INNER JOIN users u on i.id = u.image_id " +
            "WHERE u.id = ?;";
        Image image;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            image = ImageFacade.parseResultSetToImage(resultSet, "i");
        }
        if(image.getId() == null) {
            throw new BadUserRequestException(String.format("Image with user id: %d is not found",id));
        }
        return image;
    }

    @Override
    @SneakyThrows
    public Role getRoleById(Long id) {
        Connection connection = prepareConnection();
        String sql = "SELECT * FROM roles " +
            "WHERE id = ?;";
        Role role;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, String.valueOf(id));
            ResultSet resultSet = preparedStatement.executeQuery();
            role = RoleFacade.parseResultSetToRole(resultSet);
        }
        if(role.getId() == null) {
            throw new BadUserRequestException(String.format("Role with id: %d is not found",id));
        }
        return role;
    }

    @SneakyThrows
    @Override
    public User create(UserDtoRequest request) {
        Connection connection = prepareConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);
        User user = UserFacade.userDtoToUser(request);
        String sql ="SELECT id from users " +
            "where email = ?;";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, user.getEmail());
            ResultSet resultSet = preparedStatement.executeQuery();
            long id = resultSet.getLong("id");
            if(id != 0L) {
                connection.rollback();
                throw new BadUserRequestException(String.format("User with email: %s exists",user.getEmail()));
            }
        }
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        String countryName = getIp();
        Country country = countryService.findByName(countryName);
        if(country == null)
            country = countryService.add(countryName);
        if(request.getImage() != null) {
            saveImage(request, connection, user);
        }
        Role role = getRoleById(1L);
        sql = "INSERT INTO users(email, middlename, name, password, phone_number, sex, surname, country_id, role_id, image_id) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            UserFacade.parseUserValuesToStatement(user,
                country.getId(),
                role.getId(),
                (user.getImage() != null ? user.getImage().getId() : null),
                preparedStatement);
            boolean isExecuted = preparedStatement.execute();
            if (!isExecuted) {
                connection.rollback();
                throw new BadUserRequestException("User is not added!");
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        connection.close();
        return user;
    }

    private void saveImage(UserDtoRequest request, Connection connection, User user) throws SQLException {
        Image image = request.getImage();
        String sql = "INSERT INTO images(content_type, data, extension, name) VALUES (?, ?, ?, ?) RETURNING id;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ImageFacade.parseImageToStatement(image, preparedStatement);
            ResultSet resultSet = preparedStatement.executeQuery();
            long id = resultSet.getLong("id");
            if (id == 0L) {
                connection.rollback();
                throw new BadUserRequestException("Image is not saved!");
            }
            image.setId(id);
        }
        user.setImage(image);
        request.setImage(image);
    }

    @SneakyThrows
    @Override
    public User edit(UserDtoRequest userDtoRequest) {
        Connection connection = prepareConnection();
        connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
        connection.setAutoCommit(false);
        User user = UserFacade.userDtoToUser(userDtoRequest);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User oldUser = getById(userDtoRequest.getId());
        if(user.getImage() != null) {
            saveImage(userDtoRequest, connection, user);
        }
        String sql = "UPDATE users " +
            "SET email = ?, name = ?, middlename = ?, password = ?, phone_number = ?, sex = ?, surname = " +
            "?, country_id = ?, role_id = ?, image_id = ?" +
            "WHERE id = ?;";
        try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            UserFacade.parseUserValuesToStatement(
                user,
                oldUser.getCountry().getId(),
                user.getRole().getId(),
                (user.getImage() != null ? user.getImage().getId() : (oldUser.getImage() != null ? oldUser.getImage().getId() : null)),
                preparedStatement);
            preparedStatement.setLong(11, user.getId());
            boolean isExecuted = preparedStatement.execute();
            if (!isExecuted) {
                connection.rollback();
                throw new BadUserRequestException("User is not updated!");
            }
        }
        connection.commit();
        connection.setAutoCommit(true);
        connection.close();
        return user;
    }

    @SneakyThrows
    @Override
    public void saveImageIfNeeded(UserDtoRequest userDtoRequest, MultipartFile file) {
        ImageFacade.setImageIfNeeded(userDtoRequest, file);
    }

    @SneakyThrows
    @Override
    public User getUserByEmail(String email) {
        Connection connection = prepareConnection();
        String sql = "SELECT id FROM users WHERE email = ?;";
        long id;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            id = resultSet.getLong("id");
            if (id == 0L) {
                throw new UserNotFoundException(String.format("User with email: %s is not found!", email));
            }
        }
        return getById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getUserByEmail(username);
    }

    @Override
    public User create(UserDtoRequest userDtoRequest, MultipartFile file) {
        saveImageIfNeeded(userDtoRequest, file);
        return create(userDtoRequest);
    }

    @Override
    public User edit(UserDtoRequest userDtoRequest, MultipartFile file) {
        saveImageIfNeeded(userDtoRequest, file);
        return edit(userDtoRequest);
    }
}
