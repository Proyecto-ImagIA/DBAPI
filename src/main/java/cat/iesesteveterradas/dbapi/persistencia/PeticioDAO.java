package cat.iesesteveterradas.dbapi.persistencia;

import java.time.LocalDateTime;
import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PeticioDAO {
    private static final Logger logger = LoggerFactory.getLogger(PeticioDAO.class);

    public static Peticio crearPeticio(String prompt, LocalDateTime data, String model, String imatgeb64, Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Peticio peticio = null;
        byte[] imatge = null;
        
        if (imatgeb64 != null) {
            imatge = java.util.Base64.getDecoder().decode(imatgeb64);
        }

        try {
            tx = session.beginTransaction();
            peticio = new Peticio(prompt, data, model, imatge, usuari);
            session.save(peticio);
            tx.commit();
            logger.info("Nova petició creada amb el prompt: {}", prompt);
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            peticio = null;
            logger.error("Error al crear la petició", e);
        } finally {
            session.close();
        }
        return peticio;
    }

    public static Collection<Peticio> llistarPeticions() {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Collection<Peticio> peticions = null;
        try {
            tx = session.beginTransaction();
            peticions = session.createQuery("FROM Peticio").list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al llistar les peticions", e);
        } finally {
            session.close();
        }
        return peticions;
    }

    public static Collection<Peticio> llistarPeticionsPerUsuari(Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Collection<Peticio> peticions = null;
        try {
            tx = session.beginTransaction();
            Query<Peticio> query = session.createQuery("FROM Peticio WHERE usuari = :usuari", Peticio.class);
            query.setParameter("usuari", usuari);
            peticions = query.list();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al llistar les peticions per usuari", e);
        } finally {
            session.close();
        }
        return peticions;
    }

    public static void eliminaPeticio(Peticio peticio) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(peticio);
            tx.commit();
            logger.info("Petició eliminada amb èxit: {}", peticio.getPeticioId());
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al eliminar la petició", e);
        } finally {
            session.close();
        }
    }

    public static Peticio getPeticioPerId(Long peticioId) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Peticio peticio = null;
        try {
            peticio = session.get(Peticio.class, peticioId);
        } catch (HibernateException e) {
            logger.error("Error al trobar la petició per id", e);
        } finally {
            session.close();
        }
        return peticio;
    }


}
