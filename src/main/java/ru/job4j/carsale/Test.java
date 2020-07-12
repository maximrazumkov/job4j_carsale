package ru.job4j.carsale;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.carsale.models.Model;

import java.util.List;
import java.util.function.Function;

public class Test {
    public static void main(String[] args) {
        try (
                final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
                final SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
                final Session session = sf.openSession();
        ) {
            Integer brandId = 5;
            session.beginTransaction();
            List<Model> query = tx(s -> {
                Query<Model> q = s.createQuery("select m from Model m inner join Brand b on m.brand.id = b.id where b.id = :brandId");
                q.setParameter("brandId", brandId);
                return q.list();
            }, sf);
            System.out.println(query);
        }  catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static <T> T tx(final Function<Session, T> command, SessionFactory sf) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
