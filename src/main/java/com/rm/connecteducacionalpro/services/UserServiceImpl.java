package com.rm.connecteducacionalpro.services;

import com.rm.connecteducacionalpro.model.User;
import com.rm.connecteducacionalpro.model.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserServiceImpl {
    Page<User> getUser(int page, int size);
    User postUser(UserDTO userDTO, String role);
    User getUser(String username);

    User findById();
}

