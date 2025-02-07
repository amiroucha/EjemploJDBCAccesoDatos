package dam.ad.almacenamiento.bin;

import java.io.File;

public class BinarioUtil {


    public static final String PATH = "."+ File.separator+"bin";
    public static final String EXTENSION = ".bin";
    public static final String HAMBURGUESA_PATH = PATH +File.separator+"Hamburguesa"+EXTENSION;
    public static final String VENTA_PATH = PATH +File.separator+"Venta"+EXTENSION;


    private BinarioUtil() {}
}
