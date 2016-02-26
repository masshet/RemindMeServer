package by.mrstark.remindme.server.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("by.mrstark.remindme.server.repository")
@EnableTransactionManagement
@PropertySource("classpath:db.properties")
@ComponentScan("by.mrstark.remindme.server")
public class DatabaseConfig {

    @Resource
    private Environment env;
    private Properties hibernateProperties;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean bean = new LocalContainerEntityManagerFactoryBean();
        bean.setDataSource(dataSource());
        bean.setPackagesToScan(env.getRequiredProperty("db.entitypakage"));
        bean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        bean.setJpaProperties(getHibernateProperties());
        return bean;
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource source = new BasicDataSource();
        source.setUrl(env.getRequiredProperty("db.url"));
        source.setDriverClassName(env.getRequiredProperty("db.driver"));
        source.setUsername(env.getRequiredProperty("db.username"));
        source.setPassword(env.getRequiredProperty("db.password"));

        source.setInitialSize(Integer.parseInt(env.getRequiredProperty("db.intialSize")));
        source.setMinIdle(Integer.parseInt(env.getRequiredProperty("db.minIdle")));
        source.setMaxIdle(Integer.parseInt(env.getRequiredProperty("db.maxIdle")));
        source.setTimeBetweenEvictionRunsMillis(Long.parseLong(env.getRequiredProperty("db.timeBweenEvictionRunsMillis")));
        source.setMinEvictableIdleTimeMillis(Long.parseLong(env.getRequiredProperty("db.minEvictabIdleTimeMillis")));
        source.setTestOnBorrow(Boolean.parseBoolean(env.getRequiredProperty("db.testOnBorrow")));
        source.setValidationQuery(env.getRequiredProperty("db.validationQuery"));
        return source;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return manager;
    }

    public Properties getHibernateProperties() {
        try {
            Properties properties = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.properties");
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("Can`t find hibernate.properties", e);
        }
    }
}
