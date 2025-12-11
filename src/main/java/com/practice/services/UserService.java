package com.practice.services;


import com.practice.models.Comment;
import com.practice.models.User;
import com.practice.repository.CommentRepo;
import com.practice.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo testRepo;
    private final CommentRepo commentRepo;

    private static final String USER_NOT_FOUND_MESSAGE = "User not found";

    public User saveTest(User user) {
        return testRepo.save(user);
    }

    public boolean existByEmail(String email) {
        return testRepo.existsByEmail(email);
    }

    public Comment addComment(Long userId, Comment comment) {
        User user = testRepo.findById(userId).orElseThrow(
                () -> new RuntimeException(USER_NOT_FOUND_MESSAGE)
        );
        user.addComment(comment);
        return commentRepo.save(comment);
    }

    public List<User> getAllTests() {
        return testRepo.findAll();
    }

    public User getTestById(Long id) {
        return testRepo.findById(id).orElse(null);
    }

    public void deleteTestById(Long id) {

        User user = testRepo.findById(id).orElseThrow(
                () -> new RuntimeException(USER_NOT_FOUND_MESSAGE)
        );

        testRepo.delete(user);
    }

    public User update(Long id, User user) {

        User existing = testRepo.findById(id).orElseThrow(
                () -> new RuntimeException(USER_NOT_FOUND_MESSAGE)
        );

        Optional.ofNullable(user.getName()).ifPresent(existing::setName);
        return testRepo.save(existing);
    }


}
