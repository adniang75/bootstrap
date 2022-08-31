package rsb.bootstrap.templates;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import rsb.bootstrap.DataSourceUtils;
import rsb.bootstrap.Demo;

import javax.sql.DataSource;

public class Application {

    public static void main(String[] args) {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        DataSource initializedDataSource = DataSourceUtils.initializedDdl(dataSource);
        PlatformTransactionManager dsTxManager = new DataSourceTransactionManager(initializedDataSource);
        TransactionTemplate transactionTemplate = new TransactionTemplate(dsTxManager);
        TransactionTemplateCustomerService customerService = new TransactionTemplateCustomerService(
                initializedDataSource, transactionTemplate);
        Demo.workWithCustomerService(Application.class, customerService);
    }

}
