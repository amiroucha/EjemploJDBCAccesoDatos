package dam.ad.almacenamiento.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.Db4oEmbedded;
import dam.ad.persistencia.dao.VentaDAO;
import dam.ad.persistencia.dto.VentaDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VentaDAO_DB4O implements VentaDAO {
    public static final VentaDAO_DB4O SINGLETON = new VentaDAO_DB4O();

    private VentaDAO_DB4O() {
        File file = new File(DB4O_Util.PATH);
        if (!file.exists()) file.mkdirs();
        file = new File(DB4O_Util.VENTA_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void create(VentaDTO venta) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            db.store(venta);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public VentaDTO read(int id) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            ObjectSet<VentaDTO> result = db.queryByExample(new VentaDTO(id, null, null, 0));
            return result.hasNext() ? result.next() : null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public List<VentaDTO> read() {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            ObjectSet<VentaDTO> result = db.query(VentaDTO.class);
            return new ArrayList<>(result);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public boolean update(VentaDTO venta) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            ObjectSet<VentaDTO> result = db.queryByExample(new VentaDTO(venta.getId(), null, null, 0));
            if (result.hasNext()) {
                VentaDTO ventaActual = result.next();
                ventaActual.setFecha(venta.getFecha());
                ventaActual.setHamburguesa(venta.getHamburguesa());
                ventaActual.setCantidad(venta.getCantidad());
                db.store(ventaActual);
                return true;
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    @Override
    public boolean delete(VentaDTO venta) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            ObjectSet<VentaDTO> result = db.queryByExample(venta);
            if (result.hasNext()) {
                db.delete(result.next());
                return true;
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }

    @Override
    public boolean delete() {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.VENTA_PATH);
            ObjectSet<VentaDTO> result = db.query(VentaDTO.class);
            if (!result.isEmpty()) {
                for (VentaDTO venta : result) {
                    db.delete(venta);
                }
                return true;
            }
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return false;
    }
}
