package edu.ukma.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses = IUsersRepo.class)
@EnableJpaRepositories(basePackages = {"edu.ukma.blog.repositories", "edu.ukma.blog.security.repositories"})
@EntityScan({"edu.ukma.blog.models", "edu.ukma.blog.security.models"})
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class BlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
    }
}