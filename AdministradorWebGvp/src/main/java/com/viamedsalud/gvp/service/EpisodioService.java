package com.viamedsalud.gvp.service;

import com.viamedsalud.gvp.modelo.Episodio;
import com.viamedsalud.gvp.modeloDAO.IEpisodio;
import com.viamedsalud.gvp.serviceInterface.IEpisodioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EpisodioService implements IEpisodioService {

    @Autowired
    private IEpisodio dao;

    @Override
    public List<Episodio> listar() {
        return (List<Episodio>) dao.findAll();
    }

    @Override
    public Optional<Episodio> listarId(int id) {
        return dao.findById(id);
    }

    @Override
    public int saveEpisodio(Episodio e) {
        //Fecha Actual
        Date fechaActual = new Date();
        e.setFecha(fechaActual);


        //Añadimos el nuevo episodio
        if (e.getEpisodio() == null) {
            Episodio episodioUltimo = dao.findFirstByOrderByIdDesc();
            String nuevoEpisodio = "AMN" + (Integer.parseInt(episodioUltimo.getEpisodio().substring(3)) + 1);
            e.setEpisodio(nuevoEpisodio);
        }else {
            e.setEpisodio(dao.findByIdAndId(e.getId(),e.getId()).getEpisodio());
        }

        int res = 0;
        Episodio per = dao.save(e);
        if (!per.equals(null)) {
            res = 1;
        }
        return res;
    }

    @Override
    public void delete(int id) {
        dao.deleteById(id);
    }


    @Override
    public Episodio findByIdAndId(int id, int id1) {
        return dao.findByIdAndId(id,id1);
    }


}
