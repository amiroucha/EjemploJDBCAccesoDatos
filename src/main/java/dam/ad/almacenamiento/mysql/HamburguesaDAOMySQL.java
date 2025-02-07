package dam.ad.almacenamiento.mysql;

import dam.ad.persistencia.dao.HamburguesaDAO;

public interface HamburguesaDAOMySQL extends HamburguesaDAO {
    @Override
    public default boolean migrate(HamburguesaDAO origen) {
        if (origen instanceof HamburguesaDAOMySQL) {
            System.out.println("No es necesaria la migración, ya es compatible con MySQL.");
            return false;
        }

        return HamburguesaDAO.super.migrate(origen);
    }
}
