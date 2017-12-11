package pl.ms.akkatest.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static final Logger LOG = LoggerFactory.getLogger(ApplicationContextProvider.class);

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        ApplicationContextProvider.applicationContext = applicationContext;
    }

    public static <T> T getManagedBean(final String beanName, final Class<T> classs) {
        return applicationContext.getBean(beanName, classs);
    }

    public static <T> T getManagedBean(final Class<T> classs) {
        return applicationContext.getBean(classs);
    }
}
