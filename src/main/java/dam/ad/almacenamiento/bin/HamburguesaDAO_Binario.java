package dam.ad.almacenamiento.bin;

import dam.ad.persistencia.dao.HamburguesaDAO;
import dam.ad.persistencia.dto.HamburguesaDTO;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class HamburguesaDAO_Binario implements HamburguesaDAO {

    public static HamburguesaDAO_Binario SINGLETON = new HamburguesaDAO_Binario();
    private static final File CARPETA;
    private static int INT_SIZE = 4;
    private static int DOUBLE_SIZE = 8;


    static {
        CARPETA = new File(BinarioUtil.PATH);
        if (!CARPETA.exists()) CARPETA.mkdirs();
    }

    private HamburguesaDAO_Binario() {}

    /**
     * Guarda una hamburguesa en el fichero
     * @param hamburguesa el objeto DTO con la info a guardar
     */
    @Override
    public void create(HamburguesaDTO hamburguesa) {
        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "rwd")) {
            raf.seek(raf.length()); // Nos colocamos al final para escribir a continuación de lo que ya hay
            raf.writeInt(hamburguesa.getId());
            raf.writeUTF(hamburguesa.getNombre());
            raf.writeDouble(hamburguesa.getCoste());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Leemos una hamburguesa concreta
     * @param id de la hamburguesa a leer
     * @return devolvemos la hamburguesa. Si no la encontramos, null
     */

    @Override
    public HamburguesaDTO read(int id) {
        HamburguesaDTO hamburguesa = null;
        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "r")) {
            long filePointer = search(id); // Buscamos la posición en el fichero en el que empieza el objeto
            if (filePointer != -1) {
                raf.seek(filePointer+INT_SIZE); // El id ya lo tenemos. No necesitamos leerlo.
                String nombre = raf.readUTF();
                double coste = raf.readDouble();
                hamburguesa = new HamburguesaDTO(id, nombre, coste);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hamburguesa;
    }


    /**
     * Lee todas las hamburguesas del fichero
     * @return
     */
    @Override
    public List<HamburguesaDTO> read() {
        return read(0L);
    }


    /**
     * Lee todas las hamburguesas desde la posición indicada por el filePointer
     * @param filePointer puntero desde donde empieza a leer
     * @return lista de las hamburguesas leidas desde el filePointer
     */
    private List<HamburguesaDTO> read(long filePointer) {
        List<HamburguesaDTO> hamburguesas = new ArrayList<>();
        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "r")) {
            raf.seek(filePointer); // Nos movemos a la posición indicada para empezar a leer
            while (raf.getFilePointer() < raf.length()) { // Mientras no estemos al final del fichero seguimos leyendo
                int id = raf.readInt();
                String nombre = raf.readUTF();
                double coste = raf.readDouble();
                hamburguesas.add(new HamburguesaDTO(id, nombre, coste));
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hamburguesas;
    }

    /**
     * Actualiza una hamburguesa
     * @param hamburguesa el objeto a actualizar
     * @return true si ha ido bien la actualización. false en caso contrario
     */
    @Override
    public boolean update(HamburguesaDTO hamburguesa) {
        if (hamburguesa == null) return false;
        long filePointer = search(hamburguesa.getId());
        if (filePointer == -1) create(hamburguesa); // si no existe, creamos.
        else {
            long newFilePointer = skipHamburguesa(filePointer);
            List<HamburguesaDTO> hamburguesas = read(newFilePointer);
            delete(filePointer);
            create(hamburguesa);
            for (HamburguesaDTO hamburguesaActual : hamburguesas) {
                create(hamburguesaActual);
            }
        }
        return false;
    }

    /**
     * Borra una hamburguesa
     * @param hamburguesa el objeto a borrar
     * @return true si ha ido bien, false en caso contrario
     */
    @Override
    public boolean delete(HamburguesaDTO hamburguesa) {
        if (hamburguesa == null) return false;
        long filePointer = search(hamburguesa.getId());
        if (filePointer == -1) return false;
        long newFilePointer = skipHamburguesa(filePointer); // Saltamos la hamburguesa
        if (newFilePointer != -1) {
            List<HamburguesaDTO> hamburguesas = read(newFilePointer);
            delete(filePointer);
            for (HamburguesaDTO hamburguesaActual : hamburguesas) create(hamburguesaActual);
        }
        return true;
    }

    /**
     * Vacia el fichero
     * @return true si ha ido bien, false en caso contrario
     */
    @Override
    public boolean delete() {
        return delete(0L);
    }

    /**
     * borra todas las hamburguesas desde apunta el puntero
     * @param filePointer punto que apunta a desde donde hay que borrar
     * @return
     */
    private boolean delete(long filePointer) {
        boolean borradoCorrecto = true;
        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "rwd")) {
            raf.setLength(filePointer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            borradoCorrecto = false;
        }
        return borradoCorrecto;
    }

    /**
     * Salta una hamburguesa
     * @param filePointer puntero que apunta a la hamburguesa a saltar
     * @return puntero que apunta a la siguiente hamburguesa. -1 si no hay mas
     */
    private long skipHamburguesa(long filePointer) {
        long newFilePointer = -1;

        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "rwd")) {
            if (filePointer < raf.length()) {
                raf.seek(filePointer + INT_SIZE); // Saltamos el id
                raf.readUTF(); // Leemos el nombre para mover el puntero
                newFilePointer = raf.getFilePointer() + DOUBLE_SIZE; // Para el nuevo puntero saltamos el precio
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return newFilePointer;
    }

    // METODOS AUXILIARES

    /**
     * Busca una hamburguesa
     * @param id de la hamburguesa a leer
     * @return el puntero donde empieza la hamburguesa. -1 si no la encuentra
     */

    private long search(int id) {
        long filePointer = -1;
        try(RandomAccessFile raf = new RandomAccessFile(BinarioUtil.HAMBURGUESA_PATH, "r")) {
            while (raf.getFilePointer() < raf.length()) { // Mientras no estemos al final del fichero seguimos leyendo
                if (raf.readInt() == id) { // Lo hemos encontrado!!
                    // Al leer el fichero, el puntero avanza. Queremos la posición inicial
                    filePointer = raf.getFilePointer() - INT_SIZE;
                    break; // Ya no tenemos que seguir buscando
                } else {
                    raf.readUTF(); // Leemos el nombre SOLO para que el puntero avance
                    raf.skipBytes(DOUBLE_SIZE); // saltamos el precio
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return filePointer;
    }
}
