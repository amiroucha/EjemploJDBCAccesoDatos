package dam.ad.persistencia.dao;

import dam.ad.persistencia.dto.VentaDTO;

import java.util.List;

public interface VentaDAO {

    // Métodos CRUD
    public void create(VentaDTO venta);
    public VentaDTO read(int id);
    public List<VentaDTO> read();
    public boolean update(VentaDTO venta);
    public boolean delete(VentaDTO venta);
    public boolean delete();


    public default boolean migrate(VentaDAO origen) {
        this.delete();
        for (VentaDTO venta: origen.read()) this.create(venta);
        return true;
    }

}
