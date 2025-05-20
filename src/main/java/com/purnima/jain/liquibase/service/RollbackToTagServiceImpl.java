package com.purnima.jain.liquibase.service;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.purnima.jain.liquibase.exception.MissingTagException;

import liquibase.Liquibase;
import liquibase.database.DatabaseConnection;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

@Service
public class RollbackToTagServiceImpl implements RollbackService {

	@Autowired
	@Qualifier("postgresDataSource")
	private DataSource postgresDataSource;

	public void rollbackToTag(String tagToRollBackTo) {

		try (Connection connection = postgresDataSource.getConnection();
				DatabaseConnection liquibaseConnection = new JdbcConnection(connection);
				Liquibase liquibase = new Liquibase("db/changelog/db-changelog-master.xml", new ClassLoaderResourceAccessor(), liquibaseConnection)) {
			if (liquibase.tagExists(tagToRollBackTo)) {
				liquibase.rollback(tagToRollBackTo, "");
			} else {
				throw new MissingTagException("Tag " + tagToRollBackTo + " does not exist. Please provide correct tag.");
			}
		} catch (LiquibaseException | SQLException e) {
			throw new RuntimeException("Failed to run liquibase", e);
		}

	}

}
