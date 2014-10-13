package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

public class ServletLogout extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		
		return "Logout";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("			<div class=\"panel panel-info\">");
		out.println("				<div class=\"panel-heading\">Se ha salido del sistema exitosamente");
		out.println("				(Volver a la <a href=\"landing.htm\">pagina principal.</a>)</div>");
		out.println("				</div>");
		out.println("		</div>");
		out.println("	</div>");
		out.println("</div>");
		
		ValorAndes.getInstance().logout();
		
	}

}
