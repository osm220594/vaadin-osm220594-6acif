package com.teppichcenter;

import com.teppichcenter.services.TeppichRepository;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
@NpmPackage(value = "@fontsource/cairo", version = "4.5.0")
@Theme(value = "teppich-center")
public class Application implements AppShellConfigurator {

    @Autowired
    private TeppichRepository teppichRepository;
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
