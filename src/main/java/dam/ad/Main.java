package dam.ad;

import dam.ad.modelo.Venta;
import dam.ad.persistencia.conf.Configuracion;
import dam.ad.persistencia.dao.HamburguesaDAO;
import dam.ad.persistencia.dao.VentaDAO;
import dam.ad.persistencia.dto.HamburguesaDTO;
import dam.ad.almacenamiento.mysql.hib.HibernateUtil;
import dam.ad.persistencia.map.*;
import dam.ad.modelo.Hamburguesa;

import java.util.List;
import java.util.Scanner;


public class Main {
    private static Scanner consola = new Scanner(System.in);
    private static HamburguesaMapper hamburguesaMapper = HamburguesaMapper.SINGLETON;
    private static VentaMapper ventaMapper = VentaMapper.SINGLETON;
    private static Configuracion configuracion = Configuracion.SINGLETON;
    // Obtenemos los DAO actuales de configuracion
    private static HamburguesaDAO hamburguesaDAO = null;
    private static VentaDAO ventaDAO = null;


    public static void main(String[] args) {
        cargar();
        menuPrincipal();
    }

    public static void cargar() {
        hamburguesaDAO = configuracion.getHamburguesaDAO();
        ventaDAO = configuracion.getVentaDAO();
    }

    private static List<Hamburguesa> getHamburguesas() {
        return hamburguesaMapper.toEntity(hamburguesaDAO.read());
    }

    private static List<Venta> getVentas() {
        return ventaMapper.toEntity(ventaDAO.read());
    }

    public static void menuPrincipal() {
        System.out.println("¿que desea hacer?");
        int opcion;
        boolean continuar = true;
        int numOpciones = 13;
        while (continuar) {
            do {
                System.out.println("0. Salir");
                System.out.println("1. Cambiar persistencia");
                System.out.println("2. Crear Hamburguesa");
                System.out.println("3. Listar Hamburguesas");
                System.out.println("4. Leer Hamburguesa");
                System.out.println("5. Actualizar Hamburguesa");
                System.out.println("6. Borrar Hamburguesa");
                // Acciones sobre el modelo
                System.out.println("7. Cocinar Hamburguesa");
                System.out.println("8. Incrementar precio Hamburguesa");
                System.out.println("9. Crear Venta");
                System.out.println("10. Listar Ventas");
                System.out.println("11. Leer Venta");
                System.out.println("12. Actualizar Venta");
                System.out.println("13. Borrar Venta");
                opcion = leerEntero(0, numOpciones);
            } while(opcion < 0 || opcion > numOpciones);

            switch (opcion) {
                case 0 -> continuar = false;
                case 1 -> menuCambiarPersistencia();
                case 2 -> menuCrearHamburguesa();
                case 3 -> listarHamburguesas();
                case 4 -> leerHamburguesa();
                case 5 -> actualizarHamburguesa();
                case 6 -> borrarHamburguesa();
                case 7 -> cocinarHamburguesa();
                case 8 -> incrementarHamburguesa();
                case 9 -> crearVenta();
                case 10 -> listarVentas();
                case 11 -> leerVenta();
                case 12 -> actualizarVenta();
                case 13 -> borrarVenta();
                default -> throw new RuntimeException("No se puede hacer una opción");
            }
        }
        cerrar();
    }

    public static void menuCambiarPersistencia() {
        System.out.println("¿que modo de persistencia desea?");
        int opcion;
        int numOpciones = 5;

        do {
            System.out.println("0. Salir");
            System.out.println("1. JDBC");
            System.out.println("2. Hibernate");
            System.out.println("3. DB4O");
            System.out.println("4. Binario");
            System.out.println("5. CSV");
            opcion = leerEntero(1, numOpciones);
        } while(opcion < 0 || opcion > numOpciones);

        switch (opcion) {
            case 0 -> System.out.print("");
            case 1 -> configuracion.setJDBC();
            case 2 -> configuracion.setHibernate();
            case 3 -> configuracion.setDB4O();
            case 4 -> configuracion.setBinario();
            case 5 -> configuracion.setCSV();
            default -> throw new RuntimeException("No se puede hacer una opción");
        }
        cargar();
    }

