package dam.ad.persistencia.conf;

import dam.ad.almacenamiento.bin.HamburguesaDAO_Binario;
import dam.ad.almacenamiento.csv.HamburguesaDAO_CSV;
import dam.ad.almacenamiento.db4o.HamburguesaDAO_DB4O;
import dam.ad.almacenamiento.db4o.VentaDAO_DB4O;
import dam.ad.persistencia.dao.HamburguesaDAO;
import dam.ad.almacenamiento.mysql.hib.HamburguesaDAOHibernate;
import dam.ad.almacenamiento.mysql.jdbc.HamburguesaDAO_JDBC;
import dam.ad.persistencia.dao.VentaDAO;

public class Configuracion {

    // Patrón Singleton. Al ser final puede ser publico
    public final static Configuracion SINGLETON = new Configuracion();

    // Singletons de todos los DAO de Hamburguesa
    private HamburguesaDAO_JDBC hamburguesaDAO_JDBC = HamburguesaDAO_JDBC.SINGLETON;
    private HamburguesaDAOHibernate hamburguesaDAOHibernate = HamburguesaDAOHibernate.SINGLETON;
    private HamburguesaDAO_DB4O hamburguesaDAO_DB4O = HamburguesaDAO_DB4O.SINGLETON;
    private HamburguesaDAO_Binario hamburguesaDAOBinario = HamburguesaDAO_Binario.SINGLETON;
    private HamburguesaDAO_CSV hamburguesaDAO_CSV = HamburguesaDAO_CSV.SINGLETON;
    // Singletons de todos los DAO de Venta -> Solo está implementado el de DB4O
    private VentaDAO_DB4O ventaDAO_DB4O = VentaDAO_DB4O.SINGLETON;

    // Guardamos el DAO actual y el anterior
    private HamburguesaDAO hamburguesaDAO = hamburguesaDAO_JDBC;
    private HamburguesaDAO hamburguesaDAO_old = null;
    // Para Venta solo tenemos uno. Por eso no necesitamos guardar el antiguo
    private VentaDAO ventaDAO = ventaDAO_DB4O;

    // Por el patrón Singleton, ponemos el constructor privado para que nadie pueda usarlo
    private Configuracion () {}

    /**
     * Método privado que sirve para cambiar el DAO actual
     * @param newHamburguesaDAO Nuevo DAO a actualizar
     */
    private void setMode(HamburguesaDAO newHamburguesaDAO, VentaDAO newVentaDAO) {
        if (newHamburguesaDAO == hamburguesaDAO) System.out.println("Ya estás en ese modo de persistencia");
        else {
            // Actualizamos al nuevo y el viejo DAO
            hamburguesaDAO_old = hamburguesaDAO;
            hamburguesaDAO = newHamburguesaDAO;
            // Después migramos para mantener la consistencia
            hamburguesaDAO.migrate(hamburguesaDAO_old);
            // Lo dejamos preparado para Venta aunque realmente pasamos el mismo objeto que ya tenemos guardado
            ventaDAO = newVentaDAO;
            // No hacemos la migración de Venta porque no tenemos implementados más persistencias para Venta
        }

    }

    public void setJDBC () {setMode(hamburguesaDAO_JDBC, ventaDAO_DB4O);}
    public void setHibernate () {setMode(hamburguesaDAOHibernate, ventaDAO_DB4O);}
    public void setDB4O () {setMode(hamburguesaDAO_DB4O, ventaDAO_DB4O);}
    public void setBinario () {setMode(hamburguesaDAOBinario, ventaDAO_DB4O);}
    public void setCSV () {setMode(hamburguesaDAO_CSV, ventaDAO_DB4O);}


    public HamburguesaDAO getHamburguesaDAO() {
        return hamburguesaDAO;
    }

    public VentaDAO getVentaDAO() {
        return ventaDAO;
    }

}
