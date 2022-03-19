package kz.edu.astanait.usertest.controller;

import kz.edu.astanait.usertest.dto.UserDtoRequest;
import kz.edu.astanait.usertest.model.User;
import kz.edu.astanait.usertest.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Log
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/create")
    public User create(@RequestBody UserDtoRequest userDtoRequest) {
        return userService.create(userDtoRequest);
    }

    @PutMapping("/{id}")
    public User edit(@PathVariable Long id, @RequestBody UserDtoRequest userDtoRequest) {
        userDtoRequest.setId(id);
        return userService.edit(userDtoRequest);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok("User is deleted!");
    }

    @GetMapping("/ip")
    public String getIp() {
        return userService.getIp();
    }

}
