package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletConsultaPortafolio extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	public String darTituloPagina(HttpServletRequest request) {
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		PrintWriter out = response.getWriter();

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

		out.println("										<option></option>");
		out.println("										<option>Oferente</option>");
		out.println("										<option>Inversionista</option>");
		out.println("										<option>Intermediario</option>");

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

		}
		out.println("	</div> ");
	}

}
