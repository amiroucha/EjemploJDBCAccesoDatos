package dam.ad.persistencia.dao;

import dam.ad.persistencia.dto.HamburguesaDTO;

import java.util.List;

public interface HamburguesaDAO {

    // Métodos CRUD
    public void create(HamburguesaDTO hamburguesa);
    public HamburguesaDTO read(int id);
    public List<HamburguesaDTO> read();
    public boolean update(HamburguesaDTO hamburguesa);
    public boolean delete(HamburguesaDTO hamburguesa);
    public boolean delete();


    public default boolean migrate(HamburguesaDAO origen) {
        this.delete();
        for (HamburguesaDTO hamburguesa: origen.read()) this.create(hamburguesa);
        return true;
    }

}
