package dam.ad.modelo;

public class Venta {
    private int id;
    private String fecha;
    private Hamburguesa hamburguesa;
    private int cantidad;

    private static int ultimoId = -1;

    public Venta(String fecha, Hamburguesa hamburguesa, int cantidad) {
        this.id = ++ultimoId;
        this.fecha = fecha;
        this.hamburguesa = hamburguesa;
        this.cantidad = cantidad;
    }

    public Venta(int id, String fecha, Hamburguesa hamburguesa, int cantidad) {
        this.id = id;
        this.fecha = fecha;
        this.hamburguesa = hamburguesa;
        this.cantidad = cantidad;
        if (ultimoId < id) ultimoId = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public Hamburguesa getHamburguesa() {
        return hamburguesa;
    }

    public void setHamburguesa(Hamburguesa hamburguesa) {
        this.hamburguesa = hamburguesa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "Venta{" +
                "id=" + id +
                ", fecha='" + fecha +
                ", hamburguesa" + hamburguesa.getNombre() +
                ", cantidad=" + cantidad +
                '}';
    }
}
