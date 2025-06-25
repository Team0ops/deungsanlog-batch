package com.deungsanlog.batch.job.mountain.sun.reader;

import com.deungsanlog.batch.job.mountain.sun.dto.MountainSunRow;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

@Configuration
public class MountainSunReader {

    @Bean
    public JdbcCursorItemReader<MountainSunRow> mountainSunItemReader(DataSource dataSource) {
        JdbcCursorItemReader<MountainSunRow> reader = new JdbcCursorItemReader<>();
        reader.setDataSource(dataSource);
        reader.setSql("SELECT id AS mountain_id, latitude, longitude FROM mountains\n");
        reader.setRowMapper(new RowMapper<>() {
            @Override
            public MountainSunRow mapRow(ResultSet rs, int rowNum) throws SQLException {
                return new MountainSunRow(
                        rs.getLong("mountain_id"),
                        rs.getDouble("latitude"),
                        rs.getDouble("longitude")
                );
            }
        });
        return reader;
    }
}