package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.IntermediarioValue;

public class ServletRetirarIntermediario extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "RetirarIntermediario";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("		<h1>Retirar Intermediario<hr></h1>");
		
		String correoViej = request.getParameter("correo");
		String correoNuevo = request.getParameter("reemplazo");
		if(correoViej!=null){
			try {
				ValorAndes.getInstance().retirarIntermediario(correoViej, correoNuevo);
				out.println("				<br><h5>El intermediario fue retirado exitosamente<br></h5>");
			} catch (SQLException e) {
				e.printStackTrace();
				out.println("				<br><h4>Ocurrio un error retirando al intermediario.<br></h4>");
			}
		}
		
		out.println("		");
		out.println("		<h3>Seleccione el intermediario que desea retirar de la bolsa de valores.<form></h3>");
		out.println("		");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"panel panel-info\">");

		try {
			construirTablaInter(ValorAndes.getInstance().darTodosIntermediarios(),  out);
		} catch (SQLException e) {

			e.printStackTrace();
		}
		out.println("					</div> ");
		out.println("				</div>");
		out.println("			<div class=\"row\"><hr>");
		out.println("				<h2>Reemplazo</h2> ");
		out.println("				Seleccione el intermediario que remplazara al que se retira<hr>");
		out.println("						<div class=\"col-lg-3\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Intermediario Reemplazo</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"reemplazo\" required>");
		out.println("										<option></option>");
		try {
			escribirReemplazo(ValorAndes.getInstance().darTodosIntermediarios(),  out);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Retirar Intermediario\" class=\"btn btn-info pull-right\">");						
		out.println("			</form></div>");
		out.println("		</div> ");
		out.println("	</div> ");
		out.println("</div> ");
		out.println("");
		out.println("<style> h4 {color:red} h5 {color:blue} </style> ");

	}

	/**
	 * Metodo que construye la tabla de operaciones existentes para el usuario
	 */
	public void construirTablaInter(ArrayList<IntermediarioValue> ops, PrintWriter out){

		if(ops.isEmpty())
			out.println("				<div class=\"panel-heading\">Aun no hay intermediarios en la bolsa.</div>");

		else{
			out.println("				<div class=\"panel-heading\">Operaciones</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr>");
			out.println("							<th>Correo</th>");
			out.println("							<th>Nombre</th>");
			out.println("							<th>Nacionalidad</th>");
			out.println("							<th>Departamento</th>		");
			out.println("							<th>Ciudad</th>");
			out.println("							<th>Direccion</th>			");
			out.println("							<th>Telefono</th>			");
			out.println("							<th>Numero de Registro</th>");
			out.println("							<th>Tipo de Entidad</th>		");
			out.println("							<th>Retirar</th>");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<ops.size();i++){
				IntermediarioValue opi = ops.get(i);

				out.println("						<tr>");
				out.println("							<td>"+opi.getCorreo() +"</td>");
				out.println("							<td>"+opi.getNombre() +"</td>");
				out.println("							<td>"+opi.getNacionalidad() +"</td>");
				out.println("							<td>"+opi.getDepartamento() +"</td>");
				out.println("							<td>"+opi.getCiudad() +"</td>");
				out.println("							<td>"+opi.getDireccion() +"</td>");
				out.println("							<td>"+opi.getTelefono() +"</td>");
				out.println("							<td>"+opi.getNumRegistro() +"</td>");
				out.println("							<td>"+opi.getTipoEntidad() +"</td>");
				out.println("							<td><input type=\"radio\" name=\"correo\" value=\"" + opi.getCorreo() + "\"></td>");
				out.println("						</tr>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("				</div>");

		}
	}
	
	/**
	 * Escribe la lista de los intermediarios de reemplazo
	 */

	public boolean escribirReemplazo(ArrayList<IntermediarioValue> ops, PrintWriter out){
		boolean hay = false;
		if(!ops.isEmpty()){
			for(int i =0; i< ops.size(); i++){
				IntermediarioValue opcion = ops.get(i);
				out.println("					  <option value = \"" + opcion.getCorreo() + "\"> " +  opcion.getNombre() + " (" + opcion.getCorreo() + ")</option>");
			}
			hay = true;
		}
		return hay;
	
	}
}
