package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

/**
 * Clase abstracta servlet a partir de la cual se construyen las demas 
 * clases servlet que representan las paginas de la aplicacion web.
 */
public abstract class ServletTemplate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//Constructor
	public ServletTemplate() {
		super();
	}


	//Metodos Implementados

	/**
	 * Recibe la solicitud y la herramienta de respuesta a las solicitudes
	 * hechas por los metodos get. Invoca el metodo procesarPedido.
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepcion de error al escribir la respuesta
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		procesarPedido(request, response);
	}

	/**
	 * Recibe la solicitud y la herramienta de respuesta a las solicitudes
	 * hechas por los metodos post. Invoca el metodo procesarPedido.
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepcion de error al escribir la respuesta
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		procesarPedido(request, response);
	}

	/**
	 * Procesa el pedido de igual manera para todos
	 * @param request Pedido del cliente
	 * @param response Respuesta del servlet
	 * @throws IOException Excepcion de error al escribir la respuesta
	 */
	private void procesarPedido( HttpServletRequest request, HttpServletResponse response ) throws IOException {
		//TODO Si hay otras fachadas, ellas tambien deben inicializar la ruta.

		ValorAndes.getInstance().inicializarRuta("C:/Software/jboss-4.2.2.GA/server/default/data/valorAndes");
		// Comienza con el Header del template
		imprimirHeader( request, response );
		//
		// Escribe el contenido de la pagina
		escribirContenido( request, response );
		//
		// Termina con el footer del template
		imprimirFooter( response );

	}

	/**
	 * Escribe la cabecera de la pagina web
	 * @param request pedido del cliente
	 * @param response respuesta del servlet
	 * @throws IOException Excepcion de error al recibir la respuesta
	 */
	private void imprimirHeader(HttpServletRequest request,HttpServletResponse response) throws IOException{

		PrintWriter out = response.getWriter();

		int vaLog = ValorAndes.getInstance().getLogeado();

		out.println("<!DOCTYPE html>");
		out.println("<html lang=\"en\">");
		out.println("");
		out.println("<head>");
		out.println("");
		out.println("    <meta charset=\"utf-8\">");
		out.println("    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">");
		out.println("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">");
		out.println("");
		out.println("    <title>ValorAndes - " + darTituloPagina(request) + "</title>");
		out.println("");
		out.println("    <link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">");
		out.println("");
		out.println("    <link href=\"css/landing-page.css\" rel=\"stylesheet\">");
		out.println("");
		out.println("    <link href=\"font-awesome-4.1.0/css/font-awesome.min.css\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("    <link href=\"http://fonts.googleapis.com/css?family=Lato:300,400,700,300italic,400italic,700italic\" rel=\"stylesheet\" type=\"text/css\">");
		out.println("	");
		out.println("    <nav class=\"navbar navbar-inverse navbar-fixed-top\" role=\"navigation\">");
		out.println("        <div class=\"container\">");
		out.println("		");
		out.println("            <div class=\"navbar-header\">");
		out.println("                <a class=\"navbar-brand\" href=\"landing.htm\">Valores de los Andes</a>");
		out.println("            </div>");
		out.println("");
		if(vaLog!=0){
			out.println("            <div class=\"collapse navbar-collapse\" id=\"bs-example-navbar-collapse-1\">");
			out.println("                <ul class=\"nav navbar-nav navbar-right\">");
			out.println("                    <li>");
			out.println("                        <a href=\"logout.htm\">Logout</a>");
			out.println("                    </li>");
			out.println("                    <li>");
			out.println("                        <a href=\"portal.htm\">Servicios</a>");
			out.println("                    </li>");
			out.println("                </ul>");
			out.println("            </div>");
		}
		out.println("        </div>");
		out.println("    </nav>");
		out.println("</head>");
		out.println("");
		out.println("<body>");
		out.println("");
		out.println("");

	}

	/**
	 * Escribe el footer de la pagina web
	 * @param response respuesta del servlet
	 * @throws IOException Excepcion de error al recibir la respuesta
	 */
	private void imprimirFooter(HttpServletResponse response) throws IOException{

		PrintWriter out = response.getWriter();

		out.println("<footer>");
		out.println("        <div class=\"navbar navbar-fixed-bottom\">");
		out.println("        <div class=\"container\">");
		out.println("            <div class=\"row\">");
		out.println("                <div class=\"col-lg-12\">");
		out.println("                    <p class=\"copyright text-muted small\">Copyright &copy; ValorAndes 2014. Todos los derechos reservados</p>");
		out.println("                </div>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("		</div>");
		out.println("    </footer>");
		out.println("");
		out.println("    <script src=\"js/jquery-1.11.0.js\"></script>");
		out.println("");
		out.println("    <script src=\"js/bootstrap.min.js\"></script>");
		out.println("	<script src=\"jquery.select-to-autocomplete.js\"></script>");
		out.println("");
		out.println("</body>");
		out.println("");
		out.println("</html>");
	}

	//Metodos abstractos

	/**
	 * Devuelve el titulo de la pagina
	 * @param request Pedido del cliente
	 * @return Titulo de la pagina
	 */
	public abstract String darTituloPagina( HttpServletRequest request );

	/**
	 * Escribe el contenido de la pagina
	 * @param request Pedido del cliente
	 * @param response Respuesta
	 * @throws IOException Excepcion de error al escribir la respuesta
	 */
	public abstract void escribirContenido( HttpServletRequest request, HttpServletResponse response ) throws IOException;

}
