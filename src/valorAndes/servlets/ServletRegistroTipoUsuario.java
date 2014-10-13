package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletRegistroTipoUsuario extends ServletTemplate{
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
		out.println("		<br><br>");
		out.println("		<h2> Seleccion de tipo de usuario</h2>");
		out.println("		A continuacion seleccione el tipo de usuario como el cual se registrara");
		out.println("		<div class=\"row\">");
		out.println("        <div class=\"col-xs-12 col-sm-12 col-md-4 col-lg-4\">");
		out.println("            <div class=\"listing\">");
		out.println("                <div class=\"listing-content\">");
		out.println("                    <h3 class=\"lead\"><b>Oferente/Empresa</b></h3>");
		out.println("                    <p>");
		out.println("					- Crea valores<br>");
		out.println("					- Puede realizar consultas sobre valores y operaciones<br>");
		out.println("					- Puede ordenar operaciones bursatiles<br>");
		out.println("					- Puede cancelar operaciones bursatiles<br>");
		out.println("					- Maneja tipos de rentabilidad<br>");
		out.println("					- Maneja tipos de valor<br>");
		out.println("					- Tiene asociado un intermediario</p>");
		out.println("					<a href=\"registroOferente.html\" class=\"btn btn-info btn-lg pull-right\"><span class=\"network-name\">Registrar</span></a>");
		out.println("                </div>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("        <div class=\"col-xs-12 col-sm-12 col-md-4 col-lg-4\">");
		out.println("            <div class=\"listing\">");
		out.println("                <div class=\"listing-content\">");
		out.println("                    <h3 class=\"lead\"><b>Inversionista</b></h3>");
		out.println("                    <p>");
		out.println("					- Puede realizar consultas sobre valores y operaciones<br>");
		out.println("					- Puede ordenar operaciones bursatiles<br>");
		out.println("					- Puede cancelar operaciones bursatiles<br>");
		out.println("					- Maneja tipos de rentabilidad<br>");
		out.println("					- Maneja tipos de valor<br>");
		out.println("					- Tiene asociado uno o varios intermediarios<br>");
		out.println("					- Maneja portafolio de valores</p>");
		out.println("					<a href=\"registroInversionista.html\" class=\"btn btn-info btn-lg pull-right\"><span class=\"network-name\">Registrar</span></a>");
		out.println("                </div>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("        <div class=\"col-xs-12 col-sm-12 col-md-4 col-lg-4\">");
		out.println("            <div class=\"listing\">");
		out.println("                <div class=\"listing-content\">");
		out.println("                    <h3 class=\"lead\"><b>Intermediario</b></h3>");
		out.println("                    <p>- Puede realizar consultas sobre valores y operaciones<br>");
		out.println("					- Registra operaciones<br>");
		out.println("					- Tiene asociados varios oferentes o inversionistas<br>");
		out.println("					- Maneja portafolio de valores");
		out.println("					</p>");
		out.println("					<a href=\"registroIntermediario.html\" class=\"btn btn-info btn-lg pull-right\"><span class=\"network-name\">Registrar</span></a>");
		out.println("                </div>");
		out.println("            </div>");
		out.println("        </div>");
		out.println("		</div>");
		out.println("</div>");
		escribirEstilo(out);
	}
	
	public void escribirInver(PrintWriter out){
		out.println("<div class=\"well well-lg\">");
		out.println("    <div class=\"container\">");
		out.println("		<div class=\"row\"><br><br>");
		out.println("		<h2>Registro de Inversionista</h2> ");
		out.println("		 Por favor diligencie todos los campos");
		out.println("		 <hr>");
		out.println("			<form role=\"form\">");
		out.println("				<div class=\"col-lg-6\">");
		out.println("				");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Debe seleccionar un intermediario inical por medio del cual realizara operaciones en ValorAndes,");
		out.println("						mas tarde podra registrar mas intermediarios o cambiarlos si lo desea.</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<select class=\"form-control\" name=\"idInter\" required>");
		out.println("							<option></option>");
		out.println("							<option>---</option>");
		out.println("							</select> ");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Finalizar\" class=\"btn btn-info pull-right\">");
		out.println("				</div>");
		out.println("				</form>	");
		out.println("			</div>		");
		out.println("		</div>");
		out.println("	</div>	");

	}
	
	public void escribirOfer(PrintWriter out){
		out.println("<div class=\"well well-lg\">");
		out.println("    <div class=\"container\">");
		out.println("		<div class=\"row\"><br><br>");
		out.println("		<h2>Registro de Oferente/Empresa</h2> ");
		out.println("		 Por favor diligencie todos los campos");
		out.println("		 <hr>");
		out.println("			<form role=\"form\">");
		out.println("				<div class=\"col-lg-6\">");
		out.println("				");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Tipo de Entidad</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<select class=\"form-control\" name=\"ent\" required>");
		out.println("							<option></option>");
		out.println("							<option>Empresa Publica</option>");
		out.println("							<option>Empresa Privada</option>");
		out.println("							<option>Estado</option>");
		out.println("							</select> ");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Debe seleccionar un intermediario inical por medio del cual realizara operaciones en ValorAndes,");
		out.println("						mas tarde podra cambiar de intermediario si lo desea.</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<select class=\"form-control\" name=\"idInter\" required>");
		out.println("							<option></option>");
		out.println("							<option>---</option>");
		out.println("							</select> ");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Tipos de rentabilidad que maneja (Para seleccionar mas de un tipo mantenga presionada la tecla ctrl)</label>");
		out.println("						<div class=\"input-group\">");
		out.println("						<select multiple class = \"form-control\" name=\"tiposRent\" required>");
		out.println("						  <option value=\"volvo\">---</option>");
		out.println("						</select>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Tipos de valores que maneja (Para seleccionar mas de un tipo mantenga presionada la tecla ctrl)</label>");
		out.println("						<div class=\"input-group\">");
		out.println("						<select multiple class = \"form-control\" name=\"tiposVal\" required>");
		out.println("						  <option value=\"accion\">Acciones</option>");
		out.println("						  <option value=\"bonpub\">Bonos Publicos</option>");
		out.println("						  <option value=\"bonpriv\">Bonos Privados</option>");
		out.println("						  <option value=\"cert\">Certificados</option>");
		out.println("						  <option value=\"titu\">Titulos de Participacion</option>");
		out.println("						</select>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Finalizar\" class=\"btn btn-info pull-right\">");
		out.println("				</div>");
		out.println("				</form>	");
		out.println("			</div>		");
		out.println("		</div>");
		out.println("	</div>	");

	}
	
	public void escribirInter(PrintWriter out){
		out.println("<div class=\"well well-lg\">");
		out.println("    <div class=\"container\">");
		out.println("		<div class=\"row\"><br><br>");
		out.println("		<h2>Registro de Intermediario</h2> ");
		out.println("		 Por favor diligencie todos los campos");
		out.println("		 <hr>");
		out.println("			<form role=\"form\">");
		out.println("				<div class=\"col-lg-6\">");
		out.println("				");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Tipo de Entidad</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<select class=\"form-control\" name=\"ent\" required>");
		out.println("							<option></option>");
		out.println("							<option>Corredor de bolsa</option>");
		out.println("							<option>Casa de bolsa</option>");
		out.println("							<option>Agente de valores</option>");
		out.println("							</select> ");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<div class=\"form-group\">");
		out.println("						<label for=\"InputEmail\">Numero de registro ante la Superintendencia Nacional de Valores</label>");
		out.println("						<div class=\"input-group\">");
		out.println("							<input type=\"text\" class=\"form-control\" id=\"numregistro\" name=\"numregistro\" placeholder=\"Ingresar numero\" required>");
		out.println("							<span class=\"input-group-addon\"></span>");
		out.println("						</div>");
		out.println("					</div>");
		out.println("					<input type=\"submit\" name=\"submit\" id=\"submit\" value=\"Finalizar\" class=\"btn btn-info pull-right\">");
		out.println("				</div>");
		out.println("				</form>	");
		out.println("			</div>		");
		out.println("		</div>");
		out.println("	</div>	");
	}
	
	public void escribirEstilo(PrintWriter out){
		out.println("<style>");
		out.println("");
		out.println(".listing {");
		out.println("    background: #fff;");
		out.println("    border: 1px solid #ddd;");
		out.println("    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.2);");
		out.println("    margin: 15px 0;");
		out.println("    overflow: hidden;");
		out.println("}");
		out.println(".listing:hover {");
		out.println("    -webkit-transform: scale(1.1);");
		out.println("    -moz-transform: scale(1.1);");
		out.println("    -ms-transform: scale(1.1);");
		out.println("    -o-transform: scale(1.1);");
		out.println("    transform: rotate scale(1.1);");
		out.println("    -webkit-transition: all 0.4s ease-in-out;");
		out.println("    -moz-transition: all 0.4s ease-in-out;");
		out.println("    -o-transition: all 0.4s ease-in-out;");
		out.println("    transition: all 0.4s ease-in-out;");
		out.println("}");
		out.println("");
		out.println("");
		out.println(".listing-content {");
		out.println("    padding: 0 20px 10px;");
		out.println("}");
		out.println("</style>");
		out.println("</body>");
		out.println("");
		out.println("");
		out.println("");
		out.println("</html>");
	}
	
}
