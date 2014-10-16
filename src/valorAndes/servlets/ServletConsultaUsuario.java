package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;

public class ServletConsultaUsuario  extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {

		return "Consulta Usuario";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		int vaLog = ValorAndes.getInstance().getLogeado();

		PrintWriter out = response.getWriter();

		String opid = request.getParameter("opid");
		if(opid!=null){
			escribirInformacion(out, opid);
			
		}

		else{


			out.println("	<div class=\"well well-lg\">");
			out.println("		<div class=\"container\">");
			out.println("			<div class=\"row\"><br><br>");
			out.println("				<h2>Consulta de Usuarios</h2> ");
			out.println("				Llene los filtros que considere necesarios si desea ver a un usuario en especifico<hr>");
			out.println("					<form role=\"form\">");
			out.println("						<div class=\"col-lg-3\">		");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Debe escoger un tipo de usuario</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<select class=\"form-control\" name=\"tipoUs\" required>");
			if(vaLog==4){
				out.println("										<option></option>");
				out.println("										<option>Oferente</option>");
				out.println("										<option>Inversionista</option>");
				out.println("										<option>Intermediario</option>");
			}
			else
				out.println("										<option>Inversionista</option>");
			out.println("										</select> ");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>		");
			out.println("						</div>");
			out.println("						<div class=\"col-lg-3\">");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Correo</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"email\" class=\"form-control\" id=\"correo\" name=\"correo\" >");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Pais</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"email\" class=\"form-control\" id=\"pais\" name=\"pais\" >");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("						<div class=\"col-lg-3\">");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Nombre</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"text\" class=\"form-control\" id=\"nombre\" name=\"nombre\" >");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Ciudad</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"email\" class=\"form-control\" id=\"ciudad\" name=\"ciudad\" >");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("						");
			out.println("						<div class=\"col-lg-3\">");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Telefono</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"text\" class=\"form-control\" id=\"tel\" name=\"tel\" >");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("							<div class=\"form-group\">");
			out.println("								<label for=\"InputEmail\">Id Representante</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<input type=\"email\" class=\"form-control\" id=\"idRe\" name=\"idRe\" >");
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

			String tipoUs = request.getParameter("tipoUs");
			if(tipoUs!=null){

				String correo = request.getParameter("correo");
				if(correo == "")
					correo="---";

				String pais = request.getParameter("pais");
				if(pais == "")
					pais="---";

				String nombre = request.getParameter("nombre");
				if(nombre == "")
					nombre="---";

				String ciudad = request.getParameter("ciudad");
				if(ciudad == "")
					ciudad="---";

				String telefono = request.getParameter("tel");
				if(telefono == "")
					telefono="---";

				String idRepresentante = request.getParameter("idRe");
				if(idRepresentante == "")
					idRepresentante="---";

				try {
					escribirResultados(out, ValorAndes.getInstance().darUsuarios(tipoUs, correo, nombre, telefono, pais, ciudad, idRepresentante), tipoUs);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			//Se escriben los resultados de las tablas dependiendo del tipo de usuario
			out.println("	</div> ");
		}
	}

	
	//-----------------------------------------------------
	//Resultados de un usuario especifico
	//-----------------------------------------------------
	/**
	 * Escribe la informacion completa de un usuario en especifico
	 */
	public void escribirInformacion(PrintWriter out, String opid){

		String[] opids = opid.split("-");
		
		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<div class=\"row\">");
		out.println("			<br><br><br>");
		
		if(opids[1].equals("Oferente")){
			OferenteValue ofer = null;
			try {
				ofer = ValorAndes.getInstance().darInfoOferente(opids[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.println("	<div class=\"well well-lg\">");
			out.println("		<div class=\"container\">");
			out.println("		<div class=\"row\">");
			out.println("			<br><br><br>");
			out.println("					<h2> Nombre </h2><br>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Correo:</u>&nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Telefono:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nacionalidad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Departamento:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Ciudad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Direccion:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Id Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nombre Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Codigo Postal:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
		}
		
		else if(opids[1].equals("Inversionista")){
			InversionistaValue inver = null;
			try {
				inver = ValorAndes.getInstance().darInfoInversionista(opids[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.println("	<div class=\"well well-lg\">");
			out.println("		<div class=\"container\">");
			out.println("		<div class=\"row\">");
			out.println("			<br><br><br>");
			out.println("					<h2> Nombre </h2><br>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Correo:</u>&nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Telefono:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nacionalidad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Departamento:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Ciudad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Direccion:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Id Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nombre Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Codigo Postal:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
		}
		
		else{
			IntermediarioValue inter = null;
			try {
				inter = ValorAndes.getInstance().darInfoIntermediario(opids[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			out.println("	<div class=\"well well-lg\">");
			out.println("		<div class=\"container\">");
			out.println("		<div class=\"row\">");
			out.println("			<br><br><br>");
			out.println("					<h2> Nombre </h2><br>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Correo:</u>&nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Telefono:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nacionalidad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Departamento:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Ciudad:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Direccion:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Id Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Nombre Representante:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
			out.println("					<div class=\"col-lg-6\">");
			out.println("						<h3> <u>Codigo Postal:</u> &nbsp sadasda</h3>");
			out.println("					</div>");
		}
		
		out.println("			</div>");
		out.println("		</div>");
		out.println("	</div>");
	}

	//-----------------------------------------------------
	//Resultados iniciales de tabla
	//-----------------------------------------------------
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
