package cse530a.servlet;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import cse530a.dao.BookDao;
import cse530a.dao.ShoppingCartDao;
import cse530a.dao.UserDao;
import cse530a.model.ShoppingCart;
import cse530a.model.ShoppingCartItem;
import cse530a.model.User;

/**
 * Servlet implementation class AddToCartServlet
 */
@WebServlet("/addToCart")
public class AddToCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    private static final Logger LOGGER = Logger.getLogger(AddToCartServlet.class.getName());

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

        SessionFactory sessionFactory = (SessionFactory) request.getServletContext().getAttribute("hibernateSessionFactory");
        Session session = sessionFactory.openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            Long userId = (Long) request.getSession().getAttribute("userId");
            if (userId == null) {
                // not properly logged in
                request.getRequestDispatcher("WEB-INF/jsp/Login.jsp").forward(request, response);
                return;
            }

            User user = UserDao.retrieveUser(session, userId);
            if (user == null) {
                // not properly logged in
                request.getRequestDispatcher("WEB-INF/jsp/Login.jsp").forward(request, response);
                return;
            }
            // get the isbn from the web
            String isbn = request.getParameter("isbn");
            // flag to indicate wheather the book is found
            boolean bookFound = false;
			// Retrieve the shopping cart
			ShoppingCart shoppingCart = user.getShoppingCart();
			for(ShoppingCartItem element : shoppingCart.getItems()) {
				// check if there are same books in the cart
				if(element.getBook().getIsbn().equals(isbn)){
					bookFound = true;
					// update the quantity of the books
					ShoppingCartDao.updateItem(session, element);
				} 
			}
			// if we did not find the books in the cart, we create the books
			if(!bookFound) {
				ShoppingCartDao.createItem(session, shoppingCart, BookDao.retrieveBook(session, isbn), 1);
			}
			
			// update the time modification stamp for shopping cart
			ShoppingCartDao.updateCart(session, shoppingCart);
			
			request.setAttribute("cart", shoppingCart);
            
            request.getRequestDispatcher("WEB-INF/jsp/ShoppingCart.jsp").forward(request, response);
        
            tx.commit();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "error retrieving cart", e);
            request.getRequestDispatcher("WEB-INF/jsp/Login.jsp").forward(request, response);
            tx.rollback();
        } finally {
            session.close();
        }
    }
}
