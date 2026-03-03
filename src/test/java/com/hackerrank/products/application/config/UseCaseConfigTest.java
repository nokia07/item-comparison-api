package com.hackerrank.products.application.config;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.util.ReflectionTestUtils;

import com.hackerrank.products.application.domain.model.gateway.ProductRepository;
import com.hackerrank.products.application.domain.usecase.ProductUseCase;

class UseCaseConfigTest {

    @Test
    void classShouldHaveConfigurationAnnotation() {
        Configuration annotation = UseCaseConfig.class.getAnnotation(Configuration.class);

        assertNotNull(annotation);
    }

    @Test
    void productUseCaseMethodShouldHaveBeanAnnotation() throws NoSuchMethodException {
        Method method = UseCaseConfig.class.getDeclaredMethod("productUseCase", ProductRepository.class);
        Bean beanAnnotation = method.getAnnotation(Bean.class);

        assertNotNull(beanAnnotation);
    }

    @Test
    void productUseCaseShouldReturnNewInstanceWithInjectedRepository() {
        UseCaseConfig config = new UseCaseConfig();
        ProductRepository mockRepository = mock(ProductRepository.class);

        ProductUseCase result = config.productUseCase(mockRepository);

        assertNotNull(result);
    }

    @Test
    void productUseCaseShouldInjectRepositoryCorrectly() {
        UseCaseConfig config = new UseCaseConfig();
        ProductRepository mockRepository = mock(ProductRepository.class);

        ProductUseCase result = config.productUseCase(mockRepository);

        assertNotNull(result);
        Object injectedRepository = ReflectionTestUtils.getField(result, "productRepository");
        assertNotNull(injectedRepository);
        assertSame(mockRepository, injectedRepository);
    }

    @Test
    void productUseCaseMethodShouldReturnProductUseCaseType() throws NoSuchMethodException {
        Method method = UseCaseConfig.class.getDeclaredMethod("productUseCase", ProductRepository.class);

        assertEquals(ProductUseCase.class, method.getReturnType());
    }
}
