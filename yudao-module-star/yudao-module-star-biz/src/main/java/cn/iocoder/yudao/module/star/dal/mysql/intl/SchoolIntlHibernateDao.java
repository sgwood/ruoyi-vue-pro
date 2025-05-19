package cn.iocoder.yudao.module.star.dal.mysql.intl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SchoolIntlHibernateDao {
    private SessionFactory sessionFactory;

    public SchoolIntlHibernateDao() {
        sessionFactory = new Configuration().configure().buildSessionFactory();
    }

    public void save(SchoolIntlEntity entity) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
