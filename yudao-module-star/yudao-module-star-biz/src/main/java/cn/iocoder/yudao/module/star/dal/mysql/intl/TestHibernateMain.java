package cn.iocoder.yudao.module.star.dal.mysql.intl;

public class TestHibernateMain {
    public static void main(String[] args) {
        SchoolIntlEntity entity = new SchoolIntlEntity();
        entity.setStudentCapacity(1000);
        entity.setGraduatedStuNum(200);
        entity.setStuDominantNationality("China");

        SchoolIntlHibernateDao dao = new SchoolIntlHibernateDao();
        dao.save(entity);
    }
}
