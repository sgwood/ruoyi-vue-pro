package cn.iocoder.yudao.module.star.controller.admin.edu;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.star.dal.dataobject.edu.SchoolListResponse;
import cn.iocoder.yudao.module.star.service.edu.SchoolIntlService;
import cn.iocoder.yudao.module.star.service.edu.SchoolUniService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("/star/school/uni")
@Validated
    @RestController
    public class SchoolUniController {
        private final SchoolUniService schoolUniService;

        public SchoolUniController(SchoolUniService schoolUniService) {
            this.schoolUniService = schoolUniService;
        }

        @GetMapping("/list")
        public List<Map<String, Object>> getData() throws Exception {
            return schoolUniService.getData2();
        }
    @PostMapping("/save")
        public CommonResult<Boolean> saveData() throws Exception {
        schoolUniService.saveData();
            return CommonResult.success(true);
        }
    }

