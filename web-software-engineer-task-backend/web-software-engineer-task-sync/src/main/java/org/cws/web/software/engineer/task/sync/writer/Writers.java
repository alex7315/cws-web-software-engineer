package org.cws.web.software.engineer.task.sync.writer;

import javax.sql.DataSource;

import org.cws.web.software.engineer.task.persistence.model.GithubUser;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import jakarta.persistence.EntityManagerFactory;

@Configuration
public class Writers {

    @Bean
    JpaItemWriter<GithubUser> userWriter(EntityManagerFactory usersEntityManagementFactory) {
        //@formatter:off
        return new JpaItemWriterBuilder<GithubUser>()
                .entityManagerFactory(usersEntityManagementFactory)
                .usePersist(false)
                .build();
         //@formatter:on
    }

    @Bean
    JdbcBatchItemWriter<Long> deletionUserWriter(DataSource usersDataSource) {
        //@formatter:off
        return new JdbcBatchItemWriterBuilder<Long>()
                .dataSource(usersDataSource)
                .sql("delete from github_user where github_id = :githubId")
                .itemSqlParameterSourceProvider(item -> new MapSqlParameterSource().addValue("githubId", item))
                .assertUpdates(false)
                .build();
        //@formatter:on
    }
}
