package dam.ad.almacenamiento.csv;

import dam.ad.persistencia.dao.HamburguesaDAO;
import dam.ad.persistencia.dto.HamburguesaDTO;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class HamburguesaDAO_CSV implements HamburguesaDAO {

    public static HamburguesaDAO_CSV SINGLETON = new HamburguesaDAO_CSV();
    private static final File CARPETA;

    static {
        CARPETA = new File(CSV_Util.PATH);
        if (!CARPETA.exists()) CARPETA.mkdirs();
    }
    private HamburguesaDAO_CSV() {}

    @Override
    public void create(HamburguesaDTO hamburguesa) {
        try(RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "rwd")) {
            raf.seek(raf.length()); // Nos colocamos al final para escribir a continuación de lo que ya hay
            raf.writeBytes(hamburguesa.getId() +
                              CSV_Util.SEPARATOR +
                              hamburguesa.getNombre() +
                              CSV_Util.SEPARATOR +
                              hamburguesa.getCoste() +
                              '\n');
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public HamburguesaDTO read(int id) {
        long filePointer = search(id);
        if (filePointer == -1) return null;
        HamburguesaDTO hamburguesa = null;
        try (RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "r")) {
            if (filePointer < raf.length()) {
                raf.seek(filePointer);
                String linea= raf.readLine();
                if (!linea.isBlank()) {
                    String[] partes = linea.split(CSV_Util.SEPARATOR);
                    hamburguesa =  new HamburguesaDTO(Integer.parseInt(partes[0]), partes[1], Double.parseDouble(partes[2]));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hamburguesa;
    }

    @Override
    public List<HamburguesaDTO> read() {
        return read(0L);
    }

    private List<HamburguesaDTO> read(long filePointer) {
        List<HamburguesaDTO> hamburguesas = new ArrayList<>();
        if (filePointer == -1) return hamburguesas;
        try (RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "r")) {
            if (filePointer < raf.length()) {
                raf.seek(filePointer);
                String linea;
                while ((linea = raf.readLine()) != null) {
                    String[] partes = linea.split(CSV_Util.SEPARATOR);
                    String id = partes[0];
                    String nombre = partes[1];
                    String coste = partes[2];
                    hamburguesas.add(new HamburguesaDTO(Integer.parseInt(id), nombre, Double.parseDouble(coste)));
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return hamburguesas;
    }

    @Override
    public boolean update(HamburguesaDTO hamburguesa) {
        if (hamburguesa == null) return false;
        long filePointer = search(hamburguesa.getId());
        if (filePointer == -1) return false;
        long newLilePointer = skipHamburguesa(filePointer);
        List<HamburguesaDTO> hamburguesas = newLilePointer != -1 ? read(newLilePointer) : new ArrayList<>();
        delete(filePointer);
        create(hamburguesa);
        for (HamburguesaDTO h : hamburguesas) create(h);
        return true;
    }

    @Override
    public boolean delete(HamburguesaDTO hamburguesa) {
        if (hamburguesa == null) return false;
        long filePointer = search(hamburguesa.getId());
        if (filePointer == -1) return false;
        long newLilePointer = skipHamburguesa(filePointer);
        List<HamburguesaDTO> hamburguesas = newLilePointer != -1 ? read(newLilePointer) : new ArrayList<>();
        delete(filePointer);
        for (HamburguesaDTO h : hamburguesas) create(h);
        return true;
    }

    @Override
    public boolean delete() {
        return delete(0L);
    }

    public boolean delete(long filePointer) {
        boolean borrado = true;

        try (RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "rw")) {
            raf.setLength(filePointer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            borrado = false;
        }
        return borrado;
    }

    /**
     * Busca una hamburguesa
     * @param id de la hamburguesa a leer
     * @return el puntero donde empieza la hamburguesa. -1 si no la encuentra
     */

    private long search(int id) {
        long filePointer = -1;
        try (RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "r")) {
            String linea;
            long filePointer_old;
            long filePointer_new = raf.getFilePointer();
            while ((linea = raf.readLine()) != null && !linea.isEmpty()) {
                filePointer_old = filePointer_new;
                filePointer_new = raf.getFilePointer();
                String[] partes = linea.split(CSV_Util.SEPARATOR);
                if (Integer.parseInt(partes[0]) == id) {
                    filePointer = filePointer_old;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return filePointer;
    }
    /**
     * Salta una hamburguesa
     * @param filePointer puntero que apunta a la hamburguesa a saltar
     * @return puntero que apunta a la siguiente hamburguesa. -1 si no hay mas
     */
    private long skipHamburguesa(long filePointer) {
        long newFilePointer = -1;
        if (filePointer != -1) {
            try (RandomAccessFile raf = new RandomAccessFile(CSV_Util.HAMBURGUESA_PATH, "r")) {
                if (filePointer < raf.length()) {
                    raf.seek(filePointer);
                    raf.readLine();
                    newFilePointer = raf.getFilePointer();
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        return newFilePointer;
    }


}
