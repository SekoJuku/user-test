package kz.edu.astanait.usertest.beanpostprocessors;

import kz.edu.astanait.usertest.annotation.Metric;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.SpringApplication;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MetricAnnotationBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        for (Method method : aClass.getMethods()) {
            if (method.isAnnotationPresent(Metric.class)) {
                Metric annotation = method.getAnnotation(Metric.class);
                String name = annotation.name();
            }
        }

        return bean;
    }
}
