package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;

public class ServletConsultaMovimiento extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	public String darTituloPagina(HttpServletRequest request) {
		return "ConsultaMovimiento";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();


		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Movimientos</h2> ");
		out.println("				Seleccione un rango de tiempo dentro del cual desea consultar movimientos sobre valores.<hr>");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Fecha Inicial</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaIni\" name=\"fechaIni\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Fecha Final</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaFin\" name=\"fechaFin\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"text\" class=\"form-control\" id=\"val\" name=\"val\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Tipo de Valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoVal\">");
		out.println("									  <option>---</option>");
		try {
			escribirOpcionesVal( out, ValorAndes.getInstance().darTiposVal());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label=>Tipo de Rentabilidad</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoRent\" required>");
		out.println("									  <option>---</option>");
		
		try {
			escribirOpcionesRent( out, ValorAndes.getInstance().darTiposRent());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label=>Tipo de movimiento</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoMov\" >");
		out.println("										<option></option>");
		out.println("										<option>Compra</option>");
		out.println("										<option>Venta</option>");
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Correo Oferente</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"ofer\" name=\"ofer\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Correo Inversionista</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"inver\" name=\"inver\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Correo Intermediario</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"inter\" name=\"inter\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Buscar\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");

		String fechaIni = request.getParameter("fechaIni");
		
		String fechaFin = request.getParameter("fechaFin");
		
		if(fechaIni!=null){

			String ofer = request.getParameter("ofer");
			if(ofer == "")
				ofer="---";

			String inter = request.getParameter("inver");
			if(inter == "")
				inter="---";

			String inver = request.getParameter("inter");
			if(inver == "")
				inver="---";


		}
		out.println("	</div> ");
	}

	//----------------------------------------------------------
	// Rellenar Formularios
	//----------------------------------------------------------
	
	/**
	 * Escribe las opciones de rentabilidad existentes en el sistema
	 * @param out
	 * @param rentList
	 */
	public void escribirOpcionesRent(PrintWriter out, ArrayList<String> rentList){
		for(int i =0; i< rentList.size(); i++){
			String opcion = rentList.get(i);
			out.println("					  <option>" +  opcion + "</option>");
		}
	}
	
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
	public void escribirResultados(PrintWriter out, ArrayList<String[]> usus, String tipoUs){

		if(usus.isEmpty()){
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">No se encontraron Usuarios</div>");
		}

		else{
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">Resultados</div>");
			out.println("					<table class=\"table table-striped\">");
			out.println("						<thead>");
			out.println("							<tr>");
			out.println("								<th>Correo</th>");
			out.println("								<th>Nombre</th>");
			out.println("								<th>Telefono</th>");
			out.println("								<th>Pais</th>");
			out.println("								<th>Ciudad</th>	");
			out.println("								<th>Id Representante</th>	");
			out.println("								<th>Ver informacion</th>	");
			out.println("							</tr>");
			out.println("						</thead>");
			out.println("						<tbody>");

			for(int i=0; i<usus.size();i++){
				String[] us = usus.get(i);

				out.println("							<tr><form>");
				out.println("								<td>" + us[0] + "</td>");
				out.println("								<td>" + us[1] + "</td>");
				out.println("								<td>" + us[2] + "</td>");
				out.println("								<td>" + us[3] + "</td>");
				out.println("								<td>" + us[4] + "</td>");
				out.println("								<td>" + us[5] + "</td>");
				out.println("								<input type=\"hidden\" name=\"opid\" value=\"" + us[0] + "-" + tipoUs + "\"/>");
				out.println("								<td><button type=\"submit2\"  class=\"btn btn-default\" name=\"us\">Ver informacion</button></td>");
				out.println("							</tr></form>");
			}

			out.println("						</tbody>");
			out.println("					</table>");
		}

		out.println("			</div>");
		out.println("		</div> ");
	}

	
}