package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Import(SchoolUniService.class)
public class SchoolUniServiceTest extends BaseDbUnitTest {

    @Resource
    private SchoolUniService schoolUniService;


    @Test
    public void testGetData() throws Exception {
        List<Map<String, Object>>  response=schoolUniService.getData2();

//        List<Map<String, Object>> item
//       // System.out.println(reponse.getInstitutionList());
//        response.forEach( item -> System.out.println(item));
//        assertNotNull(response);
    }

    @Test
    public void testSaveData() throws Exception {
        schoolUniService.saveData();
    }

}
