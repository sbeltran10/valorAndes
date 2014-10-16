package valorAndes.vos;

public class ValorPorcentajeInversionValue {

	//--------------------------------------
	//Atributos.
	//--------------------------------------
	/**
	 * Porcentaje de inversion.
	 */
	private int porcentajeInversion;
	
	/**
	 * Valor.
	 */
	private ValorValue valor;

	//--------------------------------------
	//Metodos.
	//--------------------------------------
	
	/**
	 * @return the valor
	 */
	public ValorValue getValor() {
		return valor;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(ValorValue valor) {
		this.valor = valor;
	}

	/**
	 * @return the porcentajeInversion
	 */
	public int getPorcentajeInversion() {
		return porcentajeInversion;
	}

	/**
	 * @param porcentajeInversion the porcentajeInversion to set
	 */
	public void setPorcentajeInversion(int porcentajeInversion) {
		this.porcentajeInversion = porcentajeInversion;
	}
}
