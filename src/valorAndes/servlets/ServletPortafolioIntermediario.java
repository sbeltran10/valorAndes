package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.PortafolioValue;
import valorAndes.vos.ValorValue;

public class ServletPortafolioIntermediario extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		IntermediarioValue inter = ValorAndes.getInstance().getInter();

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("		<h1>Administrar Portafolios<hr></h1>");

		String porta = request.getParameter("portAdmin");

		String nuevoPorta = request.getParameter("nombrePort");

		if(nuevoPorta!=null){
			try{
				ValorAndes.getInstance().crearPortafolio(inter.getCorreo(), nuevoPorta, request.getParameter("nivelRiesgo"));
				out.println("				<br><h5>Se creo el portafolio " + nuevoPorta + " exitosamente.<br></h5>");

			} catch (Exception e) {
				out.println("				<br><h4>Ocurrio un error creando el portafolio.<br></h4>");

				e.printStackTrace();
			}
		}

		if(porta==null){
			try {
				construirTablaPort(ValorAndes.getInstance().darPortafoliosIntermediario(inter.getCorreo()),  out);
			} catch (Exception e) {

				e.printStackTrace();
			}

			out.println("			<div class=\"row\"><hr>");
			out.println("				<h2>Crear Portafolio</h2> ");
			out.println("				Llene los datos para crear un nuevo portafolio<hr>");
			out.println("					<form role=\"form\">");
			out.println("						<div class=\"col-lg-3\">		");
			out.println("							<div class=\"form-group\">");
			out.println("								<label>Nombre</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"text\" class=\"form-control\" id=\"nombrePort\" name=\"nombrePort\" required>");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("						<div class=\"col-lg-3\">		");
			out.println("							<div class=\"form-group\">");
			out.println("								<label>Nivel de Riesgo</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<select class=\"form-control\" name=\"nivelRiesgo\" required>");
			out.println("										<option></option>");
			out.println("										<option>Conservador</option>");
			out.println("										<option>Moderado</option>");
			out.println("										<option>Agresivo</option>");
			out.println("										</select> ");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Crear nuevo Portafolio\" class=\"btn btn-info pull-right\">");						
			out.println("					</form>");

		}
		else{
			
			String[] portVal = porta.split("-");
			out.println("			<div class=\"row\"><hr>");
			String nuevoVal = request.getParameter("valorAgregar");
			String valElim = request.getParameter("valElim");
			
			if(nuevoVal!=null){
				String[] nuevoValVals = nuevoVal.split("-");
				try {
					ValorAndes.getInstance().anadirValorAPortafolio(Integer.parseInt(portVal[0]), Integer.parseInt(nuevoValVals[0]));
					out.println("			<h5><b>Se ha anadido exitosamente el valor al portafolio</h5></b>");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(valElim!=null){
				try {
					ValorAndes.getInstance().eliminarValorPortafolio(Integer.parseInt(portVal[0]), Integer.parseInt(valElim));
					out.println("			<h5><b>Se ha eliminado exitosamente el valor del portafolio</h5></b>");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			out.println("				Agregue un nuevo valor al portafolio de los que les fue asignado para vender o elimine un valor del portafolio.<hr>");
			out.println("					<form role=\"form\">");
			out.println("						<div class=\"col-lg-3\">		");
			out.println("							<div class=\"form-group\">");
			out.println("								<label>Valores en Venta</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<select class=\"form-control\" name=\"valorAgregar\" required>");
			out.println("										<option></option>");
			try {
				boolean opcionesVal = escribirOpcionesValor(out, ValorAndes.getInstance().darValoresInterVenta(inter.getCorreo()));
				if(!opcionesVal){
					out.println("				<br><h4>Usted aun no tiene ningun valor asignado para vender.<br></h4>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			out.println("										</select> ");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("							<input type=\"hidden\" name=\"portAdmin\" value=\"" + porta + "\"/>");
			out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Agregar Valor\" class=\"btn btn-info pull-right\">");						
			out.println("					</form><br><hr>");
			try {
				construirTablaPortVals(ValorAndes.getInstance().darValoresPortafolio(Integer.parseInt(portVal[0]),  inter.getCorreo()),out,portVal[1] + " Nivel de Riesgo: " + portVal[2] , porta);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		out.println("		");
		out.println("		");
		out.println("");
		out.println("		</div>");
		out.println("	</div>");
		out.println("");
		out.println("<style> h4 {color:red} h5 {color:blue} </style> ");
	}

	/**
	 * Metodo que construye la tabla de operaciones existentes para el usuario
	 */
	public void construirTablaPort(ArrayList<PortafolioValue> ports, PrintWriter out){

		
		out.println("			<div class=\"container\">");
		out.println("				<div class=\"panel panel-info\">");
		if(ports.isEmpty())
			out.println("				<div class=\"panel-heading\">Aun no se ha creado un portafolio.</div>");

		else{
			out.println("				<div class=\"panel-heading\">Portafolios</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr>");
			out.println("							<th>Nombre</th>");
			out.println("							<th>Nivel de Riesgo</th>");
			out.println("							<th>Administrar</th>");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<ports.size();i++){
				PortafolioValue port = ports.get(i);

				out.println("						<tr><form>");
				out.println("							<td>"+port.getNombre() +"</td>");
				out.println("							<td>"+port.getTipoRiesgo() +"</td>");
				out.println("							<input type=\"hidden\" name=\"portAdmin\" value=\"" + port.getId() + "-" + port.getNombre() + "-" + port.getTipoRiesgo() + "\"/>");
				out.println("							<td><button type=\"submit\"  class=\"btn btn-default\" name=\"portAdmin\">Administrar Portafolio</button></td>");
				out.println("						</tr></form>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
		}
		out.println("			</div>");
		out.println("		</div> ");
	}

	/**
	 * Construye la tabla con la informacion de un portafolio
	 */
	public void construirTablaPortVals(ArrayList<ValorValue> vals, PrintWriter out, String nomPorta, String portAdmin){
		
		out.println("			<div class=\"container\">");
		out.println("				<div class=\"panel panel-info\">");
		if(vals.isEmpty())
			out.println("				<div class=\"panel-heading\">Aun no se han anadido valores al portafolio.</div>");

		else{
			out.println("				<div class=\"panel-heading\">" + nomPorta +  "</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr><form>");
			out.println("							<th>Nombre Valor</th>");
			out.println("							<th>Estado</th>");
			out.println("							<th>Fecha de Expiracion</th>	");
			out.println("							<th>Mercado</th>	");
			out.println("							<th>Precio</th>	");
			out.println("							<th>Creador</th>	");
			out.println("							<th>Eliminar</th>	");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<vals.size();i++){
				ValorValue val = vals.get(i);

				out.println("						<tr>");
				out.println("							<td>" + val.getNombre() + "</td>");
				if(val.isDisponible())
					out.println("							<td>Disponible</td>");
				else
					out.println("							<td>No Disponible</td>");
				Calendar cal = Calendar.getInstance();
				cal.setTime(val.getFechaExpiracion());
				out.println("							<td>" + cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) +1) + "/" + cal.get(Calendar.YEAR) + "</td>");
				out.println("							<td>" + val.getMercado()+ "</td>");
				out.println("							<td>" + val.getPrecio()+ "</td>");
				out.println("							<td>" + val.getCreador()+ "</td>");
				out.println("							<input type=\"hidden\" name=\"valElim\" value=\"" + val.getCodigo() + "\"/>");
				out.println("							<input type=\"hidden\" name=\"portAdmin\" value=\"" + portAdmin + "\"/>");
				out.println("							<td><button type=\"submit\"  class=\"btn btn-default\" name=\"valElim\">Eliminar Valor</button></td>");
				out.println("						</tr></form>");	
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("			</div>");
			out.println("		</div> ");
		}
	}
	
	/**
	 * Escribir las opciones existentes de valores y sus ids
	 * @param out
	 * @param rentList
	 */
	public boolean escribirOpcionesValor(PrintWriter out, ArrayList<String> vaList){
		boolean hay = false;
		if(!vaList.isEmpty()){
			for(int i =0; i< vaList.size(); i++){
				String[] opcion = vaList.get(i).split("-");
				String op = opcion[0] + " (" + opcion[3] + ") - Precio: $" + opcion[2] + " - Cantidad Disponible: " + opcion[4];	

				out.println("					  <option value = \"" + opcion[1] + "-" + opcion[0] + "-" + opcion[3] + "-" + opcion[4] + "\">" +  op + "</option>");
			}
			hay = true;
		}
		return hay;
	}
}


