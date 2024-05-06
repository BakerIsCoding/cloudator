package com.project.cloudator.service;

import java.util.List;

import com.project.cloudator.dto.UserDto;
import com.project.cloudator.entity.User;
import com.project.cloudator.entity.UserAccess;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByEmail(String email);

    User findByUsername(String username);

    List<UserDto> findFirstXUsers(Integer page, Integer size);

    List<UserDto> findAllUsers();

    UserAccess findById(Integer id);

    void updateUsername(Long userid, String username);

    void updateEmail(Long userid, String email);

    void updatePassword(Long userid, String password);

    boolean blockUser(Long id);

    boolean unBlockUser(Long id);

    User getUserById(Long id);

    void deleteUser(Long id);

    User fetchUser(String user);

    Long getUserIdByUsername(String username);

    /**
     * Updates the role of a user identified by the given userid.
     *
     * @param userid  the ID of the user to update the role for
     * @param newRole the ID of the new role to assign to the user
     */
    void updateRole(Long userid, Long newRole);

    List<String> getRolesByUserId(Long userId);

}
