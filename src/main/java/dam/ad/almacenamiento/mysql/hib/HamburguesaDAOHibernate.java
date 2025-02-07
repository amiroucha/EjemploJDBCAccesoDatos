package dam.ad.almacenamiento.mysql.hib;

import dam.ad.almacenamiento.mysql.HamburguesaDAOMySQL;
import dam.ad.persistencia.dto.HamburguesaDTO;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class HamburguesaDAOHibernate implements HamburguesaDAOMySQL {

    public static HamburguesaDAOHibernate SINGLETON = new HamburguesaDAOHibernate();

    private HamburguesaDAOHibernate() {}

    @Override
    public void create(HamburguesaDTO hamburguesa) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(hamburguesa);
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();  // Puedes loguearlo o agregar más detalles
        }
    }

    @Override
    public HamburguesaDTO read(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(HamburguesaDTO.class, id);
        } catch (Exception e) {
            e.printStackTrace();  // Loguea o maneja el error
            return null;
        }
    }

    @Override
    public List<HamburguesaDTO> read() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<HamburguesaDTO> query = session.createQuery("FROM HamburguesaDTO", HamburguesaDTO.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();  // Maneja el error de manera apropiada
            return null;
        }
    }

    @Override
    public boolean update(HamburguesaDTO hamburguesa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(hamburguesa);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();  // Agregar un log detallado
            return false;
        }
    }

    @Override
    public boolean delete(HamburguesaDTO hamburguesa) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.delete(hamburguesa);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();  // Loguea el error para más detalles
            return false;
        }
    }

    @Override
    public boolean delete() {
        boolean success = false;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();

            Query query = session.createQuery("DELETE FROM HamburguesaDTO");
            int rowsAffected = query.executeUpdate();
            session.createNativeQuery("ALTER TABLE hamburguesa AUTO_INCREMENT = 1").executeUpdate();

            transaction.commit();
            success = true;

            System.out.println("Se eliminaron " + rowsAffected + " hamburguesas.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return success;
    }

}
