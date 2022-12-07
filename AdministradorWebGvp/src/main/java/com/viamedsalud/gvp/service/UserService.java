package com.viamedsalud.gvp.service;

import com.viamedsalud.gvp.modelo.User;
import com.viamedsalud.gvp.modeloDAO.IUser;
import com.viamedsalud.gvp.serviceInterface.IUserService;
import com.viamedsalud.gvp.util.Bcrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    @Autowired
    private IUser dao;



    //Util hashear contraseñas
    private Bcrypt bcrypt;

    @Override
    public List<User> listar() {
        return (List<User>) dao.findAll();
    }

    @Override
    public Optional<User> listarId(int id) {
        return dao.findById(id);
    }

    @Override
    public int saveUser(User u) {
        Date fechaActual = new Date();
        if (u.getFechaCreacion() == null)
            u.setFechaCreacion(fechaActual);
        u.setFechaModificacion(fechaActual);
        bcrypt = new Bcrypt();
        u.setHash(bcrypt.hashear(u.getHash()));
        int res = 0;
        User per = dao.save(u);
        if (!per.equals(null)) {
            res = 1;
        }
        return res;
    }

    @Override
    public void delete(int id) {
        dao.deleteById(id);
    }



}
