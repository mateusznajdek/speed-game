package pl.edu.agh.ki.speedgame.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.EncodedResourceResolver;
import org.springframework.web.servlet.resource.PathResourceResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/tasks/", "classpath:/static/js/").setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/tasks/", "classpath:/static/css/").setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/tasks/", "classpath:/static/img/").setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/tasks/", "classpath:/static/lib/").setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());

        registry.addResourceHandler("/tasks/**/js/**").addResourceLocations(listCompo("js")).setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/tasks/**/css/**").addResourceLocations(listCompo("css")).setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/tasks/**/img/**").addResourceLocations(listCompo("img")).setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
        registry.addResourceHandler("/tasks/**/lib/**").addResourceLocations(listCompo("lib")).setCachePeriod(0).resourceChain(false).addResolver(new EncodedResourceResolver()).addResolver(new PathResourceResolver());
    }

    private String[] listCompo(String suffix) {
        ArrayList<String> res = new ArrayList<>(Arrays.asList("classpath:/tasks/", "classpath:/static/" + suffix + "/"));
        try {
            if (ResourceUtils.getFile("classpath:tasks").isDirectory() && ResourceUtils.getFile("classpath:tasks").listFiles() != null) {
                res.addAll(
                        Arrays.stream(
                                ResourceUtils.getFile("classpath:tasks").listFiles())
                                .filter(File::isDirectory)
                                .map(file -> "classpath:/tasks/" + file.getName() + "/" + suffix)
                                .collect(Collectors.toList())
                );
            }

        } catch (FileNotFoundException ignored) {
        }
        return res.toArray(new String[]{});
    }

    @Bean
    public ClassLoaderTemplateResolver yourTemplateResolver() {
        ClassLoaderTemplateResolver yourTemplateResolver = new ClassLoaderTemplateResolver();
        yourTemplateResolver.setPrefix("tasks/");
        yourTemplateResolver.setSuffix(".html");
        yourTemplateResolver.setCacheTTLMs(0L);
        yourTemplateResolver.setCacheable(false);
        yourTemplateResolver.setTemplateMode(TemplateMode.HTML);
        yourTemplateResolver.setCharacterEncoding("UTF-8");
        yourTemplateResolver.setOrder(0);  // this is important. This way spring boot will listen to both places 0 and 1
        yourTemplateResolver.setCheckExistence(true);

        return yourTemplateResolver;
    }
}