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

	/**
	 * Nombre del portafolio
	 */
	private String nombre;
	
	/**
	 * Id del portafolio
	 */
	private int id;
	
	/**
	 * Correo del intermediario asociado al portafolio
	 */
	private String correoInter;
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

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the correoInter
	 */
	public String getCorreoInter() {
		return correoInter;
	}

	/**
	 * @param correoInter the correoInter to set
	 */
	public void setCorreoInter(String correoInter) {
		this.correoInter = correoInter;
	}
			
}
