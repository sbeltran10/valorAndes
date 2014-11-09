package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.PortafolioValue;

public class ServletConsultaPortafolio extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	public String darTituloPagina(HttpServletRequest request) {
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Portafolios</h2> ");
		out.println("				Elija el tipo de valor y el valor de operaciones sobre las cuales desea consultar un portafolio<hr>");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-3\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Tipo de Valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoVal\" required>");
		out.println("									  <option></option>");
		try {
			escribirOpcionesVal( out, ValorAndes.getInstance().darTiposVal());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Operaciones con valor mayor a: </label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"number\" class=\"form-control\" id=\"valop\" name=\"valop\" required >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Buscar\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");

		String tipoVal = request.getParameter("tipoVal");
		if(tipoVal!=null){
			int cant = Integer.parseInt(request.getParameter("valop"));
			
			try {
				escribirResultados(out, ValorAndes.getInstance().consultarPortafolios(tipoVal, cant));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		out.println("	</div> ");
	}

	//----------------------------------------------------------
	// Rellenar Formularios
	//----------------------------------------------------------
	
	/**
	 * Escribe las opciones de valores existentes en la base de datos
	 * @param out
	 * @param vaList
	 */
	public void escribirOpcionesVal(PrintWriter out, ArrayList<String> vaList){
		for(int i =0; i< vaList.size(); i++){
			String opcion = vaList.get(i);
			out.println("					  <option>" +  opcion + "</option>");
		}
	}
	
	//----------------------------------------------------------
	// Tabla de resultados
	//----------------------------------------------------------
	
	
	/**
	 * Se muestran estos resultados si el tipo de usuario a consultar es Oferente
	 */
	public void escribirResultados(PrintWriter out, ArrayList<PortafolioValue> porta){

		if(porta.isEmpty()){
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">No se encontraron portafolios</div>");
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

			for(int i=0; i<porta.size();i++){
				PortafolioValue port = porta.get(i);

				out.println("							<tr><form>");
				out.println("								<td>" + port.getNombre() + "</td>");
				out.println("								<td>" + port.getTipoRiesgo() + "</td>");
				out.println("								<td>" + port.getCorreoInter() + "</td>");
				out.println("							</tr></form>");
			}

			out.println("						</tbody>");
			out.println("					</table>");
		}

		out.println("			</div>");
		out.println("		</div> ");
	}

}
