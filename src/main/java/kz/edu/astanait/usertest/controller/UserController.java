package kz.edu.astanait.usertest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.edu.astanait.usertest.annotation.Metric;
import kz.edu.astanait.usertest.dto.request.UserDtoRequest;
import kz.edu.astanait.usertest.model.Image;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.UserService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldNameConstants;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log
@RequestMapping("/api/user")
public class UserController {
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private final Map<String, UserService> userServicesMap;
    @Value("${userService}")
    private String name;

    @GetMapping("/{id}")
    @Metric(name = "method.getById")
    public User getById(@PathVariable Long id) {
        log.info("GET:getById(" + id+ ")");
        return findUserService(name).getById(id);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImageByUserId(@PathVariable Long id) {
        Image image = findUserService(name).getImageByUserId(id);
        return ResponseEntity.status(HttpStatus.OK)
            .header(HttpHeaders.CONTENT_TYPE, image.getContentType())
            .header(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"", image.getName()+"."+image.getExtension()))
            .body(image.getData());
    }

    @SneakyThrows
    @PostMapping(value = "",
        consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public User create(@Validated @RequestParam("requestString") String requestString, @Nullable @RequestParam("file") MultipartFile file) {
        log.info("POST:create( " + requestString + ") " + name + userServicesMap.keySet() );
        UserDtoRequest request = objectMapper.readValue(requestString, UserDtoRequest.class);
        return findUserService(name).create(request, file);
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public User edit(@PathVariable Long id, @Validated @RequestParam("requestString") String requestString, @Nullable @RequestParam("file") MultipartFile file) {
        log.info("PUT:edit(" + id+ ", " + requestString + ")");
        UserDtoRequest userDtoRequest = objectMapper.readValue(requestString, UserDtoRequest.class);
        userDtoRequest.setId(id);
        return findUserService(name).edit(userDtoRequest, file);
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        log.info("DELETE:delete(" + id+ ")");
        findUserService(name).delete(id);
        return "User is deleted!";
    }

    @GetMapping("/ip")
    public String getIp() {
        log.info("GET:getIp()");
        return findUserService(name).getIp();
    }

    private UserService findUserService(String key) {
        return userServicesMap.get(key);
    }

}
