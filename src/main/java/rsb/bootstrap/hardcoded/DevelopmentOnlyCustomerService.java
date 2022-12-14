package rsb.bootstrap.hardcoded;

import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import rsb.bootstrap.BaseCustomerService;
import rsb.bootstrap.DataSourceUtils;

import javax.sql.DataSource;

/**
 * ⚠️ Do not do this
 */
public class DevelopmentOnlyCustomerService extends BaseCustomerService {

    DevelopmentOnlyCustomerService() {
        super(buildDataSource());
    }

    private static DataSource buildDataSource() {
        DataSource dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .build();
        return DataSourceUtils.initializedDdl(dataSource);
    }

}
