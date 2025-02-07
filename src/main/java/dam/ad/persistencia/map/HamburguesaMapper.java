package dam.ad.persistencia.map;

import dam.ad.persistencia.dto.HamburguesaDTO;
import dam.ad.modelo.Hamburguesa;

import java.util.ArrayList;
import java.util.List;

public class HamburguesaMapper {

    static {
        SINGLETON = new HamburguesaMapper();
    }

    public static final HamburguesaMapper SINGLETON;

    private HamburguesaMapper() {}

    public HamburguesaDTO toDTO(Hamburguesa hamburguesa) {
        return new HamburguesaDTO(
                hamburguesa.getID(),
                hamburguesa.getNombre(),
                hamburguesa.getCoste()
        );
    }

    public List<HamburguesaDTO> toDTO(List<Hamburguesa> hamburguesas) {
        List<HamburguesaDTO> hamburguesasDTO = new ArrayList<>();
        if (hamburguesas != null) {
            for (Hamburguesa hamburguesa: hamburguesas) {
                hamburguesasDTO.add(toDTO(hamburguesa));
            }
        }
        return hamburguesasDTO;
    }

    public Hamburguesa toEntity(HamburguesaDTO dto) {
        return new Hamburguesa(
                dto.getId(),
                dto.getNombre(),
                dto.getCoste()
        );
    }

    public List<Hamburguesa> toEntity(List<HamburguesaDTO> hamburguesasDTO) {
        List<Hamburguesa> hamburguesas = new ArrayList<>();
        if (hamburguesasDTO != null) {
            for (HamburguesaDTO dto: hamburguesasDTO) {
                hamburguesas.add(toEntity(dto));
            }
        }
        return hamburguesas;
    }
}
