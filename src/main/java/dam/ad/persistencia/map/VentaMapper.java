package dam.ad.persistencia.map;

import dam.ad.persistencia.dto.VentaDTO;
import dam.ad.modelo.Venta;

import java.util.ArrayList;
import java.util.List;

public class VentaMapper {

    static {
        SINGLETON = new VentaMapper();
    }

    public static final VentaMapper SINGLETON;

    private VentaMapper() {}

    public VentaDTO toDTO(Venta venta) {
        return new VentaDTO(
                venta.getId(),
                venta.getFecha(),
                HamburguesaMapper.SINGLETON.toDTO(venta.getHamburguesa()),
                venta.getCantidad()
        );
    }

    public List<VentaDTO> toDTO(List<Venta> ventas) {
        List<VentaDTO> ventasDTO = new ArrayList<>();
        if (ventas != null) {
            for (Venta venta : ventas) {
                ventasDTO.add(toDTO(venta));
            }
        }
        return ventasDTO;
    }

    public Venta toEntity(VentaDTO dto) {
        return new Venta(
                dto.getId(),
                dto.getFecha(),
                HamburguesaMapper.SINGLETON.toEntity(dto.getHamburguesa()),
                dto.getCantidad()
        );
    }

    public List<Venta> toEntity(List<VentaDTO> ventasDTO) {
        List<Venta> ventas = new ArrayList<>();
        if (ventasDTO != null) {
            for (VentaDTO dto : ventasDTO) {
                ventas.add(toEntity(dto));
            }
        }
        return ventas;
    }
}
