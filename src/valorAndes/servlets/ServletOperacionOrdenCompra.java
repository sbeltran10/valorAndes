package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;
import valorAndes.vos.OperacionValue;


/**
 * Servlet que representa el servicio de orden de operaciones bursatiles de la aplicacion web.
 */

public class ServletOperacionOrdenCompra extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	//Metodos

	public String darTituloPagina(HttpServletRequest request) {
		return "CompraValor";
	}

	/**
	 * Escribe el contenido de la pagina.
	 */
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		int vaLog = ValorAndes.getInstance().getLogeado();

		InversionistaValue inver = null;
		OferenteValue ofer = null;

		if(vaLog==2)
			inver = ValorAndes.getInstance().getInver();

		else
			ofer = ValorAndes.getInstance().getOfer();

		PrintWriter out = response.getWriter();
		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");

		String cant = request.getParameter("cantidad");

		if(cant!=null){

			String[] vals = request.getParameter("valor").split("-"); 
			boolean si = true;
			int valCant = 0;
			try{
				valCant = Integer.parseInt(cant);
			}
			catch(Exception e){
				si = false;
				out.println("			<h3><b>Debe ingresar una cantidad valida.</b></h3>");	
			}

			if(si && valCant > Integer.parseInt(vals[3])){
				si = false;
				out.println("			<h3><b>La cantidad deseada sobrepasa a la cantidad disponible.</b></h3>");	
			}


			if(si){
				OperacionValue op = new OperacionValue();
				String inter = request.getParameter("intermediario");

				op.setCantidad(valCant);
				op.setCorIntermediario(inter);

				if(inver!=null)
					op.setCorSolicitante(inver.getCorreo());
				else
					op.setCorSolicitante(ofer.getCorreo());

				op.setFecha(new Date());
				try {
					op.setId(ValorAndes.getInstance().darTotalOperaciones() + 1);
				} catch (SQLException e) {
					e.printStackTrace();
				}

				op.setIdValor(vals[0]);
				op.setTipoCompraVenta("COMPRA");


				try {
					ValorAndes.getInstance().ordenarOperacion(op, inter);
					out.println("			<h3><b>Ha ordenado exitosamente la compra de " + cant + " " + vals[1] + " de " + vals[2] + ","
							+ " si lo desea puede ordenar una nueva orden de compra.</b></h3>");

				} catch (Exception e) {
					e.printStackTrace();
					out.println("			<h3><b>Ocurrio un error al realizar la orden de compra.</b></h3>");
				}
			}
		}


		out.println("			<div class=\"row\"><br><br>");
		out.println("				<h2>Operacion de Compra</h2> ");
		out.println("				Escoja el valor que desea comprar (Si tiene dudas sobre cual escoger puede recurrir al <a href = \"consultaVal.htm\">servicio de consulta de valores).</a> <br>");
		out.println("				Debe escoger la cantidad a comprar y tambien debe indicar cual de sus intermediario desea que registre esta operacion bursatil.<hr>");
		out.println("					<form role=\"form\">");
		out.println("						<div class=\"col-lg-6\">		");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Valor a Comprar</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"valor\" required>");
		out.println("										<option></option>");
		if(inver!=null)
			try {
				escribirOpcionesValor(out,ValorAndes.getInstance().darIdValoresCompra(inver.getCorreo()) );
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else
			try {
				escribirOpcionesValor(out,ValorAndes.getInstance().darIdValoresCompra(ofer.getCorreo()) );
			} catch (SQLException e) {
				e.printStackTrace();
			}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>		");
		out.println("						</div>");
		out.println("						<div class=\"col-lg-6\">");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Cantidad Deseada</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<input type=\"number\" class=\"form-control\" id=\"cantidad\" name=\"cantidad\" required>");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("							<div class=\"form-group\">");
		out.println("								<label>Intermediario</label>");
		out.println("								<div class=\"input-group\">");
		out.println("									<select class=\"form-control\" name=\"intermediario\" required>");
		out.println("										<option></option>");
		if(inver!=null)
			try {
				escribirOpcionesIntermediario(out,ValorAndes.getInstance().darIntermediariosmios(inver.getCorreo()) );
			} catch (SQLException e) {
				e.printStackTrace();
			}
		else
			try {
				escribirOpcionesIntermediario(out,ValorAndes.getInstance().darIntermediariosmios(ofer.getCorreo()) );
			} catch (SQLException e) {
				e.printStackTrace();
			}
		out.println("										</select> ");
		out.println("									<span class=\"input-group-addon\"></span>");
		out.println("								</div>");
		out.println("							</div>");
		out.println("						</div>");
		out.println("");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Ordenar Compra\" class=\"btn btn-info pull-right\">");
		out.println("					</form>				");
		out.println("					<hr>");
		out.println("				</div>");
		out.println("			</div><hr>");
		out.println("			");
		out.println("	</div> ");
		out.println("  ");
		out.println("");

	}

	/**
	 * Escribir las opciones existentes de valores y sus ids
	 * @param out
	 * @param rentList
	 */
	public void escribirOpcionesValor(PrintWriter out, ArrayList<String> vaList){
		for(int i =0; i< vaList.size(); i++){
			String[] opcion = vaList.get(i).split("-");
			String op = opcion[0] + "(" + opcion[3] + ") - Precio: " + opcion[2] + " - Cantidad Disponible: " + opcion[4];	

			out.println("					  <option value = \"" + opcion[1] + "-" + opcion[0] + "-" + opcion[3] + "-" + opcion[4] + "\">" +  op + "</option>");
		}
	}

	/**
	 * Escribir las opciones existentes de usuarios.
	 * @param out
	 * @param rentList
	 */
	public void escribirOpcionesIntermediario(PrintWriter out, ArrayList<String> usList){
		for(int i =0; i< usList.size(); i++){
			String[] opcion = usList.get(i).split("-");
			out.println("					  <option value = \"" + opcion[1]+ "\"> " +  opcion[0] + "(" + opcion[1] + ")</option>");
		}
	}

}
