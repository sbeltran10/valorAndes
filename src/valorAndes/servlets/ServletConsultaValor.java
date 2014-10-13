package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.ValorValue;

/**
 * Servlet que representa el servicio de consultas de valores de la palicacion web.
 */

public class ServletConsultaValor extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "ConsultaValor";
	}

	/**
	 * Escribe el contenido de la pagina
	 */
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Valores</h2> ");
		out.println("					A continuacion seleccione los filtros por los cuales desea hacer la consulta<hr>");
		out.println("");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-3\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Nombre del valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"text\" class=\"form-control\" id=\"nombre\" name=\"nombre\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Tipo de Valor</label>");
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
		out.println("							</div>		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Tipo de Rentabilidad</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoRent\">");
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
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Estado</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"estado\">");
		out.println("									  <option>---</option>");
		out.println("										<option>Disponible</option>");
		out.println("										<option>No Disponible</option>");
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Fecha de Expiracion</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaExp\" name=\"fechaExp\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Correo Oferente</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"idOfer\" name=\"idOfer\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Correo Inversionista</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"idInver\" name=\"idInver\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Correo Intermediario</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"idInter\" name=\"idInter\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Ordenar por:</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"orden\">");
		out.println("								  <option>---</option>");
		out.println("								  <option>Nombre Valor </option>");
		out.println("								  <option>Tipo del Valor</option>");
		out.println("									<option>Tipo de Rentabilidad</option>");
		out.println("									<option>Estado</option>");
		out.println("								  <option>Fecha de Expiracion</option>");
		out.println("											  <option>Mercado</option>");
		out.println("					 					 <option>Precio</option>");
		out.println("									  <option>Creador</option>");
		out.println("									</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Buscar\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");
		out.println("			");
		out.println("			<div class=\"container\">");
		out.println("			");
		out.println("				<div class=\"panel panel-info\">");

		String fech = request.getParameter("fechaExp");
		if(fech!=null){

			String dat = null;
			if(!fech.isEmpty()){

				String[] arFechas1 = fech.split("-");
				dat = arFechas1[2] + "-" + arFechas1[1] +  "-" + arFechas1[0];
			}

			String nomb = request.getParameter("nombre");
			if(nomb == "")
				nomb="---";

			String corOfer = request.getParameter("idOfer");
			if(corOfer == "")
				corOfer="---";

			String corInte = request.getParameter("idInte");
			if(corInte == "")
				corInte="---";

			String corInver = request.getParameter("idInver");
			if(corInver == "")
				corInver="---";
			
			String order = request.getParameter("orden");
			
			if(order.equals("Nombre Valor"))
				order = "VALOR.nombre";
			
			if(order.equals("Tipo del Valor"))
				order = "TIPO_VALOR.nombre";
			
			if(order.equals("Tipo de Rentabilidad"))
				order = "RENTABILIDAD.nombre";
			
			if(order.equals("Estado"))
				order = "VALOR.disponible";
			
			if(order.equals("Fecha de Expiracion"))
				order = "VALOR.Fecha_expiracion";
			
			if(order.equals("Mercado"))
				order = "VALOR.mercado";
			
			if(order.equals("Precio"))
				order = "VALOR.precio";
			
			if(order.equals("Creador"))
				order = "VALOR.cod_oferente_creado";
				
			String est = request.getParameter("estado");
			
			if(est.equals("Disponible"))
				est = "T";
			else if(est.equals("No Disponible"))
				est = "F";
						
			try {
				escribirResultado(out, ValorAndes.getInstance().darExistenciaDeValores(nomb, request.getParameter("tipoVal"), request.getParameter("tipoRent"), est,
						dat, corOfer, corInte, corInver, order));
			} catch (Exception e) {
				e.printStackTrace();

			} 
		}

		out.println("			</div>");
		out.println("		</div> ");
		out.println("	</div> ");
		out.println("  ");
	}

	/**
	 * Escribe las opciones de rentabilidad existentes en la base de datos.
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


	/**
	 * Escribe el resultado de una consulta
	 * @param out
	 * @param valorList
	 */
	public void escribirResultado(PrintWriter out, ArrayList<ValorValue> valorList){

		if(valorList.isEmpty())
			out.println("				<div class=\"panel-heading\">No se encontraron resultados</div>");

		else{


			out.println("				<div class=\"panel-heading\">Resultados</div>");
			out.println("				<table class=\"table table-striped\">");
			out.println("					<thead>");
			out.println("						<tr><b>");
			out.println("							<th>Nombre Valor</th>");
			out.println("							<th>Tipo del Valor</th>");
			out.println("							<th>Tipo de Rentabilidad</th>");
			out.println("							<th>Estado</th>");
			out.println("							<th>Fecha de Expiracion</th>	");
			out.println("							<th>Mercado</th>	");
			out.println("							<th>Precio</th>	");
			out.println("							<th>Creador</th>	");
			out.println("							<th>Due&#241o</th>	");
			out.println("							<th>Inversionista</th>	");
			out.println("						</tr></b>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i< valorList.size(); i++){
				ValorValue valor = valorList.get(i);

				out.println("						<tr>");
				out.println("							<td>" + valor.getNombre() + "</td>");
				out.println("							<td>" + valor.getNomTipoValor() + "</td>");
				out.println("							<td>" + valor.getRentabilidad() + "</td>");
				if(valor.isDisponible())
					out.println("							<td>Disponible</td>");
				else
					out.println("							<td>No Disponible</td>");
				Calendar cal = Calendar.getInstance();
				cal.setTime(valor.getFechaExpiracion());
				out.println("							<td>" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) +1) + "/" + cal.get(Calendar.YEAR) + "</td>");
				out.println("							<td>" + valor.getMercado()+ "</td>");
				out.println("							<td>" + valor.getPrecio()+ "</td>");
				out.println("							<td>" + valor.getCreador()+ "</td>");
				
				out.println("						</tr>");	
			}
		}

		out.println("					</tbody>");
		out.println("				</table>");
	}

}
