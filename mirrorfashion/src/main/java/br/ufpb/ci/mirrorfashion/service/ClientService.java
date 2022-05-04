package br.ufpb.ci.mirrorfashion.service;

import br.ufpb.ci.mirrorfashion.control.ClienteManager;
import br.ufpb.ci.mirrorfashion.model.Cliente;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ClientService
 */
@WebServlet("/client")
public class ClientService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ClienteManager clienteManager;
	
	private Connection c;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClientService() {
        super();
        System.out.println();
    }

	@Override
	public void init() throws ServletException {
		try {
			Class.forName("org.hsqldb.jdbcDriver");
			c = DriverManager.getConnection("jdbc:hsqldb:file:/main/webapp/db/simplehr", "sa", "");
			
			clienteManager = new ClienteManager(c);
			System.out.println("Schema.: " + c.getSchema());
			super.init();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		response.getWriter().append("Lista de clientes: ").append(request.getContextPath());
		
		response.getWriter().append(clienteManager.getTodosclientes().toString());

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String login = request.getParameter("login");
		String senha = request.getParameter("senha");
		String statusCad = request.getParameter("statusCad");
		
		Cliente cli = new Cliente("","","");
		cli.setLogin(login);
		cli.setSenha(senha);
		cli.setStatus(statusCad);

		clienteManager.cadastrar(cli);
		request.setAttribute("msg", "Cadastrado Realizado com Sucesso!");
		request.setAttribute("lista", clienteManager.getTodosclientes());

		RequestDispatcher dispatcher = request.getRequestDispatcher("cliente.jsp");

		dispatcher.forward(request, response);
	}
	
	@Override
	public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Cliente cliente = clienteManager.buscar(req.getParameter("login")).get(0);
		cliente.setLogin(req.getParameter("login"));
		cliente.setSenha(req.getParameter("senha"));
		cliente.setStatus(req.getParameter("statusCad"));

		resp.getWriter().print(clienteManager.editar(cliente));
		RequestDispatcher dispatcher = req.getRequestDispatcher("cliente.jsp");

		dispatcher.forward(req, resp);
	}


	@Override
	public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int id = Integer.parseInt(req.getParameter("id"));
		clienteManager.excluir(id);


		RequestDispatcher dispatcher = req.getRequestDispatcher("cliente.jsp");
		req.setAttribute("msg", "Cliente deletado com sucesso!");
		req.setAttribute("lista", clienteManager.getTodosclientes());

		dispatcher.forward(req, resp);
	}

	public void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		RequestDispatcher dispatcher = request.getRequestDispatcher("cliente.jsp");
		request.setAttribute("lista", clienteManager.buscar(request.getParameter("login")));
		request.setAttribute("msg", "Busca realizada com sucesso");

		dispatcher.forward(request, response);
	}



}
