package valorAndes.vos;

import java.util.ArrayList;

public class InversionistaValue {
	//----------------------------------
	//Atributos
	//----------------------------------
	/**
	 * Documento de identidad.
	 */
	private int docIdentidad;

	/**
	 * Valores que tiene en la bolsa
	 */
	private ArrayList<String[]> valores;
	
	/**
	 * Portafolios del inversionista
	 */
	private ArrayList<InPortafolioValue> inPortafolios;
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
	private int telefono;

	/**
	 * El tipo de portafolio que maneja
	 */
	private String tipoPortafolio;
	
	/**
	 * @return the docIdentidad
	 */
	public int getDocIdentidad() {
		return docIdentidad;
	}

	/**
	 * @param docIdentidad the docIdentidad to set
	 */
	public void setDocIdentidad(int docIdentidad) {
		this.docIdentidad = docIdentidad;
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
	public int getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono the telefono to set
	 */
	public void setTelefono(int telefono) {
		this.telefono = telefono;
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

	/**
	 * @return the inPortafolios
	 */
	public ArrayList<InPortafolioValue> getInPortafolios() {
		return inPortafolios;
	}

	/**
	 * @param inPortafolios the inPortafolios to set
	 */
	public void setInPortafolios(ArrayList<InPortafolioValue> inPortafolios) {
		this.inPortafolios = inPortafolios;
	}
}
