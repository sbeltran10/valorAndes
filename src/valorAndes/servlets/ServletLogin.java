package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

public class ServletLogin extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	/**
	 * Titulo de la pagina
	 */
	public String darTituloPagina(HttpServletRequest request) {
		return "Login";
	}

	/**
	 * Escribre el contenido de la pagina dependiendo de los procesos.
	 */
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		int vaLog = ValorAndes.getInstance().getLogeado();

		String login = request.getParameter("login");

		if(login==null){

			if(vaLog==0){
				out.println("<div class=\"container\">");
				out.println("		<br><br><br>");
				out.println("    <div class=\"row colored\">");
				out.println("        <div id=\"contentdiv\" class=\"contcustom\">");
				out.println("            <span class=\"fa fa-spinner bigicon\"></span>");
				out.println("            <h2>Login</h2>");
				out.println("            <div><form method=\"post\">");
				out.println("                <input id=\"username\" type=\"text\" placeholder=\"e-mail\" name=\"login\">");
				out.println("                <input id=\"password\" type=\"password\" placeholder=\"clave\" name=\"pass\">");
				out.println("				<button type=\"submit\" class=\"btn btn-default\" >Login</button>");
				out.println("				</form>");
				out.println("            </div>");
				out.println("        </div>");
				out.println("    </div>");
				out.println("</div>");

			}

			else{
				out.println("	<div class=\"well well-lg\">");
				out.println("		<div class=\"container\">");
				out.println("		<br><br><br>");
				out.println("			<div class=\"panel panel-info\">");
				out.println("				<div class=\"panel-heading\">Usted ya se encuentra logeado en el sistema ");
				out.println("				(Ir al <a href=\"portal.htm\">portal de servicios.</a>)</div>");
				out.println("				</div>");
				out.println("		</div>");
				out.println("	</div>");
				out.println("</div>");
			}
		}

		else{

			String pass = request.getParameter("pass");

			if(ValorAndes.getInstance().adminLogin(login, pass))
				escribirSuccessAdmin(out);

			else{

				try{
					boolean log = ValorAndes.getInstance().login(login, pass);
					if(log)
						escribirSuccess(out);
					else
						escribirFail(out);
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}

		out.println("");
		escribirEstilo(out);
	}

	/**
	 * Informa al usuario que el login fue exitoso
	 * @param out
	 */
	public void escribirSuccess(PrintWriter out){

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("			<div class=\"panel panel-info\">");
		out.println("				<div class=\"panel-heading\">Ha ingresado exitosamente en la pagina, ahora puede hacer uso de nuestro ");
		out.println("				<a href=\"portal.htm\">portal de servicios.</a>)</div>");
		out.println("				</div>");
		out.println("		</div>");
		out.println("	</div>");
		out.println("</div>");
	}

	/**
	 * Informa al admin que el login fue exitoso
	 * @param out
	 */
	public void escribirSuccessAdmin(PrintWriter out){

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("			<div class=\"panel panel-info\">");
		out.println("				<div class=\"panel-heading\">Bienvenido Admin, ahora puede hacer uso del");
		out.println("				<a href=\"portal.htm\">portal de servicios.</a>)</div>");
		out.println("				</div>");
		out.println("		</div>");
		out.println("	</div>");
		out.println("</div>");
	}

	/**
	 * Informa al usuario que el login fracaso
	 * @param out
	 */
	public void escribirFail(PrintWriter out){

		out.println("<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("    <div class=\"row colored\">");
		out.println("        <div id=\"contentdiv\" class=\"contcustom\">");
		out.println("            <span class=\"fa fa-spinner bigicon\"></span>");
		out.println("            <h2>Login</h2>");
		out.println("            <div><form>");
		out.println("            <font color=\"red\"> El correo y/o clave son incorrectos, intentelo de nuevo </font>");	
		out.println("                <input id=\"username\" type=\"text\" placeholder=\"e-mail\" name=\"login\">");
		out.println("                <input id=\"password\" type=\"password\" placeholder=\"clave\" name=\"pass\">");
		out.println("				<button type=\"submit\" class=\"btn btn-default\" >Login</button>");
		out.println("				</form>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("    </div>");
		out.println("</div>");
	}

	/**
	 * El estilo de la pagina
	 */
	public void escribirEstilo(PrintWriter out){
		out.println("<style>");
		out.println(".colored {");
		out.println("    background-color: #F0EEEE;");
		out.println("}");
		out.println("");
		out.println(".row {");
		out.println("    padding: 20px 0px;");
		out.println("}");
		out.println("");
		out.println(".bigicon {");
		out.println("    font-size: 97px;");
		out.println("    color: #0080FF;");
		out.println("}");
		out.println("");
		out.println(".contcustom {");
		out.println("    text-align: center;");
		out.println("    width: 300px;");
		out.println("    border-radius: 0.5rem;");
		out.println("    top: 0;");
		out.println("    bottom: 0;");
		out.println("    left: 0;");
		out.println("    right: 0;");
		out.println("    margin: 10px auto;");
		out.println("    background-color: white;");
		out.println("    padding: 20px;");
		out.println("}");
		out.println("");
		out.println("input {");
		out.println("    width: 100%;");
		out.println("    margin-bottom: 17px;");
		out.println("    padding: 15px;");
		out.println("    background-color: #ECF4F4;");
		out.println("    border-radius: 2px;");
		out.println("    border: none;");
		out.println("}");
		out.println("");
		out.println("h2 {");
		out.println("    margin-bottom: 20px;");
		out.println("    font-weight: bold;");
		out.println("    color: #ABABAB;");
		out.println("}");
		out.println("");
		out.println("</style>");
	}
}
