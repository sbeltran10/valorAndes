package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.OperacionValue;
import valorAndes.vos.ValorValue;

public class ServletConsultaValoresDinamicos extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "ValoresDinamicos";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Consulta de Valores mas Dinamicos</h2> ");
		out.println("				Seleccione un rango de tiempo dentro del cual desea consultar los valores que tuvieron la mayor cantidad de movimientos ");
		out.println("					<form role=\"form\">");
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
			fechaFin = arFechas2[2] + "-" + arFechas2[1] +  "-" + arFechas2[0];
			
			try {
				escribirResultados(out, ValorAndes.getInstance().valoresMDinamicos(fechaIni, fechaFin));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		out.println("</div> ");
	}
	
	//----------------------------------------------------------
	// Tabla de resultados
	//----------------------------------------------------------
		
		/**
		 * Se muestran estos resultados si el tipo de usuario a consultar es Oferente
		 */
		public void escribirResultados(PrintWriter out, ArrayList<ValorValue> opers){

			if(opers.isEmpty()){
				out.println("			<div class=\"container\">");
				out.println("				<div class=\"panel panel-info\">");
				out.println("					<div class=\"panel-heading\">No se encontraron Resultados</div>");
			}

			else{
				out.println("			<div class=\"container\">");
				out.println("				<div class=\"panel panel-info\">");
				out.println("					<div class=\"panel-heading\">Resultados</div>");
				out.println("					<table class=\"table table-striped\" data-pagination=\"true\" data-height=\"400\" >");
				out.println("						<thead>");
				out.println("							<tr>");
				out.println("								<th>Creador del Valor</th>");
				out.println("								<th>Fecha expiracion</th>");
				out.println("								<th>Mercado</th>");
				out.println("								<th>Nombre</th>");
				out.println("								<th>Precio</th>	");
				out.println("							</tr>");
				out.println("						</thead>");
				out.println("						<tbody>");

				for(int i=0; i<opers.size();i++){
					ValorValue op = opers.get(i);

					out.println("							<tr><form>");
					out.println("								<td>" + op.getCreador() + "</td>");
					out.println("								<td>" + op.getFechaExpiracion() + "</td>");
					out.println("								<td>" + op.getMercado() + "</td>");
					out.println("								<td>" + op.getNombre() + "</td>");
					out.println("								<td>" + op.getPrecio() + "</td>");
					out.println("							</tr></form>");
				}

				out.println("						</tbody>");
				out.println("					</table>");
			}

			out.println("			</div>");
			out.println("		</div> ");
		}



}
