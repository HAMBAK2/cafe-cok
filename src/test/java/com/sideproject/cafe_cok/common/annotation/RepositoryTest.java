package com.sideproject.cafe_cok.common.annotation;


import com.sideproject.cafe_cok.global.config.JpaConfig;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@Import(JpaConfig.class)
@ActiveProfiles("test")
public abstract class RepositoryTest {
}
