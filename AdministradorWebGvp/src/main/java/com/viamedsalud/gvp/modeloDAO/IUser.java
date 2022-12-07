package com.viamedsalud.gvp.modeloDAO;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.viamedsalud.gvp.modelo.User;

import java.util.Optional;

@Repository
public interface IUser extends CrudRepository<User, Integer>{
    Optional<User> findAll(Sort colName);
}
