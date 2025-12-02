package cn.iocoder.yudao.module.star.service.edu;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.ArrayUtils;
import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;
import java.util.List;
import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.buildTime;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertPojoEquals;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.assertServiceException;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@Import(InstitutionCrawlerService.class)
public class DataServiceImplTest extends BaseDbUnitTest {

    @Resource
    private InstitutionCrawlerService institutionCrawlerService;


    @Test
    public void testGetData() {
        InstitutionListResponse reponse=institutionCrawlerService.crawlInstitutions();
       // System.out.println(reponse.getInstitutionList());
        reponse.getInstitutionList().forEach( institution -> System.out.println(institution.getName()));
        assertNotNull(reponse);
    }

    @Test
    public void testCrawlAndSaveInstitutions() {
        institutionCrawlerService.crawlAndSaveInstitutions();
    }

}
