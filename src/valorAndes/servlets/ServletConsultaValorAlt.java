package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.PortafolioValue;

public class ServletConsultaValorAlt extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	public String darTituloPagina(HttpServletRequest request) {
		return "ValorPortafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();
		
		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Valores en Portafolios</h2> ");
		out.println("				Ingrese el identificador de un valor para averiguar en que portafolios ha estado involucrado.<hr>");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Id Valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"text\" class=\"form-control\" id=\"idValor\" name=\"idValor\" required>");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("");
		out.println("						</div>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Buscar\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");

		String idval = request.getParameter("idValor");
		if(idval!=null){

			try {
				escribirResultados(out, ValorAndes.getInstance().consultarValorAlt(idval));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("	</div> ");
	}
	
	/**
	 * Se muestran estos resultados si el tipo de usuario a consultar es Oferente
	 */
	public void escribirResultados(PrintWriter out, ArrayList<PortafolioValue> ports){

		if(ports.isEmpty()){
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">El valor no ha participado en ningun portafolio</div>");
		}

		else{
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">Resultados</div>");
			out.println("					<table class=\"table table-striped\">");
			out.println("						<thead>");
			out.println("							<tr>");
			out.println("								<th>Nombre</th>");
			out.println("								<th>Nivel de Riesgo</th>");
			out.println("								<th>Intermediario Asociado</th>");
			out.println("							</tr>");
			out.println("						</thead>");
			out.println("						<tbody>");

			for(int i=0; i<ports.size();i++){
				PortafolioValue porta = ports.get(i);

				out.println("							<tr><form>");
				out.println("								<td>" + porta.getNombre() + "</td>");
				out.println("								<td>" + porta.getTipoRiesgo() + "</td>");
				out.println("								<td>" + porta.getCorreoInter() + "</td>");
				out.println("							</tr></form>");
			}
			out.println("						</tbody>");
			out.println("					</table>");
		}

		out.println("			</div>");
		out.println("		</div> ");
	}

}
