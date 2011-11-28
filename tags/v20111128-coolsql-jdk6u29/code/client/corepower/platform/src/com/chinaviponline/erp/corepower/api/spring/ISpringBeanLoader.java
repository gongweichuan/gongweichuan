package com.chinaviponline.erp.corepower.api.spring;
/**
 * Spring的Adapter
 */
import org.springframework.core.io.Resource;
import org.springframework.context.ApplicationContext;
public interface ISpringBeanLoader
{
   public static final String ROLE = ISpringBeanLoader.class.getName();
    
   public static String ALLSPRINGBEAN="-springbean.xml";
   public Resource getResource(String resourceName);
   public Object getBean(String beanName);
   public ApplicationContext getApplicationContext(); 
}
