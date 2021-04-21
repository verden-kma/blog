package edu.ukma.blog;

import edu.ukma.blog.models.IModelsPackage;
import edu.ukma.blog.repositories.relational_repos.IRelationalReposPackage;
import edu.ukma.blog.security.models.LoginResponse;
import edu.ukma.blog.security.repositories.LoggedOutUserRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableJpaRepositories(basePackageClasses = {IRelationalReposPackage.class, LoggedOutUserRepo.class})
@EntityScan(basePackageClasses = {IModelsPackage.class, LoginResponse.class})
@SpringBootApplication
public class BlogApplication {
	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}
}