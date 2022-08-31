package rsb.bootstrap.scan;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import rsb.bootstrap.templates.TransactionTemplateCustomerService;

import javax.sql.DataSource;

@Service
public class DiscoveredCustomerService extends TransactionTemplateCustomerService {

    DiscoveredCustomerService(DataSource dataSource, TransactionTemplate transactionTemplate) {
        super(dataSource, transactionTemplate);
    }

}
