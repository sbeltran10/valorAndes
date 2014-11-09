package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.OperacionValue;

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
		out.println("				Seleccione un rango de tiempo dentro del cual desea consultar movimientos sobre valores y ");
		out.println("				seleccione como desea que funcionen los demas filtros a continuacion.<hr>");
		out.println("					<form role=\"form\">");
		out.println("				<input type=\"radio\" name=\"incluirFil\" checked = \"checked\" value=\"si\">Consultar movimientos que SI cumplan con los filtros<br>"
				+ " <input type=\"radio\" name=\"incluirFil\" value=\"no\">Consultar movimientos que NO cumplan con los filtros<hr>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Fecha Inicial</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaIni\" name=\"fechaIni\" required >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Fecha Final</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"date\" class=\"form-control\" id=\"fechaFin\" name=\"fechaFin\" required>");
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
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
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
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
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
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label=>Tipo de movimiento</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"tipoMov\" >");
		out.println("										<option>---</option>");
		out.println("										<option>Compra</option>");
		out.println("										<option>Venta</option>");
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Correo Intermediario</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"inter\" name=\"inter\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-3\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Correo Solicitante</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"email\" class=\"form-control\" id=\"inver\" name=\"inver\" >");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Buscar\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");

		String fechaIni = request.getParameter("fechaIni");
		
		if(fechaIni!=null){

			String fechaFin = request.getParameter("fechaFin");
			
			String[] arFechas1 = fechaIni.split("-");
			fechaIni = arFechas1[2] + "-" + arFechas1[1] +  "-" + arFechas1[0];
			
			String[] arFechas2 = fechaFin.split("-");
			fechaFin = arFechas1[2] + "-" + arFechas1[1] +  "-" + arFechas1[0];
				
			String incluir = request.getParameter("incluirFil");
			
			boolean incluirFiltros = false;
			if(incluir.equals("si")) incluirFiltros = true;
			
			String tipoVal = request.getParameter("tipoVal");
			
			String tipoRent = request.getParameter("tipoRent");
			
			String tipoMov = request.getParameter("tipoMov");
			
			String val = request.getParameter("val");
			if(val == "")
				val="---";

			String inter = request.getParameter("inter");
			if(inter == "")
				inter="---";

			String inver = request.getParameter("inver");
			if(inver == "")
				inver="---";

			try {
				escribirResultados(out, ValorAndes.getInstance().consultarMovimientos(fechaIni, fechaFin, incluirFiltros, val, tipoVal, tipoRent, tipoMov, inver, inter));
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
	public void escribirResultados(PrintWriter out, ArrayList<OperacionValue> opers){

		if(opers.isEmpty()){
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">No se encontraron Resultados</div>");
		}

		else{
			out.println("			<div class=\"container\">");
			out.println("				<div class=\"panel panel-info\">");
			out.println("					<div class=\"panel-heading\">Resultados</div>");
			out.println("					<table class=\"table table-striped\">");
			out.println("						<thead>");
			out.println("							<tr>");
			out.println("								<th>Fecha movimiento</th>");
			out.println("								<th>Tipo de movimiento</th>");
			out.println("								<th>Nombre del valor</th>");
			out.println("								<th>Correo Solicitante</th>");
			out.println("								<th>Cantidad de operacion</th>	");
			out.println("							</tr>");
			out.println("						</thead>");
			out.println("						<tbody>");

			for(int i=0; i<opers.size();i++){
				OperacionValue op = opers.get(i);

				out.println("							<tr><form>");
				out.println("								<td>" + op.getFecha() + "</td>");
				out.println("								<td>" + op.getTipoCompraVenta() + "</td>");
				out.println("								<td>" + op.getNomValor() + "</td>");
				out.println("								<td>" + op.getCorSolicitante() + "</td>");
				out.println("								<td>" + op.getCantidad() + "</td>");
				out.println("							</tr></form>");
			}

			out.println("						</tbody>");
			out.println("					</table>");
		}

		out.println("			</div>");
		out.println("		</div> ");
	}

	
}
