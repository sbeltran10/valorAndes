package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.OperacionValue;

/**
 * Servlet que representa el servicio de consultas de operaciones de la palicacion web.
 */


public class ServletConsultaOperacion extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "ConsultaOperacion";
	}

	/**
	 * Escribe el contenido de la pagina
	 */
	public void escribirContenido(HttpServletRequest request, HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Operaciones</h2> ");
		out.println("					A continuacion seleccione los filtros por los cuales desea hacer la consulta<hr>");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-3\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Tipo de Usuario</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoUsu\" required>");
		out.println("										<option>Oferente</option>");
		out.println("										<option>Inversionista</option>");
		out.println("										<option>Intermediario</option>");
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Tipo de Operacion</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoOper\" required>");
		out.println("										<option>---</option>");
		out.println("										<option>Compra</option>");
		out.println("										<option>Venta</option>");
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>		");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Nombre del valor</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"text\" class=\"form-control\" id=\"valor\" name=\"valor\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Cantidad</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"number\" class=\"form-control\" id=\"cantidad\" name=\"cantidad\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Fecha Inicial</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaIni\" name=\"fechaIni\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Tipo de Rentabilidad</label>");
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
		out.println("						</div>");
		out.println("						");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label for=\"InputEmail\">Fecha Final</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaFin\" name=\"fechaFin\" >");
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

		String fechI = request.getParameter("fechaIni");
		String fechF = request.getParameter("fechaFin");

		if(fechI!=null){ 

			String datI = null;

			if(!fechI.isEmpty()){

				String[] arFechas1 = fechI.split("-");
				datI = arFechas1[2] + "-" + arFechas1[1] +  "-" + arFechas1[0];
			}  

			String datF = null;

			if(!fechF.isEmpty()){

				String[] arFechas2 = fechF.split("-");
				datF = arFechas2[2] + "-" + arFechas2[1] +  "-" + arFechas2[0];
			}

			String nombVal = request.getParameter("valor");
			if(nombVal.isEmpty())
				nombVal = "---";

			String cant = request.getParameter("costo");
			int vcant = 0;
			if(!cant.isEmpty())
				vcant = Integer.parseInt(cant);



			try {
				escribirResultado(out, ValorAndes.getInstance().darOperaciones(request.getParameter("tipoUsu").toUpperCase(),
						request.getParameter("tipoOper"), datI, datF, vcant, nombVal, request.getParameter("tipoRent")));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		out.println("");
		out.println("			</div>");
		out.println("		</div> ");
		out.println("	</div> ");
		out.println("  ");
	}


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
	 * Escribe el resultado de una busqueda
	 * @param out
	 * @param opList
	 */
	public void escribirResultado(PrintWriter out, ArrayList<OperacionValue> opList){

		if(opList.isEmpty())
			out.println("				<div class=\"panel-heading\">No se encontraron resultados</div>");

		else{

			out.println("				<div class=\"panel-heading\">Resultados</div>");
			out.println("				<table class=\"table table-striped\">");
			out.println("					<thead>");
			out.println("						<tr><b>");
			out.println("							<th>Valor</th>");
			out.println("							<th>Cantidad</th>");
			out.println("							<th>Precio Base</th>");
			out.println("							<th>Fecha Orden</th>");
			out.println("						</tr></b>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i< opList.size(); i++){
				OperacionValue op = opList.get(i);

				out.println("						<tr>");
				out.println("							<td>" + op.getIdValor() + "</td>");
				out.println("							<td>" + op.getCantidad() + "</td>");
				out.println("							<td>" + op.getPrecio() + "</td>");

				Calendar cal = Calendar.getInstance();
				cal.setTime(op.getFecha());
				out.println("							<td>" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) +1) + "/" + cal.get(Calendar.YEAR) + "</td>");
				out.println("						</tr>");	
			}

			out.println("					</tbody>");
			out.println("				</table>");
		}
	}

}
