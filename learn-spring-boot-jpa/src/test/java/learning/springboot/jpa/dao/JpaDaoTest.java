package learning.springboot.jpa.dao;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.annotation.Commit;

import learning.springboot.jpa.entity.Dept;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaDaoTest {

	@Resource
	Dao dao;

	@Configuration
	@EntityScan(basePackageClasses = Dept.class)
	@ImportAutoConfiguration(HibernateJpaAutoConfiguration.class)
	static class Config {
		@Bean
		Dao dao() {
			return new JpaDao();
		}
	}

	@Test
	@Order(1)
	@Transactional
	void testGet() {
		Dept dept = this.dao.get(Dept.class, 1);
		log.info("========== testGet: dept.name={}", dept.getName());
		Assertions.assertTrue(dept.getId() == 1);
	}

	@Test
	@Order(2)
	@Commit
	@Transactional
	void testUpdate() {
		Dept dept = this.dao.get(Dept.class, 1);
		dept.setName("部门1_" + new Date());
		this.dao.update(dept);
		this.dao.flush();
		log.info("========== testUpdate: dept.id={}", dept.getId());
	}

	@Test
	@Order(3)
	@Commit
	@Transactional
	void testDelete() {
		Dept dept = this.dao.get(Dept.class, 2);
		log.info("========== testDelete: dept.name={}", dept.getName());
		this.dao.delete(dept);
		this.dao.flush();
	}

	@Test
	@Order(4)
	@Commit
	@Transactional
	void testSave() {
		Dept dept = new Dept();
		dept.setName("部门3");
		this.dao.save(dept);
		log.info("========== testSave: dept.id={}", dept.getId());
		Assertions.assertTrue(dept.getId() > 0);
	}

}
