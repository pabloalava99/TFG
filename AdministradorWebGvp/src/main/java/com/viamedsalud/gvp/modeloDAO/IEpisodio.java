package com.viamedsalud.gvp.modeloDAO;

import com.viamedsalud.gvp.modelo.Episodio;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IEpisodio extends CrudRepository<Episodio, Integer>{
    Episodio findFirstByOrderByIdDesc();

    Episodio findByIdAndId(int id, int id1);
    
}
