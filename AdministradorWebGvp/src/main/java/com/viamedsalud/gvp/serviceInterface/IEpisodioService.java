package com.viamedsalud.gvp.serviceInterface;

import com.viamedsalud.gvp.modelo.Episodio;

import java.util.List;
import java.util.Optional;

public interface IEpisodioService {
	public List<Episodio> listar();

	public Optional<Episodio> listarId(int id);

	public int saveEpisodio(Episodio e);

	public void delete(int id);

	public Episodio findByIdAndId(int id, int id1);
}
