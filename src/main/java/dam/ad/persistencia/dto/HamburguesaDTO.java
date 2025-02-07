package dam.ad.persistencia.dto;

import javax.persistence.*;

@Entity
@Table(name = "hamburguesa")
public class HamburguesaDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private double coste;

    public HamburguesaDTO() {}

    public HamburguesaDTO(int id, String nombre, double coste) {
        this.id = id;
        this.nombre = nombre;
        this.coste = coste;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
        return "Hamburguesa: " +
                "id=" + id +
                ", nombre='" + nombre +
                ", coste=" + coste;
    }
}
