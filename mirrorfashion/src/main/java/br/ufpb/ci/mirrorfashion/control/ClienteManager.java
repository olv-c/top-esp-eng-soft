package br.ufpb.ci.mirrorfashion.control;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import br.ufpb.ci.mirrorfashion.model.Cliente;
import org.hsqldb.lib.tar.DbBackup;

public class ClienteManager {

private static List<Cliente> lista = new ArrayList<>();

	private Connection c;
	private PreparedStatement stmt;

	public ClienteManager(Connection c) {
			this.c = c;
	}

	public boolean cadastrar(Cliente cliente) {
		ResultSet rs = null;
	try {
		lista.add(cliente);

		String NEXT_ID = "SELECT MAX(EMP_ID)+1 AS NEXT_ID FROM employee";

		int next_id = 0;
		if(lista.size() == 1){
			next_id = 1;
		} else {
			next_id = next_id(NEXT_ID);
		}

		String status = cliente.getStatus().equals("ativo")? "A": "I";

		String INSERT = "INSERT INTO employee (EMP_ID, EMP_NAME, EMP_PASSWORD, EMP_STATUS) values (?,?,?,?)";

		//rs = statement(INSERT);
		PreparedStatement stmt = c.prepareStatement(INSERT);
		stmt.setString(1, String.valueOf(next_id));
		stmt.setString(2, cliente.getLogin());
		stmt.setString(3, cliente.getSenha());
		stmt.setString(4, status);

		stmt.execute();
		stmt.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	return true;
	}

	public List<Cliente> getTodosclientes(){
		String QUERY = "SELECT * FROM employee";
		Cliente aux;
		
        ResultSet rs = statement(QUERY);
		try {
			while(rs.next()){
				//Display values
				aux = new Cliente();
				aux.setLogin(rs.getString("EMP_NAME"));
				lista.add(aux);

				System.out.println(", Name: "  + aux.getLogin() );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}


		return lista;
	}
	
	public void excluir(int indice) {
		lista.remove(indice);
		try {
			PreparedStatement statement = c.prepareStatement("delete from employee where id=?");
			statement.setInt(1,indice);
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public List<Cliente> buscar(String st){
		List<Cliente> sts = new ArrayList<>();
		for (Cliente c : lista) {
			if (c.getLogin().equals(st)) {
				sts.add(c);
			}
		}
		return sts;
	}
	
	public String editar(Cliente cliente) {
		String GET_ID = "SELECT * FROM EMPLOYEE WHERE EMP_NAME = " + cliente.getLogin();
		ResultSet rs = null;
		rs = statement(GET_ID);
		try {
			String QUERY = "UPDATE employee set EMP_NAME = "
					+ cliente.getLogin() +
					", EMP_PASSWORD = "
					+ cliente.getSenha() +
					", EMP_STATUS = " + cliente.getStatus() +
					"WHERE EMP_ID = " + rs.getString("EMP_ID");

			statement(QUERY);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "Cliente Modificado";
	}

	private ResultSet statement(String query) {
		ResultSet rs = null;
		try {
			stmt = this.c.prepareStatement(query);

			rs = stmt.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	private int next_id(String query) throws SQLException {
		ResultSet rs = statement(query);
		if(rs == null){
			return 1;
		}

		return Integer.parseInt(rs.getString("NEXT_ID"));
	}
}

