package valorAndes.vos;

import java.util.Date;

public class ValorValue {
	//------------------------------------
	//Atributos
	//------------------------------------
	
	/**
	 * Codigo del Valor
	 */
	private int codigo;
	
	/**
	 * Disponible
	 */
	private boolean disponible;
	
	/**
	 * Fecha de expiracion
	 */
	private Date fechaExpiracion;
	
	/**
	 * Tipo de mercado "PRIMARIO" || "SECUNDARIO"
	 */
	private String mercado;
	
	/**
	 * Nombre
	 */
	private String nombre;
	
	/**
	 * Precio
	 */
	private int precio;
	
	/**
	 * Correo creador.
	 */
	private String creador;
	
	/**
	 * Nombre Creador
	 */
	private String nombreCreador;
	
	/**
	 * nombre rentabilidad.
	 */
	private String rentabilidad;
	
	/**
	 * nombre de tipo Valor.
	 */
	private String nomTipoValor;	
	
	/**
	 * @return el codigo del valor
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo del valor
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the disponible
	 */
	public boolean isDisponible() {
		return disponible;
	}



	/**
	 * @param disponible the disponible to set
	 */
	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}



	/**
	 * @return the fechaExpiracion
	 */
	public Date getFechaExpiracion() {
		return fechaExpiracion;
	}



	/**
	 * @param fecha_expiracion the fechaExpiracion to set
	 */
	public void setFechaExpiracion(Date fecha_expiracion) {
		this.fechaExpiracion = fecha_expiracion;
	}



	/**
	 * @return the mercado
	 */
	public String getMercado() {
		return mercado;
	}



	/**
	 * @param mercado the mercado to set
	 */
	public void setMercado(String mercado) {
		this.mercado = mercado;
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
	 * @return the precio
	 */
	public int getPrecio() {
		return precio;
	}



	/**
	 * @param precio the precio to set
	 */
	public void setPrecio(int precio) {
		this.precio = precio;
	}

	/**
	 * @return the creador
	 */
	public String getCreador() {
		return creador;
	}

	/**
	 * @return nombre del creador del valor
	 */
	public String getNombreCreador() {
		return nombreCreador;
	}

	/**
	 * @param nombreCreador a fijar
	 */
	public void setNombreCreador(String nombreCreador) {
		this.nombreCreador = nombreCreador;
	}

	/**
	 * @param creador the creador to set
	 */
	public void setCreador(String creador) {
		this.creador = creador;
	}



	/**
	 * @return the rentabilidad
	 */
	public String getRentabilidad() {
		return rentabilidad;
	}



	/**
	 * @param rentabilidad the rentabilidad to set
	 */
	public void setRentabilidad(String rentabilidad) {
		this.rentabilidad = rentabilidad;
	}



	/**
	 * @return the nomTipoValor
	 */
	public String getNomTipoValor() {
		return nomTipoValor;
	}



	/**
	 * @param nomTipoValor the nomTipoValor to set
	 */
	public void setNomTipoValor(String nomTipoValor) {
		this.nomTipoValor = nomTipoValor;
	}
}
