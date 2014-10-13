package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRegistroUsuario extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		
		return "RegistroUsuario";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {


		PrintWriter out = response.getWriter();
		
		out.println("<div class=\"well well-lg\">");
		out.println("    <div class=\"container\">");
		out.println("		<div class=\"row\"><br><br>");
		out.println("		<h2>Registro de Nueva Cuenta</h2> ");
		out.println("		 Por favor diligencie todos los campos");
		out.println("		 <hr>");
		out.println("			<form role=\"form\" method=\"post\">");
		out.println("				<div class=\"col-lg-6\">");
		out.println("				");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Correo Electronico</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"email\" class=\"form-control\" id=\"correo\" name=\"correo\" placeholder=\"Ingresar Correo\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("						(El correo sera usado para ingresar al sistema)");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Nombre</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"nombre\" name=\"nombre\" placeholder=\"Ingresar Nombre\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Contrasenia</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"password\" class=\"form-control\" id=\"nombre\" name=\"nombre\" placeholder=\"Ingresar Contrasenia\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Confirmar Contrasenia</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"password\" class=\"form-control\" id=\"nombre\" name=\"nombre\" placeholder=\"Confirmar Contrasenia\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					</div>");
		out.println("					<div class=\"col-lg-6\">");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Nacionalidad</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<select name=\"Country\" class=\"form-control\" id=\"country-selector\" autofocus=\"autofocus\" autocorrect=\"off\" autocomplete=\"off\" required>");
		out.println("						<option>Hnnnnnhnh</option>");											
		// Archivo de texto para escribir paises.
		out.println("							</select>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Departamento</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"depto\" name=\"depto\" placeholder=\"Ingresar Departamento\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Ciudad</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"ciudad\" name=\"ciudad\" placeholder=\"Ingresar Ciudad\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Direccion</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"dir\" name=\"dir\" placeholder=\"Ingresar Direccion\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Codigo Postal</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"number\" class=\"form-control\" id=\"codpostal\" name=\"codpostal\" placeholder=\"Ingresar Codigo Postal\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Telefono</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"number\" class=\"form-control\" id=\"tel\" name=\"tel\" placeholder=\"Ingresar Telefono\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Id Representante</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"idRep\" name=\"idRep\" placeholder=\"Ingresar Id Representante\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Nombre Representante</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"nomRep\" name=\"nomRep\" placeholder=\"Ingresar Nombre Representante\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Siguiente\" class=\"btn btn-info pull-right\" formaction=\"registroTipoUsuario.htm\">");
		out.println("				</div>");
		out.println("			</form>");
		out.println("		</div>");
		out.println("	</div>");
		out.println("</div>");
	}
	
	public void escribirErrorYaExis(PrintWriter out){
		
	}
	
	public void escribirErrorCont(PrintWriter out){
		
	}

}
