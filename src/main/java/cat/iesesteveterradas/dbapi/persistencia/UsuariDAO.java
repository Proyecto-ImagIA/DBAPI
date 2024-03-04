package cat.iesesteveterradas.dbapi.persistencia;

import java.util.List;
import java.util.UUID;

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

    public static Usuari crearUsuari(String nickname, String telefon, String email, String contrasenya) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        Usuari usuari = null;
        try {
            tx = session.beginTransaction();
            Query query = session.createQuery("FROM Usuari WHERE nickname = :nickname OR telefon = :telefon OR email = :email");
            query.setParameter("nickname", nickname);
            query.setParameter("telefon", telefon);
            query.setParameter("email", email);
            List<Usuari> existingUsers = query.list();
            
            if (!existingUsers.isEmpty()) {
                // Ya existe un usuario con el mismo nickname, teléfono o email
                logger.error("Ya existe un usuario con el mismo nickname, teléfono o email");
                return null;
            }

            usuari = new Usuari(nickname, telefon, email, null, contrasenya, false, false, "Free", null, null);
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
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE codi_validacio = :codi_validacio", Usuari.class);
            query.setParameter("codi_validacio", codiValidacio);
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
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE email = :email AND codi_validacio = :codi_validacio", Usuari.class);
            query.setParameter("email", email);
            query.setParameter("codi_validacio", codiValidacio);
            usuari = query.uniqueResult();
        } catch (HibernateException e) {
            logger.error("Error al trobar l'usuari per email i codi de validació", e);
        } finally {
            session.close();
        }
        return usuari;
    }


    public static String validarUsuari(Usuari usuari) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            String apiKey = UUID.randomUUID().toString();
            usuari.setValidat(true);
            usuari.setApiKey(apiKey);
            session.update(usuari);
            tx.commit();
            logger.info("Usuari validat amb èxit: {}", usuari.getApiKey());
            return apiKey;
        } catch (HibernateException e) {
            if (tx != null)
                tx.rollback();
            logger.error("Error al validar l'usuari", e);
            return null;
        } finally {
            session.close();
        }
    }

    public static Usuari getUsuariPerTelefon(String telefon) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE telefon = :telefon", Usuari.class);
            query.setParameter("telefon", telefon);
            usuari = query.uniqueResult();
        } catch (HibernateException e) {
            usuari = null;
            logger.error("Error al trobar l'usuari per telèfon", e);
        } finally {
            session.close();
        }
        return usuari;
    }

    public static Usuari getUsuariPerApiKey(String apiKey) {
        Session session = SessionFactoryManager.getSessionFactory().openSession();
        Usuari usuari = null;
        try {
            Query<Usuari> query = session.createQuery("FROM Usuari WHERE api_key = ':apikey'", Usuari.class);
            System.out.println(apiKey);
            System.out.println(query.getQueryString());
            query.setParameter("apikey", apiKey);

            usuari = query.uniqueResult();
            System.out.println(usuari.getNickname());
        } catch (HibernateException e) {
            usuari = null;
            logger.error("Error al trobar l'usuari per api key", e);
        } finally {
            session.close();
        }
        return usuari;
    }


}
