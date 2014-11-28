package valorAndes.fachada;
import java.sql.SQLException;
import java.util.ArrayList;

import valorAndes.dao.ConsultaDAO;
import valorAndes.vos.InPortafolioValue;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;
import valorAndes.vos.OperacionValue;
import valorAndes.vos.PortafolioValue;
import valorAndes.vos.ValorPorcentajeInversionValue;
import valorAndes.vos.ValorValue;

/**
 * Clase ValorAndes, representa la fachada de comunicacion entre la interfaz y la conexion con la base de datos, maneja todas las solicitudes.
 * @author Samuel
 */
public class ValorAndes {

	private final static String ADMIN = "admin";

	/**
	 * Conexion con la clase que maneja la base de datos
	 */
	private ConsultaDAO dao;

	/**
	 * Tipo de usuario que se encuentra logeado en el sistema
	 */
	private int logeado;

	private IntermediarioValue interLogeado;

	private InversionistaValue inverLogeado;

	private OferenteValue oferLogeado;

	//--------------------------
	//Instance
	//--------------------------
	/**
	 * Instancia de la clase.
	 */
	private static ValorAndes instance;

	/**
	 * Devuelve la instancia unica de la clase.
	 */
	public static ValorAndes getInstance(){
		if(instance == null){
			instance = new ValorAndes();
		}
		return instance;
	}

	private ValorAndes(){
		dao = new ConsultaDAO();
		logeado=0;
	}

	public void inicializarRuta(String path){
		dao.inicializar(path);
	}

	//---------------------------------------------
	//Metodos asosciados a autenticacion
	//---------------------------------------------

