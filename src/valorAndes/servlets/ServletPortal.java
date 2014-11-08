package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

/**
 * Servlet que representa el portal a traves del cual se accede a los servicios de la aplicacion web.
 */

public class ServletPortal extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "Portal";
	}

	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		int vaLog = ValorAndes.getInstance().getLogeado();

		out.println("	<div class=\"well well-lg\">");
		out.println("	<div class=\"container\">");
		out.println("");
		out.println("		<br><br><br>");
		out.println("");
		out.println("		<h1>Portal de Servicios<hr></h1>");
		out.println("		<h2>Consultas</h2>");
		out.println("");
		out.println("		<li>");
		out.println("			<a href=\"consultaVal.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consulta de Valores</span></a>");
		out.println("		</li> <br>");
		out.println("		<li>");
		out.println("			<a href=\"consultaOp.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consulta de Operaciones por Usuario</span></a>");
		out.println("		</li><br>");
		out.println("		<li>");
		out.println("			<a href=\"consultaMov.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consulta de Movimientos de Valores</span></a>");
		out.println("		</li><br>");
		out.println("		<li>");
		out.println("			<a href=\"consultaPorta.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consulta de Portafolios</span></a>");
		out.println("		</li><br>");
		out.println("		<li>");
		out.println("			<a href=\"consultaValAlt.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consulta de Valores en Portafolios</span></a>");
		out.println("		</li><br>");

		if(vaLog==1 || vaLog==4){
			out.println("		<li>");
			out.println("			<a href=\"consultaUsu.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Consultar Usuarios</span></a>");
			out.println("		</li> <br>");
		}
		out.println("		<hr>");
		out.println("");
		
		
		if(vaLog!=4){
			out.println("		<h2>");
			out.println("		Operaciones Bursatiles");
			out.println("		</h2>");
			out.println("");

			if(vaLog==1){
				out.println("		<li>");
				out.println("			<a href=\"operacionRegistrar.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Registrar Operacion Bursatil</span></a>");
				out.println("		</li> <br>");
			}
			else{
				out.println("		<li>");
				out.println("			<a href=\"operacionOrdenCompra.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Compra de Valores</span></a>");
				out.println("		</li> <br>");
				out.println("		<li>");
				out.println("			<a href=\"operacionOrdenVenta.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Venta de Valores</span></a>");
				out.println("		</li> <br>");
				out.println("		<li>");
				out.println("			<a href=\"operacionCancelar.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Cancelar Operacion bursatil</span></a>");
				out.println("		</li> <br>");
			}
		}
		else{
			out.println("		<h2>");
			out.println("		Usuarios");
			out.println("		</h2>");
			out.println("");
			out.println("		<li>");
			out.println("			<a href=\"retirarInter.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Retirar Intermediarios</span></a>");
			out.println("		</li> <br>");
		}
		
		if(vaLog==1){
			out.println("		<h2>");
			out.println("		Portafolios");
			out.println("		</h2>");
			out.println("");
			out.println("		<li>");
			out.println("			<a href=\"portafolioInter.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Administrar Portafolios</span></a>");
			out.println("		</li> <br>");
		}
		else if(vaLog==2){
			out.println("		<h2>");
			out.println("		Portafolios");
			out.println("		</h2>");
			out.println("");
			out.println("		<li>");
			out.println("			<a href=\"portafolioInver.htm\" class=\"btn btn-default btn-lg\"><span class=\"network-name\">Administrar Portafolios</span></a>");
			out.println("		</li> <br>");
		}
		out.println("");
		out.println("	</div>");
		out.println("	</div>");
		out.println("");

	}

}
