package valorAndes.vos;

import java.util.ArrayList;

public class PortafolioValue {
	
	//--------------------------------------------
	//Atributos
	//--------------------------------------------
	/**
	 * Tipo de riesgo
	 */
	private String tipoRiesgo;
	
	/**
	 * Lista de valores asociados al portafolio.
	 */
	private ArrayList<ValorValue> valores;

	//--------------------------------------------
	//Metodos Value
	//--------------------------------------------

	/**
	 * @return the tipoRiesgo
	 */
	public String getTipoRiesgo() {
		return tipoRiesgo;
	}

	/**
	 * @param tipoRiesgo the tipoRiesgo to set
	 */
	public void setTipoRiesgo(String tipoRiesgo) {
		this.tipoRiesgo = tipoRiesgo;
	}

	/**
	 * @return the valores
	 */
	public ArrayList<ValorValue> getValores() {
		return valores;
	}

	/**
	 * @param valores the valores to set
	 */
	public void setValores(ArrayList<ValorValue> valores) {
		this.valores = valores;
	}
			
}
