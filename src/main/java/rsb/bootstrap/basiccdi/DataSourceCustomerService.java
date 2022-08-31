package rsb.bootstrap.basiccdi;

import rsb.bootstrap.BaseCustomerService;

import javax.sql.DataSource;

public class DataSourceCustomerService extends BaseCustomerService {

    DataSourceCustomerService(DataSource ds) {
        super(ds);
    }

}
