package valorAndes.vos;

import java.util.Date;

public class OperacionValue {
	//------------------------------------
	//Atributos
	//------------------------------------
	/**
	 * Cantidad a negociar.
	 */
	private int cantidad;
	
	/**
	 * Fecha de Orden.
	 */
	private Date fecha;
	
	/**
	 * Precio.
	 */
	private int precio;
	
	/**
	 * Tipo que determina si es una compra o una venta.
	 */
	private String tipoCompraVenta;
	
	/**
	 * Id de la operacion.
	 */
	private int id;
	
	/**
	 * Correo del solicitante.
	 */
	private String corSolicitante;
	
	/**
	 * Correo del intermediario al cuals e le asigno la operacion.
	 */
	private String corIntermediario;
	
	/**
	 * Id del valor.
	 */
	private String idValor;

	/**
	 * Nombre del valor
	 */
	private String nomValor;
	
	
	/**
	 * @return the cantidad
	 */
	public int getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the fecha
	 */
	public Date getFecha() {
		return fecha;
	}

	/**
	 * @param date the fecha to set
	 */
	public void setFecha(Date date) {
		this.fecha = date;
	}

	/**
	 * @return the precioBase
	 */
	public int getPrecio() {
		return precio;
	}

	/**
	 * @param precioBase the precioBase to set
	 */
	public void setPrecio(int precio) {
		this.precio = precio;
	}

	/**
	 * @return the tipoCompraVenta
	 */
	public String getTipoCompraVenta() {
		return tipoCompraVenta;
	}

	/**
	 * @param tipoCompraVenta the tipoCompraVenta to set
	 */
	public void setTipoCompraVenta(String tipoCompraVenta) {
		this.tipoCompraVenta = tipoCompraVenta;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Id del valor
	 */
	public String getNomValor(){
		return nomValor;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the corInversionista
	 */
	public String getCorSolicitante() {
		return corSolicitante;
	}

	/**
	 * @param corInversionista the corInversionista to set
	 */
	public void setCorSolicitante(String corSolicitante) {
		this.corSolicitante = corSolicitante;
	}
	
	/**
	 * @return the corIntermediario
	 */
	public String getCorIntermediario() {
		return corIntermediario;
	}

	/**
	 * @param corOferente the corOferente to set
	 */
	public void setCorIntermediario(String corIntermediario) {
		this.corIntermediario = corIntermediario;
	}

	/**
	 * @return the idValor
	 */
	public String getIdValor() {
		return idValor;
	}
	
	/**
	 * set nomValor
	 */
	public void setNomValor(String string){
		nomValor = string;
	}

	/**
	 * @param string the idValor to set
	 */
	public void setIdValor(String string) {
		this.idValor = string;
	}
	
	/**
	 * ToString
	 */
	public String toString(){
		return cantidad+";"+fecha.toString()+";"+precio+";"+tipoCompraVenta+";"+corSolicitante+";"+corIntermediario+";"+nomValor;
	}
}
