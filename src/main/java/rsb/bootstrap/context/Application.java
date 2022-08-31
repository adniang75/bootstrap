package rsb.bootstrap.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import rsb.bootstrap.CustomerService;
import rsb.bootstrap.DataSourceConfiguration;
import rsb.bootstrap.Demo;
import rsb.bootstrap.SpringUtils;
import rsb.bootstrap.templates.TransactionTemplateCustomerService;

import javax.sql.DataSource;

@Configuration
@Import(DataSourceConfiguration.class)
public class Application {

    public static void main(String[] args) {
        ApplicationContext context = SpringUtils.run(Application.class, "prod");
        CustomerService cs = context.getBean(CustomerService.class);
        Demo.workWithCustomerService(Application.class, cs);
    }

    @Bean
    PlatformTransactionManager transactionManager(DataSource ds) {
        return new DataSourceTransactionManager(ds);
    }

    @Bean
    TransactionTemplateCustomerService customerService(DataSource ds, TransactionTemplate transactionTemplate) {
        return new TransactionTemplateCustomerService(ds, transactionTemplate);
    }

    @Bean
    TransactionTemplate transactionTemplate(PlatformTransactionManager tm) {
        return new TransactionTemplate(tm);
    }

}
