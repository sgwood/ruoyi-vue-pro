package cn.iocoder.yudao.module.star.controller.admin.edu;

import cn.iocoder.yudao.module.star.service.edu.NicheScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/star/niche")
public class NicheScraperController {

    @Autowired
    private NicheScraperService nicheScraperService;

    @GetMapping("/scrape")
    public String startScraping() {
        nicheScraperService.startScraping();
        return "Scraping process started.";
    }
}
