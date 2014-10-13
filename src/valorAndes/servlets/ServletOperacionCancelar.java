package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;
import valorAndes.vos.OperacionValue;

/**
 * Servlet que representa el servicio de cancelacion de operaciones bursatiles de la aplicacion web.
 */

public class ServletOperacionCancelar extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "CancelarOperacion";
	}

	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

		int vaLog = ValorAndes.getInstance().getLogeado();

		InversionistaValue inver = null;
		OferenteValue ofer = null;

		if(vaLog==2)
			inver = ValorAndes.getInstance().getInver();

		else
			ofer = ValorAndes.getInstance().getOfer();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");
		out.println("		<h1>Cancelar Operacion Bursatil<hr></h1>");
		out.println("		");
		out.println("		<h3>Seleccione la operacion bursatil que desea cancelar.</h3>");
		out.println("		");
		out.println("		<div class=\"container\">");
		out.println("			<div class=\"panel panel-info\">");

		String op = request.getParameter("opid");

		if(op!=null){

			try {
				ValorAndes.getInstance().cancelarOperacion(request.getParameter("opinter"), Integer.parseInt(op));
				out.println("				La operacion fue cancelada exitosamente.<br>");

			}catch (Exception e) {
				out.println("				Ocurrio un error cancelando la operacion.<br>");
				e.printStackTrace();
			}
		}

		try {
			if(vaLog ==2)
				construirTablaOp(ValorAndes.getInstance().darOperacionesMias(inver.getCorreo()),  out);
			
			else 
				construirTablaOp(ValorAndes.getInstance().darOperacionesMias(ofer.getCorreo()),  out);
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		out.println("			</div>");
		out.println("		</div> ");
		out.println("");
		out.println("		</div>");
		out.println("	</div>");
		out.println("");
		out.println("<style> h4 {color:red} h5 {color:blue} </style> ");

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
			out.println("							<th>Precio</th>");
			out.println("							<th>Intermediario</th>");
			out.println("							<th>Cancelar</th>			");
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
				out.println("							<td>"+opi.getCorIntermediario() +"</td>");
				out.println("							<input type=\"hidden\" name=\"opid\" value=\"" + opi.getId() + "\"/>");
				out.println("							<td><button type=\"submit\"  class=\"btn btn-default\" name=\"op\">Cancelar Operacion</button></td>");
				out.println("						</tr></form>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("				</div>");
		}
	}

}
