package edu.ukma.blog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@SpringBootApplication
//@EnableJpaRepositories(basePackageClasses = IUsersRepo.class)
@EnableJpaRepositories(basePackages = "edu.ukma.blog.repositories")
@EntityScan("edu.ukma.blog.models")

@EnableGlobalMethodSecurity(prePostEnabled = true)

public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
    }


}
















/* todo:
 * 1. Особисто я б не використовував int чи long в якості ключа. Зараз це уже не дуже прийнято так як має певні обмеження.
 *  рекомендую використовувати Guid (uid), він гарантує вам унікальність ключа, може бути згенерований як в коді, так і в БД.
 *  З мінусів - менша читабельність і трохи більший розмір ніж інт.
 * 2. Особисто я б також не використовував композитні ключі, вони можуть викликати проблеми в випадку коли доведеться розширяти
 *  функціонал. Я б використовував Guid + ForeignKey. При цьому ForeignKey може бути як явною зв'язкою по БД,
 *  так і логічною на рівні коду. З явною в БД є плюси в тому, що відбувається автоматичний контроль цілісності даних \
 * (при операціях додавання, оновлення, видалення), тобто ви не зможете створити коментар від користувача, який не існує.
 * з мінусів - той самий - цілісність даних при розростанні структури може викликати проблеми, так як можливості логіки
 * моделей в коді куди більші ніж в БД.
 * 3. Не знаю що відбувається в класі шифрування паролю, але почитайте бестпрактісес по зберіганню паролів
 * */
