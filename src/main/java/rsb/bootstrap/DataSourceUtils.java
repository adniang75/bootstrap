package rsb.bootstrap;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

public abstract class DataSourceUtils {

    private DataSourceUtils() {
    }

    public static DataSource initializedDdl(DataSource dataSource) {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator(
                new ClassPathResource("/schema.sql"));
        DatabasePopulatorUtils.execute(populator, dataSource);
        return dataSource;
    }

}
