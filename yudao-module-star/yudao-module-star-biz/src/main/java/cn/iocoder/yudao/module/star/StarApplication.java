package cn.iocoder.yudao.module.star;

import cn.iocoder.yudao.module.star.dal.mysql.intl.SchoolIntlEntity;
import cn.iocoder.yudao.module.star.dal.mysql.intl.SchoolIntlRepository;
import cn.iocoder.yudao.module.star.service.edu.intl.IntlDetailToDBService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class StarApplication implements CommandLineRunner {

    @Autowired
    private IntlDetailToDBService intlDetailToDBService;

    @Autowired
    private SchoolIntlRepository schoolIntlRepository;

    public static void main(String[] args) {
        SpringApplication.run(StarApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // 获取第一个学校 id
        Optional<SchoolIntlEntity> firstSchool = null;//schoolIntlRepository.findById(100001L);
        if (firstSchool!=null && firstSchool.isPresent()) {
            Long schoolId = firstSchool.get().getId();
            intlDetailToDBService.updateSchoolIntlFromApi(schoolId);
        } else {
            System.out.println("未从 school_intl 表中找到 id。");
        }
    }
}
