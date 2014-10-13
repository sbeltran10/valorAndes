package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.OperacionValue;

/**
 * Servlet que representa el servicio de registro de operaciones bursatiles de la aplicacion web.
 */

public class ServletOperacionRegistrar extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "RegistrarOperacion";
	}

	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();

		IntermediarioValue inter = ValorAndes.getInstance().getInter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("		<h1>Registrar Operacion Bursatil<hr></h1>");
		out.println("		");
		out.println("		<h3>Seleccione la operacion bursatil que desea registrar.</h3>");
		out.println("		");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"panel panel-info\">");

		String iop = request.getParameter("op");
		ArrayList<OperacionValue> ops = null;

		try {
			ops = ValorAndes.getInstance().darOperacionesMias(inter.getCorreo());
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		if(iop!=null){
			try {
				ValorAndes.getInstance().registrarOperacion(ops.get(Integer.parseInt(iop)));

				out.println("				La operacion fue registrada exitosamente.<br>");

			}catch (Exception e) {
				out.println("				Ocurrio un error registrando la operacion.<br>");
				e.printStackTrace();
			}
		}

		try {
			construirTablaOp(ValorAndes.getInstance().darOperacionesMias(inter.getCorreo()),  out);


		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("			</div>");
		out.println("		</div> ");
		out.println("");
		out.println("		</div>");
		out.println("	</div>");
		out.println("");
	}

	/**
	 * Metodo que construye la tabla de operaciones existentes para el usuario
	 */
	public void construirTablaOp(ArrayList<OperacionValue> ops, PrintWriter out){

		if(ops.isEmpty())
			out.println("				<div class=\"panel-heading\">Aun no se ha creado ninguna operacion</div>");

		else{
			out.println("				<div class=\"panel-heading\">Operaciones</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr>");
			out.println("							<th>Valor</th>");
			out.println("							<th>Cantidad</th>");
			out.println("							<th>Fecha creacion</th>");
			out.println("							<th>Tipo de Operacion</th>		");
			out.println("							<th>Precio Base</th>");
			out.println("							<th>Solicitante</th>			");
			out.println("							<th>Registrar</th>			");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<ops.size();i++){
				OperacionValue opi = ops.get(i);

				out.println("						<tr><form>");
				out.println("							<td>"+opi.getNomValor() +"</td>");
				out.println("							<td>"+opi.getCantidad() +"</td>");
				out.println("							<td>"+opi.getFecha() +"</td>");
				out.println("							<td>"+opi.getTipoCompraVenta() +"</td>");
				out.println("							<td>"+opi.getPrecio() +"</td>");
				out.println("							<td>"+opi.getCorSolicitante() +"</td>");
				out.println("							<input type=\"hidden\" name=\"op\" value=\"" + i + "\"/>");
				out.println("							<td><button type=\"submit\"  class=\"btn btn-default\" name=\"op\">Registrar Operracion</button></td>");
				out.println("						</tr></form>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("				</div>");
		}
	}

}
