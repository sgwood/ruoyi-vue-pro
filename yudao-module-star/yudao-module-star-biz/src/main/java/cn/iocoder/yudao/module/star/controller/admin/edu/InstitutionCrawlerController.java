package cn.iocoder.yudao.module.star.controller.admin.edu;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import cn.iocoder.yudao.module.star.service.edu.InstitutionCrawlerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/star/data")
@Validated
    @RestController
    public class InstitutionCrawlerController {
        private final InstitutionCrawlerService institutionCrawlerService;

        public InstitutionCrawlerController(InstitutionCrawlerService institutionCrawlerService) {
            this.institutionCrawlerService = institutionCrawlerService;
        }

        @GetMapping("/institutions")
        public InstitutionListResponse getInstitutions() {
            return institutionCrawlerService.crawlInstitutions();
        }
    @PostMapping("/save")
        public CommonResult<Boolean> save() {
            institutionCrawlerService.crawlAndSaveInstitutions();
            return CommonResult.success(true);
        }
    }