    public static void menuCrearHamburguesa() {
        System.out.println("Vamos a crear una hamburguesa");
        System.out.println("Necesito un nombre para la hamburguesa");
        String nombre = "";
        while(nombre.isBlank()){
            nombre = consola.nextLine();
        }
        System.out.println("Ahora dime el coste de " + nombre);
        Double coste = Double.valueOf(consola.nextLine().trim());
        // Creamos la hamburguesa
        Hamburguesa hamburguesa = new Hamburguesa(nombre, coste);
        // Pasamos de modelo a DTO
        HamburguesaDTO hamburguesaDTO = hamburguesaMapper.toDTO(hamburguesa);

        // Guardamos la hamburguesa
        hamburguesaDAO.create(hamburguesaDTO);
        // Y tambien la añadimos a la lista
        getHamburguesas().add(hamburguesa);
        System.out.println("Hamburguesa creada: " + hamburguesa);
    }

    public static void listarHamburguesas() {
        System.out.println("Listado de Hamburguesas");
        for (Hamburguesa hamburguesa : getHamburguesas()) {
            System.out.println(hamburguesa);
        }
    }

    public static void leerHamburguesa() {
        System.out.println("Ingrese el id de la hamburguesa a leer");
        int id = leerEntero();
        HamburguesaDTO hamburguesa = hamburguesaDAO.read(id);
        if (hamburguesa == null) System.out.println("No se encontró la hamburguesa");
        else System.out.println(hamburguesa);
    }

    public static void actualizarHamburguesa() {
        System.out.println("Ingrese el id de la hamburguesa a actualizar");
        int id = leerEntero();
        HamburguesaDTO hamburguesa = hamburguesaDAO.read(id);
        if (hamburguesa == null) System.out.println("No se encontró la hamburguesa");
        else {
            System.out.println("Nuevo nombre para "+hamburguesa.getNombre());
            String nombre;
            while ((nombre = consola.nextLine().trim()).isBlank()) {}
            hamburguesa.setNombre(nombre);
            System.out.println("Nuevo precio para " + nombre);
            hamburguesa.setCoste(Double.valueOf(consola.nextLine().trim()));
            System.out.println(hamburguesa);
            hamburguesaDAO.update(hamburguesa);
        }
    }

    public static void borrarHamburguesa() {
        System.out.println("Ingrese el id de la hamburguesa a borrar");
        int id = leerEntero();
        HamburguesaDTO hamburguesa = hamburguesaDAO.read(id);
        System.out.println(hamburguesaDAO.delete(hamburguesa)?"Hamburguesa borrada":"No se ha podido borrar la hamburguesa");
    }

    public static void cocinarHamburguesa() {
        System.out.println("Ingrese el id de la hamburguesa a cocinar");
        int id = leerEntero();
        Hamburguesa hamburguesa = hamburguesaMapper.toEntity(hamburguesaDAO.read(id));
        if (hamburguesa == null) System.out.println("No se encontró la hamburguesa");
        else hamburguesa.cocinar();
    }

    public static void incrementarHamburguesa() {
        System.out.println("Ingrese el id de la hamburguesa a incrementar su coste");
        int id = leerEntero();
        Hamburguesa hamburguesa = hamburguesaMapper.toEntity(hamburguesaDAO.read(id));
        if (hamburguesa == null) System.out.println("No se encontró la hamburguesa");
        else {
            System.out.println("Cuanto desea incrementar el coste de la hamburguesa "+hamburguesa.getNombre()+ " (actual = " + hamburguesa.getCoste() + ")");
            hamburguesa.incrementarCoste(Double.valueOf(consola.nextLine().trim()));
            hamburguesaDAO.update(hamburguesaMapper.toDTO(hamburguesa));
        }
    }

    public static void crearVenta() {
        System.out.println("9. Crear Venta");
    }

    public static void listarVentas() {
        System.out.println("Listado de Ventas");
        for (Venta venta : getVentas()) {
            System.out.println(venta);
        }
    }

    public static void leerVenta() {
        System.out.println("11. Leer Venta");
    }

    public static void actualizarVenta() {
        System.out.println("12. Actualizar Venta");
    }

    public static void borrarVenta() {
        System.out.println("13. Borrar Venta");
    }

    public static void cerrar() {
        consola.close();
        HibernateUtil.shutdown();
    }

    public static int leerEntero() {
        return leerEntero(0, Integer.MAX_VALUE);
    }

    public static int leerEntero(int desde, int hasta) {
        int entero = Integer.MAX_VALUE;
        if (desde > hasta) {
            int aux = desde;
            hasta = desde;
            desde = aux;
        }

        do {
            try {
                if (hasta == Integer.MAX_VALUE) System.out.println("Ingrese un numero (mínimo = " + desde + ")");
                else System.out.println("Ingrese un entero entre " + desde + " y " + hasta);
                entero = Integer.valueOf(consola.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Eso no es un numero");
            }
        } while(entero < desde || entero > hasta);
        return entero;
    }
}
