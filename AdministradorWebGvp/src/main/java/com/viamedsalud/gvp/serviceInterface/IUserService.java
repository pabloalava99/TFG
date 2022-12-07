package com.viamedsalud.gvp.serviceInterface;

import java.util.List;
import java.util.Optional;

import com.viamedsalud.gvp.modelo.User;

public interface IUserService {
	public List<User> listar();

	public Optional<User> listarId(int id);

	public int saveUser(User u);

	public void delete(int id);
}
