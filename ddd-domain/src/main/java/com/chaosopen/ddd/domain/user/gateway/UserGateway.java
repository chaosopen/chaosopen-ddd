package com.chaosopen.ddd.domain.user.gateway;

import com.chaosopen.ddd.domain.user.model.User;

import java.util.Optional;

public interface UserGateway {

    Optional<User> findByUserId(Long userId);

    void save(User user);
}
