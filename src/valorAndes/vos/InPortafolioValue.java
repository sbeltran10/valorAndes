package valorAndes.vos;

import java.util.ArrayList;

public class InPortafolioValue {
	//------------------------------
	//Atributos
	//------------------------------
	/**
	 * Lista de valores y sus porcentajes de inversion.
	 */
	
	private ArrayList<ValorPorcentajeInversionValue> valoresPortafolio;
	
	/**
	 * Portafolio del que inPortafolio es subconjunto.
	 */
	
	private PortafolioValue parentPortafolio;

	//------------------------------
	//Metodos
	//------------------------------
		
	/**
	 * @return the valoresPortafolio
	 */
	public ArrayList<ValorPorcentajeInversionValue> getValoresPortafolio() {
		return valoresPortafolio;
	}

	/**
	 * @param valoresPortafolio the valoresPortafolio to set
	 */
	public void setValoresPortafolio(
			ArrayList<ValorPorcentajeInversionValue> valoresPortafolio) {
		this.valoresPortafolio = valoresPortafolio;
	}

	/**
	 * @return the parentPortafolio
	 */
	public PortafolioValue getParentPortafolio() {
		return parentPortafolio;
	}

	/**
	 * @param parentPortafolio the parentPortafolio to set
	 */
	public void setParentPortafolio(PortafolioValue parentPortafolio) {
		this.parentPortafolio = parentPortafolio;
	}
	
}
