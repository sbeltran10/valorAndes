package valorAndes.servlets;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletPortafolioIntermediario extends ServletTemplate{
	private static final long serialVersionUID = 1L;

	@Override
	public String darTituloPagina(HttpServletRequest request) {
		return "Portafolio";
	}

	@Override
	public void escribirContenido(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
