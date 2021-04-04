import entity.Item;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;


public class Main {
    private static final SessionFactory ourSessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ourSessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }



    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    public static void main(final String[] args) throws Exception {
        final Session session = getSession();
        try {
            System.out.println("querying all the managed entities...");
            final Metamodel metamodel = session.getSessionFactory().getMetamodel();
            for (EntityType<?> entityType : metamodel.getEntities()) {
                final String entityName = entityType.getName();
                final Query query = session.createQuery("from " + entityName);
                System.out.println("executing: " + query.getQueryString());
                for (Object o : query.list()) {
                    Item item = (Item) o;
                    System.out.println("  " + item.getName());
                }
            }
            Transaction transaction = session.beginTransaction();
            Item item1 = new Item();
            item1.setName("A1");
            Item item2 = new Item();
            item2.setName("A2");
            Item item3 = new Item();
            item3.setName("A3");
            session.saveOrUpdate(item1);
            session.saveOrUpdate(item2);
            session.saveOrUpdate(item3);
            transaction.commit();
        } finally {
            session.close();
            ourSessionFactory.close();
        }
    }
}