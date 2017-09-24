package com.springMVC.shop.config;

import com.springMVC.shop.dao.AccountDAO;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.springMVC.shop.dao.OrderDAO;
import com.springMVC.shop.dao.ProductDAO;
import com.springMVC.shop.impl.AccountDAOImpl;
import com.springMVC.shop.impl.OrderDAOImpl;
import com.springMVC.shop.impl.ProductDAOImpl;

import javax.activation.DataSource;

import java.util.Properties;

@Configuration
@ComponentScan("com.springMVC.shop.*")
@EnableTransactionManagement
// Load environment
@PropertySource("classpath:ds-hibernate-cfg.properties")
public class ApplicationContextConfig {

    // environment class serves as the property holder
    // and stores all the properties loaded by the @PropertySource
    @Autowired
    private Environment env;

    @Bean
    public ResourceBundleMessageSource messageSource(){
        ResourceBundleMessageSource rb = new ResourceBundleMessageSource();
        // load property in message/validator.properties
        rb.setBasenames(new String[] {"messages/validator"});
        return rb;
    }

    @Bean(name = "viewResolver")
    public InternalResourceViewResolver getViewResolver(){
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    // config for upload
    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver(){
        CommonsMultipartResolver commonsMultipartResolver =
                new CommonsMultipartResolver();

        // setMax Size...
//        commonsMultipartResolver.setMaxUploadSize(200);
        return commonsMultipartResolver;
    }

    @Bean(name = "dataSource")
    public DataSource getDataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        // see: ds-hibernate-cfg.properties
        dataSource.setDriverClassName(env.getProperty("ds.database-driver"));
        dataSource.setUrl(env.getProperty("ds.url"));
        dataSource.setUsername(env.getProperty("ds.username"));
        dataSource.setPassword(env.getProperty("ds.password"));

        System.out.println("## getDataSource : " + dataSource);
        return (DataSource) dataSource;
    }

    @Autowired
    @Bean(name = "sessionFactory")
    public SessionFactory getSessionFactory(DataSource dataSource) throws Exception{
        Properties properties = new Properties();

        // see: ds-hibernate-cfg.properties
        properties.put("hibernate.dialect", env.getProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        properties.put("current_session_context_class", env.getProperty("current_session_context_class"));

        org.springframework.orm.hibernate4.LocalSessionFactoryBean factoryBean =
                new org.springframework.orm.hibernate4.LocalSessionFactoryBean();

        // Package contain entity classes
        factoryBean.setPackagesToScan(new String[]{"com.springMVC.shop.entity"});
        factoryBean.setDataSource((javax.sql.DataSource) dataSource);
        factoryBean.setHibernateProperties(properties);
        factoryBean.afterPropertiesSet();
        //
        SessionFactory sf = factoryBean.getObject();
        System.out.println("## getSessionFactory : " + sf);
        return sf;
    }

    @Autowired
    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager(SessionFactory sessionFactory){
        HibernateTransactionManager transactionManager = new HibernateTransactionManager(sessionFactory);
        return transactionManager;
    }

    @Bean(name = "accountDAO")
    public AccountDAO getApplicantDAO() {
        return new AccountDAOImpl();
    }

    @Bean(name = "productDAO")
    public ProductDAO getProducDAO(){
        return new ProductDAOImpl();
    }

    @Bean(name = "orderDAO")
    public OrderDAO getOrderDAO(){
        return new OrderDAOImpl();
    }

    @Bean(name = "accountDAO")
    public AccountDAO getAccountDAO(){
        return new AccountDAOImpl();
    }
}
