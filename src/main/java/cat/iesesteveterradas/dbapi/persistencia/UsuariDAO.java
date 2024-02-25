package cat.iesesteveterradas.dbapi.persistencia;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UsuariDAO {
    private static final Logger logger = LoggerFactory.getLogger(UsuariDAO.class);

    public static Usuari trobaOCreaUsuariPerNom(String nickname) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuari usuari = null;
        try {
            tx = session.beginTransaction();
            // Intenta trobar un usuari existent amb el nom donat
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE nom = :nom", Usuari.class);
            query.setParameter("nickname", nickname);
            usuari = query.uniqueResult();
            // Si no es troba, crea un nou usuari
            if (usuari == null) {
                // usuari = new Usuari(nickname);
                session.save(usuari);
                tx.commit();
                logger.info("Nou usuari creat amb el nom: {}", nickname);
            } else {
                logger.info("Usuari ja existent amb el nom: {}", nickname);
            }
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al crear o trobar l'usuari", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static Usuari crearUsuari(String nickname, int telefon, String email, String contrasenya) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuari usuari = null;
        try {
            tx = session.beginTransaction();
            usuari = new Usuari(nickname, telefon, email, null, contrasenya, false, false, "Free", null);
            session.save(usuari);
            tx.commit();
            logger.info("Nou usuari creat amb el nom: {}", nickname);
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
                usuari = null;
            logger.error("Error al crear l'usuari", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static void actualitzaUsuari(Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.update(usuari);
            tx.commit();
            logger.info("Usuari actualitzat amb èxit: {}", usuari.getNickname());
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al actualitzar l'usuari", e);
        } finally {
            session.close();
        }
    }

    public static void eliminaUsuari(Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            session.delete(usuari);
            tx.commit();
            logger.info("Usuari eliminat amb èxit: {}", usuari.getNickname());
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al eliminar l'usuari", e);
        } finally {
            session.close();
        }
    }

    public static Usuari getUsuariPerId(Long userId) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            usuari = session.get(Usuari.class, userId);
        } catch (HibernateException e) {
            logger.error("Error al trobar l'usuari per id", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static Usuari getUsuariPerEmail(String email) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE email = :email", Usuari.class);
            query.setParameter("email", email);
            usuari = query.uniqueResult();
        } catch (HibernateException e) {
            logger.error("Error al trobar l'usuari per email", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static Usuari getUsuariPerCodiValidacio(String codiValidacio) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE codiValidacio = :codiValidacio", Usuari.class);
            query.setParameter("codiValidacio", codiValidacio);
            usuari = query.uniqueResult();
        } catch (HibernateException e) {
            logger.error("Error al trobar l'usuari per codi de validació", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static Usuari getUsuariPerEmailICodiValidacio(String email, String codiValidacio) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE email = :email AND codiValidacio = :codiValidacio", Usuari.class);
            query.setParameter("email", email);
            query.setParameter("codiValidacio", codiValidacio);
            usuari = query.uniqueResult();
        } catch (HibernateException e) {
            logger.error("Error al trobar l'usuari per email i codi de validació", e);
        } finally {
            session.close();
        }
        return usuari;
    }


    public static boolean validarUsuari(Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            usuari.setValidat(true);
            session.update(usuari);
            tx.commit();
            logger.info("Usuari validat amb èxit: {}", usuari.getNickname());
            return true;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al validar l'usuari", e);
            return false;
        } finally {
            session.close();
        }
    }


}
