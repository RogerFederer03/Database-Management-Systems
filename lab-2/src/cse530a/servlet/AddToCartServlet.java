package cse530a.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import cse530a.dao.DatabaseManager;
import cse530a.dao.ShoppingCartDao;
import cse530a.model.ShoppingCart;
import cse530a.model.User;

/**
 * Servlet implementation class AddToCartServlet
 */
@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());

	@Resource(name = "jdbc/postgres")
	private DataSource dataSource;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddToCartServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        process(request, response);
	}
    
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		LOGGER.info("servlet /addToCart");
		
        String isbn = request.getParameter("isbn");
	    LOGGER.info("Got isbn = " + isbn );
	    
		User user = (User) request.getSession().getAttribute("user");
		if (user == null) {
			// not properly logged in
			request.getRequestDispatcher("WEB-INF/jsp/Login.jsp").forward(request, response);
			return;
		}
        LOGGER.info("User " + user.getUsername() + " add shopping items from carts");
		DatabaseManager dbm = new DatabaseManager(dataSource);
		Connection conn = dbm.getConnection();
		try{
			// add shopping items from carts
			ShoppingCartDao.updateCart(conn, user.getId(), isbn, "updateorcreate");
			ShoppingCart shoppingCart = ShoppingCartDao.retrieveCart(conn, user.getId());
			request.setAttribute("cart", shoppingCart);
			
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "error add shopping items from shopping cart", e);
        } finally {
            DatabaseManager.closeConnection(conn);
        } 
        request.getRequestDispatcher("WEB-INF/jsp/ShoppingCart.jsp").forward(request, response);
    }
}
