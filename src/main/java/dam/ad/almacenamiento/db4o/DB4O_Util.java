package dam.ad.almacenamiento.db4o;

import java.io.File;

public class DB4O_Util {

    public static final String PATH = "."+ File.separator+"DB4O";
    public static final String EXTENSION = ".db4o";
    public static final String HAMBURGUESA_PATH = PATH +File.separator+"Hamburguesa"+EXTENSION;
    public static final String VENTA_PATH = PATH +File.separator+"Venta"+EXTENSION;

    private DB4O_Util() {}
}
