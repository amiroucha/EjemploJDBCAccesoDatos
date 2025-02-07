package dam.ad.almacenamiento.csv;

import java.io.File;

public class CSV_Util {


    public static final String PATH = "."+ File.separator+"csv";
    public static final String EXTENSION = ".csv";
    public static final String HAMBURGUESA_PATH = PATH +File.separator+"Hamburguesa"+EXTENSION;
    public static final String VENTA_PATH = PATH +File.separator+"Venta"+EXTENSION;
    public static final String SEPARATOR = ";";


    private CSV_Util() {}
}
