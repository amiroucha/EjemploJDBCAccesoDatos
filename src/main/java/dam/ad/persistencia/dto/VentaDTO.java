package dam.ad.persistencia.dto;

public class VentaDTO {
    private int id;
    private String fecha;
    private HamburguesaDTO hamburguesa;
    private int cantidad;


    public VentaDTO() {}

    public VentaDTO(int id, String fecha, HamburguesaDTO hamburguesa, int cantidad) {
        this.id = id;
        this.fecha = fecha;
        this.hamburguesa = hamburguesa;
        this.cantidad = cantidad;
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

    public HamburguesaDTO getHamburguesa() {
        return hamburguesa;
    }

    public void setHamburguesa(HamburguesaDTO hamburguesa) {
        this.hamburguesa = hamburguesa;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

}
