package cn.iocoder.yudao.module.star.controller.admin.edu;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.InstitutionListResponse;
import cn.iocoder.yudao.module.star.service.edu.InstitutionCrawlerService;
import cn.iocoder.yudao.module.star.service.edu.SchoolIntlService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/star/school/intl")
@Validated
    @RestController
    public class SchoolIntlController {
        private final SchoolIntlService schoolIntlService;

        public SchoolIntlController(SchoolIntlService schoolIntlService) {
            this.schoolIntlService = schoolIntlService;
        }

        @GetMapping("/get")
        public InstitutionListResponse getInstitutions() {
            return schoolIntlService.getData();
        }
    @PostMapping("/save")
        public CommonResult<Boolean> save() {
        schoolIntlService.saveData();
            return CommonResult.success(true);
        }
    }

