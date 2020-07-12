package ru.job4j.carsale.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.carsale.models.*;

import java.util.List;
import java.util.function.Function;

public class PsqlStore implements Store {

    private final SessionFactory sf;

    private PsqlStore() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        this.sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
    }

    private static final class Lazy {
        private static final Store INST = new PsqlStore();
    }

    public static Store instOf() {
        return Lazy.INST;
    }

    @Override
    public Integer createAdvertisement(Advertisement advertisement) {
        return tx(session -> {
            session.save(advertisement);
            return advertisement.getId();
        });
    }

    @Override
    public void updateAdvertisement(Advertisement advertisement) {
        tx(session -> {
            session.update(advertisement);
            return advertisement.getId();
        });
    }

    @Override
    public List<Status> findAllStatus() {
        return tx(session -> session.createQuery("from ru.job4j.carsale.models.Status").list());
    }

    @Override
    public List<Advertisement> findAllAdvertisements() {
        return tx(session -> session.createQuery("from ru.job4j.carsale.models.Advertisement").list());
    }

    @Override
    public List<Brand> findAllBrands() {
        return tx(session -> session.createQuery("from ru.job4j.carsale.models.Brand").list());
    }

    @Override
    public List<Model> findModelByBrandId(Integer brandId) {
        return tx(session -> {
            Query<Model> query = session.createQuery("select m from Model m inner join Brand b on m.brand.id = b.id where b.id = :brandId");
            query.setParameter("brandId", brandId);
            return query.list();
        });
    }

    @Override
    public Advertisement findAdvertisementById(Integer advertisementId) {
        return tx(session -> session.get(Advertisement.class, advertisementId));
    }

    @Override
    public Integer createUser(User user) {
        return tx(session -> {
            try {
                session.save(user);
                return user.getId();
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        });
    }

    @Override
    public List<User> findUserByLogin(String login) {
        return tx(session -> {
            Query<User> query = session.createQuery("from User u where u.login = :login");
            query.setParameter("login", login);
            return query.list();
        });
    }

    private <T> T tx(final Function<Session, T> command) {
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
