package kz.edu.astanait.usertest.utils.facade;

import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.utils.ImageUtils;
import lombok.SneakyThrows;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.Result;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class ImageFacade {
    @SneakyThrows
    public static Image parseResultSetToImage(ResultSet resultSet, String prefix) {
        Long id = resultSet.getLong(prefix+(!prefix.equals("")?".":"") + "id");
        String name = resultSet.getString(prefix+(!prefix.equals("")?".":"")+"name");
        String extension = resultSet.getString("extension");
        String contentType = resultSet.getString("contentType");
        byte[] data = resultSet.getBytes("data");
        return Image.builder()
            .id(id)
            .name(name)
            .extension(extension)
            .contentType(contentType)
            .data(data)
            .build();
    }
    public static void parseImageToStatement(Image image, PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.setString(1, image.getContentType());
        preparedStatement.setBytes(2, image.getData());
        preparedStatement.setString(3, image.getExtension());
        preparedStatement.setString(4, image.getName());
    }
    public static void setImageIfNeeded(UserDtoRequest userDtoRequest, MultipartFile file) throws IOException {
        userDtoRequest.setImage(null);
        if (file != null) {
            String[] split = Objects.requireNonNull(file.getOriginalFilename()).split("\\.");
            Image image = Image.builder()
                .name(split[0])
                .extension(split[1])
                .contentType(file.getContentType())
                .data(ImageUtils.compressImage(file.getBytes()))
                .build();
            userDtoRequest.setImage(image);
        }
    }
}
