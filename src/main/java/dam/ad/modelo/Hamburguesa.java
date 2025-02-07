package dam.ad.modelo;

public class Hamburguesa {
    private final int ID;
    private String nombre;
    private double coste;

    private static int ultimoId = -1;

    public Hamburguesa(String nombre, double coste) {
        this.ID = ++ultimoId;
        this.nombre = nombre;
        this.coste = coste;
    }

    public Hamburguesa(int ID, String nombre, double coste) {
        this.ID = ID;
        this.nombre = nombre;
        this.coste = coste;
        if (ultimoId < ID) ultimoId = ID;
    }

    public int getID() {
        return ID;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    @Override
    public String toString() {
        return "Hamburguesa{" +
                "id=" + ID +
                ", nombre='" + nombre +
                ", coste=" + coste +
                '}';
    }

    // Métodos del modelo. Simula los métodos propios de la clase, de la lógica de negocio de la aplicacion

    public void cocinar() {
        System.out.println("Cocinando una rica hamburguesa llamada "+nombre);
    }

    public void incrementarCoste(double costeExtra) {
        this.coste += costeExtra;
    }

}
