package valorAndes.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import valorAndes.fachada.ValorAndes;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.InversionistaValue;

public class ServletPortafolioInversionista extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		// TODO Auto-generated method stub
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		InversionistaValue inver = ValorAndes.getInstance().getInver();

		PrintWriter out = response.getWriter();
		
	}

}
