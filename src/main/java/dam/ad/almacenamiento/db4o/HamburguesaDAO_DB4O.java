package dam.ad.almacenamiento.db4o;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.Db4oEmbedded;
import dam.ad.persistencia.dao.HamburguesaDAO;
import dam.ad.persistencia.dto.HamburguesaDTO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




public class HamburguesaDAO_DB4O implements HamburguesaDAO {

    public static HamburguesaDAO_DB4O SINGLETON = new HamburguesaDAO_DB4O();


    private HamburguesaDAO_DB4O() {
        File file = new File(DB4O_Util.PATH);
        if (!file.exists()) file.mkdirs();
        file = new File(DB4O_Util.HAMBURGUESA_PATH);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void create(HamburguesaDTO hamburguesa) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            db.store(hamburguesa);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public HamburguesaDTO read(int id) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            ObjectSet<HamburguesaDTO> result = db.queryByExample(new HamburguesaDTO(id, null, 0.0));
            return result.hasNext() ? result.next() : null;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public List<HamburguesaDTO> read() {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            ObjectSet<HamburguesaDTO> result = db.query(HamburguesaDTO.class);
            return new ArrayList<>(result);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    @Override
    public boolean update(HamburguesaDTO hamburguesa) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            ObjectSet<HamburguesaDTO> result = db.queryByExample(new HamburguesaDTO(hamburguesa.getId(), null, 0.0));
            if (result.hasNext()) {
                HamburguesaDTO hamburguesaActual = result.next();
                hamburguesaActual.setNombre(hamburguesa.getNombre());
                hamburguesaActual.setCoste(hamburguesa.getCoste());
                db.store(hamburguesaActual);
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
    public boolean delete(HamburguesaDTO hamburguesa) {
        ObjectContainer db = null;
        try {
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            ObjectSet<HamburguesaDTO> result = db.queryByExample(hamburguesa);
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
            db = Db4oEmbedded.openFile(DB4O_Util.HAMBURGUESA_PATH);
            ObjectSet<HamburguesaDTO> result = db.query(HamburguesaDTO.class);
            if (!result.isEmpty()) {
                for (HamburguesaDTO hamburguesa : result) {
                    db.delete(hamburguesa);
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
