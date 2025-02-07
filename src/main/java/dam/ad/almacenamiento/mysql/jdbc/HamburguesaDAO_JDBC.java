package dam.ad.almacenamiento.mysql.jdbc;

import dam.ad.almacenamiento.mysql.HamburguesaDAOMySQL;
import dam.ad.persistencia.dto.HamburguesaDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HamburguesaDAO_JDBC implements HamburguesaDAOMySQL {

    public static HamburguesaDAO_JDBC SINGLETON = new HamburguesaDAO_JDBC();

    private static String queryInsert = "INSERT INTO Hamburguesa (id, nombre, coste) VALUES (?, ?, ?)";
    private static String querySelect = "SELECT id, nombre, coste FROM Hamburguesa WHERE id = ?";
    private static String querySelectAll = "SELECT id, nombre, coste FROM Hamburguesa";
    private static String queryUpdate = "UPDATE Hamburguesa SET nombre = ?, coste = ? WHERE id = ?";
    private static String queryDelete = "DELETE FROM Hamburguesa WHERE id = ?";
    private static String queryDeleteAll = "DELETE FROM Hamburguesa";


    private HamburguesaDAO_JDBC() {}

    @Override
    public void create(HamburguesaDTO hamburguesa) {

        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);

             PreparedStatement stmt = conexion.prepareStatement(queryInsert)) {

            stmt.setInt(1, hamburguesa.getId());
            stmt.setString(2, hamburguesa.getNombre());
            stmt.setDouble(3, hamburguesa.getCoste());

            int filasInsertadas = stmt.executeUpdate();
            if (filasInsertadas > 0) {
                System.out.println("Hamburguesa insertada correctamente.");
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public HamburguesaDTO read(int id) {
        HamburguesaDTO hamburguesa = null;
        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);
             PreparedStatement stmt = conexion.prepareStatement(querySelect)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                hamburguesa = new HamburguesaDTO(rs.getInt("id"),
                                                 rs.getString("nombre"),
                                                 rs.getDouble("coste"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hamburguesa;
    }

    @Override
    public List<HamburguesaDTO> read() {
        List<HamburguesaDTO> hamburguesas = new ArrayList<>();

        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);
             PreparedStatement stmt = conexion.prepareStatement(querySelectAll);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                HamburguesaDTO hamburguesa = new HamburguesaDTO(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("coste"));
                hamburguesas.add(hamburguesa);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hamburguesas;
    }

    @Override
    public boolean update(HamburguesaDTO hamburguesa) {
        boolean actualizado = false;
        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);
             PreparedStatement stmt = conexion.prepareStatement(queryUpdate)) {

            stmt.setString(1, hamburguesa.getNombre());
            stmt.setDouble(2, hamburguesa.getCoste());
            stmt.setInt(3, hamburguesa.getId());

            int filasActualizadas = stmt.executeUpdate();
            if (filasActualizadas > 0) {
                actualizado = true;
                System.out.println("Hamburguesa actualizada correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return actualizado;
    }

    @Override
    public boolean delete(HamburguesaDTO hamburguesa) {
        boolean eliminado = false;
        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);
             PreparedStatement stmt = conexion.prepareStatement(queryDelete)) {

            stmt.setInt(1, hamburguesa.getId());

            int filasBorradas = stmt.executeUpdate();
            if (filasBorradas > 0) {
                eliminado = true;
                System.out.println("Hamburguesa eliminada correctamente.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eliminado;
    }

    @Override
    public boolean delete() {
        boolean eliminados = false;
        try (Connection conexion = DriverManager.getConnection(JDBC_Util.URL, JDBC_Util.USUARIO, JDBC_Util.PASS);
             PreparedStatement stmt = conexion.prepareStatement(queryDeleteAll)) {

            int filasBorradas = stmt.executeUpdate();
            if (filasBorradas > 0) {
                eliminados = true;
            }
            stmt.executeUpdate("ALTER TABLE hamburguesa AUTO_INCREMENT = 1");


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eliminados;
    }

}
