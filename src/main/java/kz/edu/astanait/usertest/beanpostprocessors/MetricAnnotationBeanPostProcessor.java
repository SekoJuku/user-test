package kz.edu.astanait.usertest.beanpostprocessors;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import kz.edu.astanait.usertest.annotation.Metric;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@AllArgsConstructor
public class MetricAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final MeterRegistry meterRegistry;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> aClass = bean.getClass();
        if(aClass.getInterfaces().length == 0) {
            return Enhancer.create(aClass, (InvocationHandler) this::getInvocationHandler);
        } else {
            return Proxy.newProxyInstance(aClass.getClassLoader(), aClass.getInterfaces(), this::getInvocationHandler);
        }
    }

    @SneakyThrows
    private Object getInvocationHandler(Object proxy, Method method, Object[] args) {
        if(method.isAnnotationPresent(Metric.class)) {
            Counter counter = meterRegistry.counter(method.getAnnotation(Metric.class).name());
            counter.increment();
        }
        return method.invoke(proxy, args);
    }
}
