package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

/**
 * Servlet que representa la pagina principal de la aplicacion web
 */
public class ServletLanding extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {

		return "Main";
	}

	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		int vaLog = ValorAndes.getInstance().getLogeado();

		out.println("    <div class=\"intro-header\">");
		out.println("        <div class=\"container\">");
		out.println("            <div class=\"row\">");
		out.println("                <div class=\"col-lg-12\">");
		out.println("                    <div class=\"intro-message\">");
		out.println("                        <h1>Valores de los Andes</h1>");
		out.println("                        <h3>Bolsa de Valores</h3>");
		out.println("                        <hr class=\"intro-divider\">");
		if(vaLog!=0){
			out.println("                            <li>");
			out.println("                                <a href=\"portal.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Portal de Servicios</span></a>");
			out.println("                            </li>");
		}
		else{
			out.println("                            <li>");
			out.println("                                <a href=\"registroUsuario.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Registrar Nueva Cuenta</span></a>");
			out.println("                            </li> <br>");
			out.println("                            <li>");
			out.println("                                <a href=\"login.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Login</span></a>");
			out.println("                            </li>");
		}
		out.println("                    </div>");
		out.println("                </div>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("    </div>");
		out.println("");
	}

}
