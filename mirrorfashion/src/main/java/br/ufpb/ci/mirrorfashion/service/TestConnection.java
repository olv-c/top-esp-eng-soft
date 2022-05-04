package br.ufpb.ci.mirrorfashion.service;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConnection {

	public static void main(String[] args) {
		Connection c;
		try {
			c = DriverManager.getConnection("jdbc:hsqldb:file:./main/webapp/db/simplehr", "sa", ""); //"jdbc:hsqldb:file:../src/main/webapp/db/simplehr"
			System.out.println("Conectou");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
