package com.practice.controller;

import com.practice.models.Comment;
import com.practice.models.User;
import com.practice.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ResponseEntity<User> saveUser(@RequestBody User user){
        return ResponseEntity.ok(userService.saveTest(user));
    }

    @PatchMapping("/{id}")
    ResponseEntity<User> updateUser(@PathVariable Long id,@RequestBody User user){
        return ResponseEntity.ok(userService.update(id,user));
    }


    @GetMapping
    ResponseEntity<List<User>> getAll(){
        return ResponseEntity.ok(userService.getAllTests());
    }

    @PutMapping("/{userId}")
    ResponseEntity<Comment> addComment(@PathVariable Long userId,@RequestBody Comment comment){
        return ResponseEntity.ok(userService.addComment(userId,comment));
    }



}
