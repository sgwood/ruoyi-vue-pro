package cn.iocoder.yudao.module.star.framework.web.config;

import cn.iocoder.yudao.framework.swagger.config.YudaoSwaggerAutoConfiguration;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Star 模块的 web 组件的 Configuration
 *
 * @author sgwood
 */
@Configuration(proxyBeanMethods = false)
public class StarWebConfiguration {

    /**
     * star 模块的 API 分组
     */
    @Bean
    public GroupedOpenApi starGroupedOpenApi() {
        return YudaoSwaggerAutoConfiguration.buildGroupedOpenApi("star");
    }

}