	/**
	 * Maneja el login de un usuario al sistema de valorandes
	 * @param usuario
	 * @return
	 */
	public boolean login(String correo, String contrasenia){
		Object usuario = null;
		try {
			usuario = dao.login(correo, contrasenia);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		boolean log = true;

		if(usuario==null)
			log = false;

		else if(usuario.getClass().getSimpleName().equals("IntermediarioValue")){
			logeado=1;
			interLogeado = (IntermediarioValue) usuario;
		}

		else if(usuario.getClass().getSimpleName().equals("InversionistaValue")){
			logeado=2;
			inverLogeado = (InversionistaValue) usuario;
		}

		else if(usuario.getClass().getSimpleName().equals("OferenteValue")){
			logeado=3;
			oferLogeado = (OferenteValue) usuario;
		}
		return log;
	}


	/**
	 * Maneja el login del admin al sistema de valorandes
	 * @param usuario
	 * @return
	 */
	public boolean adminLogin(String log, String pass){
		if(log.equals(ADMIN))
			if(pass.equals(ADMIN)){
				logeado=4;
				return true;
			}

		return false;
	}
	/**
	 * Maneja el logout de un usuario
	 */
	public void logout(){
		logeado=0;
		interLogeado=null;
		inverLogeado=null;
		oferLogeado=null;
	}

	/**
	 * Retorna el estado del usuario
	 */
	public int getLogeado(){
		return logeado;
	}

	public IntermediarioValue getInter(){
		return interLogeado;
	}

	public InversionistaValue getInver(){
		return inverLogeado;
	}

	public OferenteValue getOfer(){
		return oferLogeado;
	}
	//---------------------------------------------
	//Metodos asociados a las consultas.
	//---------------------------------------------
	/**
	 * Metodo que retorna los valores filtrados segun los parametros, donde el ultimo de estos es el factor de orden.
	 * @throws SQLException
	 */
	public ArrayList<ValorValue> darExistenciaDeValores(String valor, String tipoValor, String tipoRentabilidad, String negociado, String fechaExpiracion, String corOferenteDuenio, String corIntermediario, String corInversionista, String orden) throws SQLException{
		return dao.existenciaValores(valor, tipoValor, tipoRentabilidad, negociado, fechaExpiracion, corOferenteDuenio, corIntermediario, corInversionista, orden);
	}

	/**
	 * Metodo que retorna las operaciones filtradas por tipo de usuario y filtradas y ordenadas segun los parametros 
	 * @throws Exception 
	 */
	public ArrayList<OperacionValue> darOperaciones(String tipoUsuario, String tipoOperacion, String fechaMenor, String fechaMayor, int precio, String valor, String tipoRentabilidad) throws Exception{
		return dao.consultarOperaciones(tipoUsuario, tipoOperacion, fechaMenor, fechaMayor, precio, valor, tipoRentabilidad);
	}
	
	/**
	 * Retorna los usuarios del sistema dados unos parametros
	 * @throws SQLException 
	 */
	public ArrayList<String[]> darUsuarios(String tipoUs, String correo, String nombre, String telefono, String pais, String ciudad, String idRepresentante) throws SQLException{
		return dao.consultaUsuarios(tipoUs, correo, nombre, telefono, pais, ciudad, idRepresentante);
	}

	//--------------------------------------------------
	//Metodos Asociados a Operaciones.
	//-------------------------------------------------
	/**
	 * Ordenar Operacion Bursatil
	 */
	public boolean ordenarOperacion(OperacionValue op, String codIntermediario) throws Exception{
		return dao.ordenarOperacion(op, codIntermediario);
	}
	/**
	 * Registrar Operacon Bursatil
	 */
	public String registrarOperacion(OperacionValue idOperacion) throws Exception{
		return dao.registrarOperacionBursatil(idOperacion);
	}
	/**
	 * Cancelar Operacion Bursatil
	 */
	public boolean cancelarOperacion(String idUser, int idOperacion) throws Exception{
		return dao.cancelarOperacionBursatil(idUser, idOperacion);
	}
	

	//--------------------------------------------------
	//Metodos Asociados a Relleno de formularios y/o tablas.
	//-------------------------------------------------
	/**
	 * Da los tipos de rentabilidad existentes en la base de datos
	 * @throws SQLException 
	 */
	public ArrayList<String> darTiposRent() throws SQLException{
		return dao.darTiposDeRentabilidad();
	}

	/**
	 * Da los tipos de rentabilidad existentes en la base de datos
	 * @throws SQLException 
	 */
	public ArrayList<String> darTiposVal() throws SQLException{
		return dao.darTiposDeValores();
	}

	/**
	 * Da el nombre y el correo de los intermediarios asociados al usuario
	 * @throws SQLException 
	 */
	public ArrayList<String> darIntermediariosmios(String correo) throws SQLException{
		return dao.darIntermediariosmios(correo);
	}

	/**
	 * Da los ids y nombres de los valores existentes en la base de datos
	 * @throws SQLException 
	 */
	public ArrayList<String> darIdValoresVenta(String correoUs) throws SQLException{
		return dao.darValoresVenta(correoUs);
	}

	/**
	 * Da los ids y nombres de los valores existentes en la base de datos
	 * @throws SQLException 
	 */
	public ArrayList<String> darIdValoresCompra(String correoUs) throws SQLException{
		return dao.darValoresCompra(correoUs);
	}

	/**
	 * Da los ids y nombres de los valores existentes en la base de datos
	 * @throws SQLException 
	 */
	public ArrayList<OperacionValue> darOperacionesMias(String correo) throws SQLException{
		return dao.darOperacionesMias(correo);
	}
	
	/**
	 * Da la informacion de los valores que vende un intermediario
	 * @throws SQLException 
	 */
	public ArrayList<String> darValoresInterVenta(String correo) throws SQLException{
		return dao.darValoresVentaIntermediario(correo);
	}

	//---------------------------------
	// Visualizacion Usuarios
	//---------------------------------
	
	/**
	 * Da la informacion de un oferente
	 * @throws SQLException 
	 */
	public OferenteValue darInfoOferente(String correo) throws SQLException{
		return dao.darInfoOferente(correo);
	}
	
	/**
	 * Da la informacion de un inversionista
	 * @throws SQLException 
	 */
	public InversionistaValue darInfoInversionista(String correo) throws SQLException{
		return dao.darInfoInversionista(correo);
	}
	
	/**
	 * Da la informacion de un intermediario
	 * @throws SQLException 
	 */
	public IntermediarioValue darInfoIntermediario(String correo) throws SQLException{
		return dao.darInfoIntermediario(correo);
	}

	//---------------------------------------
	//Retirar Intermediario
	//---------------------------------------
	
	/**
	 * Da todos los intermediarios de la bolsa de valores
	 * @throws SQLException 
	 */
	public ArrayList<IntermediarioValue> darTodosIntermediarios() throws SQLException{
		return dao.darTodosIntermediarios();
	}
	
	/**
	 * Retirar a un intermediario de la bolsa de valores, reemplazandolo por otro
	 * @throws SQLException 
	 */
	public void retirarIntermediarioGlobal(String intRetirado, String intAsociado) throws Exception{
		dao.retirarIntermediarioGlobal(intRetirado, intAsociado);
	}
	//----------------------------------------
	//Portafolios Intermediario
	//----------------------------------------
	/**
	 * Da todos los portafolios que tiene un intermediario
	 * @param correo
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<PortafolioValue> darPortafoliosIntermediario(String correo) throws SQLException{
		return dao.darPortafoliosIntermediario(correo);
	}
	
	/**
	 * Crea un nuevo portafolio
	 * @throws SQLException 
	 */
	public void crearPortafolio(String codIntermediario, String nombrePortafolio, String nivelRiesgo) throws SQLException{
		dao.crearPortafolio(codIntermediario, nombrePortafolio, nivelRiesgo);
	}
	
	/**
	 * Anade un valor al portafolio dado por parametro
	 * @param codPortafolio
	 * @param codValor
	 * @throws SQLException
	 */
	public void anadirValorAPortafolio(int codPortafolio, int codValor) throws SQLException{
		dao.anadirValorAPortafolio(codPortafolio, codValor);
	}
	
	/**
	 * Elimina un valor del portafolio
	 * @throws SQLException 
	 */
	public void eliminarValorPortafolio(int codPortafolio, int codValor) throws SQLException{
		dao.eliminarValorDeProtafolio(codPortafolio, codValor);
	}
	/**
	 * Da los valores que hay en el portafolio elegido del intermediario
	 * @throws SQLException 
	 */
	public ArrayList<ValorValue> darValoresPortafolio(int idPortafolio, String corIntermediario) throws SQLException{
		return dao.darValoresPortafolio(idPortafolio, corIntermediario);
	}
	
	//--------------------------------
	//Portafolios Inversionista
	//--------------------------------
	/**
	 * Crea un nuevo InPortafolio
	 * @throws SQLException 
	 */
	public void crearInPortafolio(String corInver, int codPorta, String nomPorta, int cantidad) throws SQLException{
		dao.crearInPortafolio(corInver, codPorta, nomPorta, cantidad);
	}
	
	/**
	 * Da todos los InPortafolios de un inversionista
	 * @throws SQLException 
	 */
	public ArrayList<InPortafolioValue> darInPortafolios(String correoInv) throws SQLException{
		return dao.darInPortafolios(correoInv);
	}
	
	/**
	 * Cambia el porcentaje del valor en el in portafolio
	 * @throws SQLException 
	 */
	public void cambiarInPorcentaje(String codInv, int codPortafolio, int codVal, int nPor) throws SQLException{
		dao.cambiarInPorcentaje(codInv, codPortafolio, codVal, nPor);
	}
	
	/**
	 * Elimina un valor de un inportafolio
	 * @throws SQLException 
	 */
	public void eliminarValorInPortafolio(String codInv, int codPortafolio, int codVal) throws SQLException{
		dao.eliminarValorInPortafolio(codInv, codPortafolio, codVal);
	}
	
	/**
	 * Agrega un nuevo valor al InPortafolio
	 * @throws SQLException 
	 */
	public void agregarValorInPortafolio(String codInv, int codPortafolio, int codVal) throws SQLException{
		dao.agregarValorInPortafolio(codInv, codPortafolio, codVal);
	}
	
	/**
	 * Da los valores de un inportafolio
	 * @throws SQLException 
	 */
	public ArrayList<ValorPorcentajeInversionValue> darValoresInPortafolio(String codInv, int codPortafolio) throws SQLException{
		return dao.darValoresInPortafolio(codInv, codPortafolio);
	}
	
	//---------------------------------------
	//Iteracion 4
	//---------------------------------------
	
	/**
	 * Retorna los movimientos de un valor teniendo en cuenta unos parametros
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param incluirFiltros
	 * @param nomValor
	 * @param tipoValor
	 * @param tipoRentabilidad
	 * @param tipoOperacion
	 * @param correoOfInv
	 * @param correoIntermediario
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<OperacionValue> consultarMGlobal(String fechaInicial, String fechaFinal, boolean incluirFiltros, String nomValor, String tipoValor, String tipoRentabilidad,
			String tipoOperacion, String correoOfInv, String correoIntermediario) throws Exception{
		return dao.consultarMGlobal(fechaInicial, fechaFinal, incluirFiltros, nomValor, tipoValor, tipoRentabilidad, tipoOperacion, correoOfInv, correoIntermediario);
	}
	
	/**
	 * Retorna los portafolios que cumplan con el tipo de valor y una cantidad
	 * @param tipoValor
	 * @param valorMayor
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<PortafolioValue> consultarPortafolios(String tipoValor, int valorMayor) throws SQLException{
		return dao.consultarPortafolios(tipoValor, valorMayor);
	}
	
	/**
	 * Consulta los portafolios en donde a estado un valor
	 * @param idValor
	 * @return
	 * @throws SQLException
	 */
	public ArrayList<PortafolioValue> consultarValorAlt(String idValor) throws SQLException{
		return dao.consultarValorAlt(idValor);
	}
	
	/**
	 * Consultar valores dinamicos
	 */
	public ArrayList<ValorValue> valoresMDinamicos(String fechaInicial, String fechaFinal)throws Exception{
		return dao.valoresMDinamicos(fechaInicial, fechaFinal);
	}
}
