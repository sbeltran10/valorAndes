package valorAndes.vos;

import java.util.ArrayList;

public class OferenteValue {
	//-------------------------------
	//Atributos
	//-------------------------------
	/**
	 * Atributo del tipo de Entidad.
	 */
	private String tipoEntidad;

	/**
	 * Id de rentabilidad.
	 */
	private int idRentabilidad;

	/**
	 * Los valores que el oferente tiene en la bolsa
	 */
	private ArrayList<String[]> valores;
	
	//----------------------------------------
	//Atributos de la super-clase Usuario
	//----------------------------------------
	/**
	 * El correo.
	 */
	private String correo;

	/**
	 * La ciudad actual de la entidad.
	 */
	private String ciudad;

	/**
	 * El codigo postal.
	 */
	private int codPostal;

	/**
	 * La contrasena de login.
	 */
	private String contrasenia;

	/**
	 * El departamento.
	 */
	private String departamento;

	/**
	 * La direccion de la ciudad.
	 */
	private String direccion;

	/**
	 * El id del representante legal.
	 */
	private String idRepresentante;

	/**
	 * La nacionalidad de la entidad.
	 */
	private String nacionalidad;

	/**
	 * El nombre de la entidad.
	 */
	private String nombre;

	/**
	 * El nombre del representante legal.
	 */
	private String nombreRepresentante;

	/**
	 * El telefono.
	 */
	private String telefono;

	/**
	 * El tipo de portafolio que maneja
	 */
	private String tipoPortafolio;
	
	/**
	 * @return the tipoEntidad
	 */
	public String getTipoEntidad() {
		return tipoEntidad;
	}

	/**
	 * @param tipoEntidad the tipoEntidad to set
	 */
	public void setTipoEntidad(String tipoEntidad) {
		this.tipoEntidad = tipoEntidad;
	}

	/**
	 * @return the idRentabilidad
	 */
	public int getIdRentabilidad() {
		return idRentabilidad;
	}

	/**
	 * @param idRentabilidad the idRentabilidad to set
	 */
	public void setIdRentabilidad(int idRentabilidad) {
		this.idRentabilidad = idRentabilidad;
	}

	/**
	 * @return the correo
	 */
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo the correo to set
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}

	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	/**
	 * @return the codPostal
	 */
	public int getCodPostal() {
		return codPostal;
	}

	/**
	 * @param codPostal the codPostal to set
	 */
	public void setCodPostal(int codPostal) {
		this.codPostal = codPostal;
	}

	/**
	 * @return the contrasenia
	 */
	public String getContrasenia() {
		return contrasenia;
	}

	/**
	 * @param contrasenia the contrasenia to set
	 */
	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}

	/**
	 * @return the departamento
	 */
	public String getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento the departamento to set
	 */
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return the idRepresentante
	 */
	public String getIdRepresentante() {
		return idRepresentante;
	}

	/**
	 * @param idRepresentante the idRepresentante to set
	 */
	public void setIdRepresentante(String idRepresentante) {
		this.idRepresentante = idRepresentante;
	}

	/**
	 * @return the nacionalidad
	 */
	public String getNacionalidad() {
		return nacionalidad;
	}

	/**
	 * @param nacionalidad the nacionalidad to set
	 */
	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
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
	 * @return the nombreRepresentante
	 */
	public String getNombreRepresentante() {
		return nombreRepresentante;
	}

	/**
	 * @param nombreRepresentante the nombreRepresentante to set
	 */
	public void setNombreRepresentante(String nombreRepresentante) {
		this.nombreRepresentante = nombreRepresentante;
	}

	/**
	 * @return the telefono
	 */
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono2 the telefono to set
	 */
	public void setTelefono(String telefono2) {
		this.telefono = telefono2;
	}

	/**
	 * @return el tipo de portafolio
	 */
	public String getTipoPortafolio() {
		return tipoPortafolio;
	}

	/**
	 * @param tipoPortafolio to set
	 */
	public void setTipoPortafolio(String tipoPortafolio) {
		this.tipoPortafolio = tipoPortafolio;
	}

	public ArrayList<String[]> getValores() {
		return valores;
	}

	public void setValores(ArrayList<String[]> valores) {
		this.valores = valores;
	}

}