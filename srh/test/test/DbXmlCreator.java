package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.ext.postgresql.PostgresqlDataTypeFactory;

public class DbXmlCreator {

	private String pathXML;

	private IDatabaseConnection connection;

    @SuppressWarnings({ "unused"})
	public DbXmlCreator(String schema) {
		try {

			// database connection
			@SuppressWarnings("rawtypes")
			Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
	        Connection jdbcConnection = DriverManager.getConnection(
	        		"jdbc:oracle:thin:@10.1.1.8:1521:orcl", "SRH", "SRH");

	        if (schema != null)
	        	connection = new DatabaseConnection(jdbcConnection, schema);
	        else
	        	connection = new DatabaseConnection(jdbcConnection);
	        DatabaseConfig databaseConfig = connection.getConfig();
	        databaseConfig.setProperty(DatabaseConfig.PROPERTY_DATATYPE_FACTORY, new PostgresqlDataTypeFactory());

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DatabaseUnitException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Criar arquivo XML com todas as tabelas de uma determinado schema
	 * @param path
	 */
	public void criarXMLSchema(){
		try {
	        IDataSet fullDataSet = connection.createDataSet();
	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream(this.pathXML));
			System.out.println("log: XML com todas as info criadas com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (DataSetException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	public void setPathXML(String pathXML) {
		this.pathXML = pathXML;
	}

	public static void main(String[] args) throws Exception {
		DbXmlCreator xmlCreator = new DbXmlCreator("SRH");
		xmlCreator.setPathXML("fullDataBase.xml");
		xmlCreator.criarXMLSchema();
	}

}
