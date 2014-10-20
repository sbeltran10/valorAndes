package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.InPortafolioValue;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.ValorPorcentajeInversionValue;
import valorAndes.vos.ValorValue;

public class ServletPortafolioInversionista extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		InversionistaValue inver = ValorAndes.getInstance().getInver();

		PrintWriter out = response.getWriter();

		out.println("	<div class=\"well well-lg\">");
		out.println("		<div class=\"container\">");
		out.println("		<br><br><br>");

		String inPorta = request.getParameter("inPorta");
		
		if(inPorta!=null){
			
			out.println("		<h1>Recomponer Portafolio<hr></h1>");
			out.println("		");
			out.println("		<h3>Seleccione los nuevos porcentajes para el portafolio o agregue nuevos valores al portafolio.</h3>");
			out.println("		");
			out.println("		<form><div class=\"container\">");
			out.println("			<div class=\"panel panel-info\">");
			
			String op = request.getParameter("submit");
			
			if(op!=null){
				if(op.equals("Agregar Valor")){
					try {
						ValorAndes.getInstance().agregarValorInPortafolio(inver.getCorreo(), Integer.parseInt(inPorta), Integer.parseInt(request.getParameter("valorAgregar")));
						out.println("				<br><h5>Se agrego exitosamente el valor.<br></h5>");

						
					} catch (Exception e) {
						e.printStackTrace();
						out.println("				<br><h4>Ocurrio un error al agregar el valor.<br></h4>");
					} 
				}
				else{
					boolean porcenCompleto = true;
					int porcen = 0;
					for(int i =0; i< Integer.parseInt(request.getParameter("totValores")); i++){
						int por = Integer.parseInt(request.getParameter("porcen" + i));
						porcen += por;
					}
					if(porcen!=100){
						porcenCompleto = false;
						out.println("				<br><h4>Los porcentajes de recompocision deben sumar el 100%<br></h4>");
					}
					
					if(porcenCompleto){
						for(int i =0; i< Integer.parseInt(request.getParameter("totValores")); i++){
							int por = Integer.parseInt(request.getParameter("porcen" + i));
							int codVal = Integer.parseInt(request.getParameter("val" + i));
							
							try {
								ValorAndes.getInstance().cambiarInPorcentaje(inver.getCorreo(), Integer.parseInt(inPorta), codVal, por);
								out.println("				<br><h5>Se recompuso el portafolio exitosamente.<br></h5>");

							} catch (Exception e) {

								e.printStackTrace();
								out.println("				<br><h4>Ocurrio un error al recomponer el portafolio.<br></h4>");

							}
							
						}
					}
				}
			}
			
			try {
				construirTablaValoresInPortafolio(ValorAndes.getInstance().darValoresInPortafolio(inver.getCorreo(), Integer.parseInt(inPorta)), out, inPorta);

			} catch (Exception e) {
				e.printStackTrace();
			}
			out.println("			</div>");
			out.println("		</div></form> ");
			out.println("				Agregue un nuevo valor al portafolio.<hr>");
			out.println("					<form role=\"form\">");
			out.println("						<div class=\"col-lg-3\">		");
			out.println("							<div class=\"form-group\">");
			out.println("								<label>Valores</label>");
			out.println("								<div class=\"input-group\">");
			out.println("									<select class=\"form-control\" name=\"valorAgregar\" required>");
			out.println("										<option></option>");
			try {
				boolean opcionesVal = escribirOpcionesValor(out, ValorAndes.getInstance().darValoresPortafolio(Integer.parseInt(request.getParameter("inPorta")), request.getParameter("interPorta")));
				if(!opcionesVal){
					out.println("				<br><h4>Todavia no existen valores en el portafolio.<br></h4>");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			out.println("										</select> ");
			out.println("									<span class=\"input-group-addon\"></span>");
			out.println("								</div>");
			out.println("							</div>");
			out.println("						</div>");
			out.println("							<input type=\"hidden\" name=\"inPorta\" value=\"" + inPorta + "\"/>");
			out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Agregar Valor\" class=\"btn btn-info pull-right\"></form>");											
		}
		
		//--------------------------------------------------------------------------
		
		else{
			out.println("		<h1>Administrar Portafolios<hr></h1>");
			out.println("		");
			out.println("		<h3>Seleccione el portafolio que desea recomponer.</h3>");
			out.println("		");
			out.println("		<div class=\"container\">");
			out.println("			<div class=\"panel panel-info\">");

			try {
				
				construirTablaPorts(ValorAndes.getInstance().darInPortafolios(inver.getCorreo()),  out);

			} catch (Exception e) {
				e.printStackTrace();
			}
			out.println("			</div>");
			out.println("		</div> ");
			out.println("");
		}
		
		out.println("		</div>");
		out.println("	</div>");
		out.println("");
		out.println("<style> h4 {color:red} h5 {color:blue} </style> ");

	}

	/**
	 * Metodo que construye la tabla de operaciones existentes para el usuario
	 */
	public void construirTablaPorts(ArrayList<InPortafolioValue> portas, PrintWriter out){

		if(portas.isEmpty())
			out.println("				<div class=\"panel-heading\">Aun no existen un portafolio asociado a un intermediario</div>");

		else{
			out.println("				<div class=\"panel-heading\">Operaciones</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr>");
			out.println("							<th>Nombre Portafolio</th>");
			out.println("							<th>Intermediario Asociado</th>");
			out.println("							<th>Nivel de Riesgo</th>		");
			out.println("							<th>Recomponer</th>			");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<portas.size();i++){
				InPortafolioValue portval = portas.get(i);

				out.println("						<tr><form>");
				out.println("							<td>"+portval.getParentPortafolio().getNombre() +"</td>");
				out.println("							<td>"+portval.getParentPortafolio().getCorreoInter() +"</td>");
				out.println("							<td>"+portval.getParentPortafolio().getTipoRiesgo() +"</td>");
				out.println("							<input type=\"hidden\" name=\"inPorta\" value=\"" + portval.getParentPortafolio().getId() + "\"/>");
				out.println("							<input type=\"hidden\" name=\"interPorta\" value=\"" + portval.getParentPortafolio().getCorreoInter() + "\"/>");
				out.println("							<td><button type=\"submit\"  class=\"btn btn-default\" name=\"port\">Recomponer Portafolio</button></td>");
				out.println("						</tr></form>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("				</div>");
		}
	}

	/**
	 * Metodo que construye la tabla de los valores que hay dentro de un InPortafolio
	 */
	public void construirTablaValoresInPortafolio(ArrayList<ValorPorcentajeInversionValue> vals, PrintWriter out, String codPortafolio){

		
		if(vals.isEmpty())
			out.println("				<form><div class=\"panel-heading\">Aun no existen valores en el portafolio</div>");

		else{
			out.println("				<div class=\"panel-heading\">Operaciones</div>");
			out.println("				<div class=\"form-group\">");
			out.println("				<table class=\"table table-striped\" name=\"tablaOp\">");
			out.println("					<thead>");
			out.println("						<tr>");
			out.println("							<th>Nombre Valor</th>");
			out.println("							<th>Fecha de Expiracion</th>	");
			out.println("							<th>Mercado</th>	");
			out.println("							<th>Precio</th>	");
			out.println("							<th>Porcentage (%)</th>	");
			out.println("						</tr>");
			out.println("					</thead>");
			out.println("					<tbody>");

			for(int i =0; i<vals.size();i++){
				ValorPorcentajeInversionValue val = vals.get(i);

				out.println("						<tr>");
				out.println("							<td>"+val.getValor().getNombre() +"</td>");
				out.println("							<td>"+val.getValor().getFechaExpiracion() +"</td>");
				out.println("							<td>"+val.getValor().getMercado() +"</td>");
				out.println("							<td>"+val.getValor().getPrecio() +"</td>");
				out.println("							<input type=\"hidden\" name=\"inPorta\" value=\"" + codPortafolio + "\"/>");
				out.println("							<input type=\"hidden\" name=\"val" + i + "\" value=\"" + val.getValor().getCodigo() + "\"/>");
				out.println("							<td><input type=\"number\" name=\"porcen" + i +  "\" value=\"" +  val.getPorcentajeInversion() + "\" required></td>");
				out.println("						</tr>");
			}
			out.println("					</tbody>");
			out.println("				</table>");
			out.println("				</div>");
		}
		out.println("							<input type=\"hidden\" name=\"totValores\" value=\"" + vals.size() + "\"/>");
		out.println("						<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Recomponer Portafolio\" class=\"btn btn-info pull-right\"></form>");	
		
	}
	
	
	/**
	 * Escribir las opciones existentes de valores y sus ids
	 * @param out
	 * @param rentList
	 */
	public boolean escribirOpcionesValor(PrintWriter out, ArrayList<ValorValue> arrayList){
		boolean hay = false;
		
		if(!arrayList.isEmpty()){
			for(int i =0; i< arrayList.size(); i++){
				ValorValue opcion = arrayList.get(i);
				
				String op = opcion.getNombre() + " - Precio: $" + opcion.getPrecio();	

				out.println("					  <option value = \"" + opcion.getCodigo() + "\">" +  op + "</option>");
			}
			hay = true;
		}
		return hay;
	}
}


