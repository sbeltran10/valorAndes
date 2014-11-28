package valorAndes.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import oracle.sql.DATE;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import valorAndes.vos.InPortafolioValue;
import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;
import valorAndes.vos.OperacionValue;
import valorAndes.vos.PortafolioValue;
import valorAndes.vos.ValorPorcentajeInversionValue;
import valorAndes.vos.ValorValue;

public class ConsultaDAO {
	//------------------------------------------
	//Atributos
	//------------------------------------------
	/**
	 * Ruta donde se encuentra el archivo de conexion.
	 */
	private static final String ARCHIVO_CONEXION = "/conexion.properties";

	/**
	 * Conexion con la base de datos.
	 */
	private java.sql.Connection conexion;

	/**
	 * Nombre de Usuario.
	 */
	private String usuario;

	/**
	 * Contrasenaa de conexion.
	 */
	private String clave;

	/**
	 * URL al cual se debe conectar la base de datos.
	 */
	private String cadenaConexion;

	//------------------------------------------
	//Atributos JMSSender
	//------------------------------------------
	/**
	 * Conexion con el JavaMessageService.
	 */
	private javax.jms.Connection conexionJMS;

	/**
	 * ConnectionFactory.
	 */
	private ConnectionFactory conFactory;

	/**
	 * JMS Session.
	 */
	private Session session;

	/**
	 * JSM Destination.
	 */
	private Destination dest;

	/**
	 * JMS MessageConsumer.
	 */
	private MessageConsumer consumer;

	/**
	 * Resultado del requerimiento funcional de consulta 12 global.
	 */
	private ArrayList<OperacionValue> rfc12Result;

	/**
	 * Resultado del requerimiento funcional de consulta 13 global.
	 */
	private ArrayList<ValorValue> rfc13Result;
	

	/**
	 * Atributos para manejar los resultados de transacciones globales.
	 */
	private String RF14;
	private String RF15;
	private String RFC12;
	private String RFC13;

	/**
	 * Constructor de la clase.
	 */
	public ConsultaDAO(){
		rfc12Result = null;
		rfc13Result = null;
		RF14 = null;
		RF15 = null;
		RFC12 = null;
		RFC13 = null;
//		try {
//			InitialContext init = new InitialContext();
//			conFactory = (ConnectionFactory) init.lookup("RemoteConnectionFactory");
//			dest = (Destination) init.lookup("queue/testCola");
//			conexionJMS = (Connection) conFactory.createConnection("sistrans", "grupo6");
//			((javax.jms.Connection)conexionJMS).start();
//			session = ((javax.jms.Connection)conexionJMS).createSession(false, Session.AUTO_ACKNOWLEDGE);
//			consumer = this.session.createConsumer(dest);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	//------------------------------------------
	//Metodos
	//------------------------------------------
	/**
	 * Obtiene los datos necesarios para establecer una conexion
	 * Los datos se obtienen a partir de un archivo properties.
	 * @param path ruta donde se encuentra el archivo properties.
	 */
	public void inicializar(String path){
		try{
			File file = new File (path+ARCHIVO_CONEXION);
			Properties prop = new Properties();
			FileInputStream in = new FileInputStream(file);

			prop.load(in);
			in.close();

			cadenaConexion = prop.getProperty("url");

			usuario = prop.getProperty("usuario");
			clave = prop.getProperty("clave");
			final String driver = prop.getProperty("driver");
			Class.forName(driver);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Metodo que se encarga de crear la conexion con el driver manager
	 * a partir de los parametros recibidos.
	 * @param url direccion url de la base de datos a la que se desea conectar.
	 * @param usuario nombre del usuario que se va a conectar a la base de datos.
	 * @param clave clave de acceso a la base de datos.
	 * @throws SQLException si ocurre un error generando la conexion con la base de datos.
	 */
	private void establecerConexion(String url, String usuario, String clave)throws SQLException{
		try{
			conexion = DriverManager.getConnection(url, usuario, clave);
		}catch(SQLException e){
			throw new SQLException ("ERROR: ConsultaDAO obteniendo una conexion.");
		}
	}

	/**
	 * Cierra la conexion activa a la base de datos.
	 * @param con objeto de conexion a la bse de datos.
	 * @throws SQLException Si se presentan errores cerrando la conexion a la base de datos.
	 */
	public void cerrarConexion(java.sql.Connection con)throws SQLException{
		try{
			con.close();
			con = null;
		}catch(SQLException e){
			throw e;
		}
	}
	//------------------------------------------
	//Consultas
	//------------------------------------------
	/**
	 * Consulta para la existencia de valores.
	 */
	public ArrayList<ValorValue>existenciaValores(String valor, String tipoValor, String tipoRentabilidad, String negociado, String fechaExpiracion, String corOferenteDuenio, String corIntermediario, String corInversionista, String orden)throws SQLException{
		ArrayList<ValorValue> rta = new ArrayList<ValorValue>();
		PreparedStatement state = null;
		ValorValue value = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		if(!valor.equals("---"))where.add("VALOR.nombre = " + "'" + valor + "'");
		if(!tipoValor.equals("---"))where.add("TIPO_VALOR.nombre = "+ "'" +tipoValor + "'");
		if(!tipoRentabilidad.equals("---"))where.add("RENTABILIDAD.nombre = "+ "'" + tipoRentabilidad + "'");
		if(!negociado.equals("---"))where.add("VALOR.disponible = "+ "'" + negociado + "'");
		if(fechaExpiracion!=null)where.add("(VALOR.fecha_expiracion - TO_DATE('"+fechaExpiracion+"', 'DD-MM-YYYY')) = 0");		
		if(!corInversionista.equals("---"))where.add("VALOR.cod_inversionista = " + "'" +corInversionista + "'");
		if(!orden.equals("---"))order.add(orden);
		String consulta = creadorDeSentencias(select, "(VALOR JOIN TIPO_VALOR ON COD_TIPO_VALOR = TIPO_VALOR_ID) JOIN RENTABILIDAD ON COD_RENTABILIDAD = RENTABILIDAD_ID", where, order);

		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				value = new ValorValue();
				String disponible = rs.getString(1);
				value.setDisponible((disponible.equals("T"))?true: false);
				rta.add(value);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * CONSULTAR LAS OPERACIONES DE UN USUARIO
	 * Deben poder ser filtrados por tipo de usuario, por tipo de operacion, en cierto rango de fechas, costo, (valor, tipo de 
	 * rentabilidad). Se debe mostrar la fecha, los productos y cantidades solicitadas y el costo. Debe ofrecerse la posibilidad de 
	 * ordenamiento de las respuestas segun los intereses del usuario que consulta.
	 * @throws Exception 
	 */
	public ArrayList<OperacionValue> consultarOperaciones(String tipoUsuario, String tipoOperacion, String fechaMenor, String fechaMayor, int precio, String valor, String tipoRentabilidad) throws Exception{
		ArrayList<OperacionValue> rta = new ArrayList<OperacionValue>();
		PreparedStatement state = null;
		OperacionValue value = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("OPERACION.CANTIDAD");
		select.add("VALOR.NOMBRE");
		select.add("OPERACION.FECHA_ORDEN");
		if(!tipoOperacion.equals("---")){where.add("OPERACION.TIPO_COMPRA_VENTA = '"+tipoOperacion + "'"); order.add("OPERACION.TIPO_COMPRA_VENTA");}
		if(fechaMenor!=null && fechaMayor != null){where.add("(OPERACION.FECHA_ORDEN > TO_DATE('"+fechaMenor+"', 'DD-MM-YYYY')) AND  (OPERACION.FECHA_ORDEN < TO_DATE('"+fechaMayor+"', 'DD-MM-YYYY'))"); order.add("OPERACION.FECHA_ORDEN");}
		String consulta = "";
		if(tipoUsuario.equals("OFERENTE")){
			consulta = creadorDeSentencias(select, "((OPERACION JOIN OFERENTE ON cod_oferente = cod_usuario )JOIN VALOR ON cod_valor = valor_id) JOIN RENTABILIDAD ON rentabilidad_id = cod_rentabilidad", where, order);
		}else if(tipoUsuario.equals("INVERSIONISTA")){
			consulta = creadorDeSentencias(select, "((OPERACION JOIN INVERSIONISTA ON cod_inversionista = cod_usuario )JOIN VALOR ON cod_valor = valor_id) JOIN RENTABILIDAD ON rentabilidad_id = cod_rentabilidad", where, order); 
		}else{
			throw new Exception ("Indicador de tipo de usuario incorreto.");
		}

		System.out.println(consulta);

		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				value = new OperacionValue();
				value.setCantidad(rs.getInt(1));
				value.setIdValor(rs.getString(2));
				value.setFecha(rs.getDate(3));
				rta.add(value);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Consulta de Usuarios
	 */
	public ArrayList<String[]>consultaUsuarios(String tipoUs, String correo, String nombre, String telefono, String pais, String ciudad, String idRepresentante)throws SQLException{
		ArrayList<String[]> rta = new ArrayList<String[]>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");

		if(!correo.equals("---"))where.add("USUARIO.correo = " + "'" + correo + "'");
		if(!nombre.equals("---"))where.add("USUARIO.nombre = "+ "'" + nombre + "'");
		if(!telefono.equals("---"))where.add("USUARIO.nombre = "+ "'" + telefono + "'");
		if(!pais.equals("---"))where.add("USUARIO.nacionalidad = "+ "'" + pais + "'");
		if(!ciudad.equals("---"))where.add("USUARIO.ciudad = "+ "'" + ciudad + "'");		
		if(!idRepresentante.equals("---"))where.add("USUARIO.idrepresentante = " + "'" +idRepresentante + "'");
		String consulta="";
		if(tipoUs.equals("Oferente")){
			consulta = creadorDeSentencias(select, "USUARIO JOIN OFERENTE ON USUARIO.correo = OFERENTE.cod_usuario", where, order);
		}
		else if(tipoUs.equals("Inversionista")){
			consulta = creadorDeSentencias(select, "USUARIO JOIN INVERSIONISTA ON USUARIO.correo = INVERSIONISTA.cod_usuario", where, order);
		}
		else{
			consulta = creadorDeSentencias(select, "USUARIO JOIN INTERMEDIARIO ON USUARIO.correo = INTERMEDIARIO.cod_usuario", where, order);
		}

		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			String[] value = null;

			while(rs.next()){

				value = new String[6];
				value[0] = rs.getString("correo");
				value[1] = rs.getString("nombre");
				value[2] = rs.getString("telefono");
				value[3] = rs.getString("nacionalidad");
				value[4] = rs.getString("ciudad");
				value[5] = rs.getString("idrepresentante");

				rta.add(value);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}


	/**
	 * GENERADOR DE SENTENCIAS
	 * @param select, Atributos a seleccionar de la tabla
	 * @param tabla, tabla a buscar
	 * @param where, condicionales del WHERE.
	 * @param order, Tipos de Orden.
	 * @return sentencia, String sentencia completa para enviar a SQL.
	 */
	private String creadorDeSentencias(ArrayList<String>select, String tabla, ArrayList<String> where, ArrayList<String> order){
		String consulta = "SELECT ";
		if(select.size() == 1){
			consulta = consulta + select.get(0) + " ";
		}else{
			Iterator<String> it = select.iterator();
			while(it.hasNext()){
				String n = (String) it.next();
				if(!it.hasNext())
					consulta = consulta + n + " ";
				else
					consulta = consulta + n + ", ";
			}
		}
		consulta = consulta + "FROM " + tabla;		
		if(where.size() != 0){
			consulta = consulta + " WHERE ";
			Iterator<String> it = where.iterator();
			while(it.hasNext()){
				String n = (String) it.next();
				if(!it.hasNext())
					consulta = consulta + n + " ";
				else
					consulta = consulta + n + ", ";

			}
		}
		if(order.size() != 0){
			consulta = consulta + " ORDER BY ";
			Iterator<String> it = order.iterator();
			while(it.hasNext()){
				String n = (String) it.next();
				if(!it.hasNext())
					consulta = consulta + n + " ";
				else
					consulta = consulta + n + ", ";
			}

		}
		return consulta;
	}

	//-----------------------------------------------
	// Metodos para rellenar formularios
	//-----------------------------------------------

	/**
	 * Dar tipos de valor.
	 */
	public ArrayList<String> darTiposDeValores() throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("nombre");
		String consulta = creadorDeSentencias(select, "TIPO_VALOR", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString("nombre"));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Dar tipos de rentabilidad.
	 */
	public ArrayList<String> darTiposDeRentabilidad() throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("nombre");
		String consulta = creadorDeSentencias(select, "RENTABILIDAD", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString("nombre"));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Dar informacion de mis intermediarios.
	 */

	public ArrayList<String> darIntermediariosmios(String correo) throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("SOCIOS.correo_inversionista = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "USUARIO JOIN SOCIOS ON USUARIO.correo = SOCIOS.correo_intermediario", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				String cor = rs.getString("correo");
				String nom = rs.getString("nombre");
				rta.add(nom + "-" + cor);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Dar codigos y nombres de valores que le pertenecen al usuario.
	 */

	public ArrayList<String> darValoresVenta(String correoUs) throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("VALOR.nombre, VALOR.valor_id, VALOR.precio, USUARIO.nombre, VALOR_PROPIETARIOS.cantidad_valor");
		where.add("VALOR_PROPIETARIOS.correo_propietario = '" + correoUs + "'");
		String consulta = creadorDeSentencias(select, "(VALOR JOIN VALOR_PROPIETARIOS on VALOR.valor_id = VALOR_PROPIETARIOS.valor_id) JOIN USUARIO ON VALOR.cod_oferente_creador = USUARIO.correo"
				, where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+"-"+rs.getString(4)+"-"+rs.getString(5) );
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Dar codigos y nombres de valores que no le pertenecen al usuario.
	 */

	public ArrayList<String> darValoresCompra(String correoUs) throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("VALOR.nombre, VALOR.valor_id, VALOR.precio, USUARIO.nombre, OPERACION.cantidad");
		where.add("(OPERACION.cod_solicitante <> '" + correoUs + "') AND OPERACION.estado = 'Registrada'");
		String consulta = creadorDeSentencias(select, "(VALOR JOIN OPERACION ON OPERACION.cod_valor = VALOR.valor_id) JOIN USUARIO ON VALOR.cod_oferente_creador = USUARIO.correo", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+"-"+rs.getString(4)+"-"+rs.getString(5));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da todas las operaciones asociadas a un usuario.
	 */

	public ArrayList<OperacionValue> darOperacionesMias(String correo) throws SQLException{
		ArrayList<OperacionValue> rta = new ArrayList<OperacionValue>();
		PreparedStatement state = null;
		OperacionValue value = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("(OPERACION.cod_solicitante = '" + correo + "' OR OPERACIONES_INT.cod_intermediario = '" + correo + "') AND OPERACION.estado = 'No Registrada'");
		String consulta = creadorDeSentencias(select, "(OPERACION JOIN OPERACIONES_INT ON operacion_id = cod_operacion) JOIN VALOR ON valor_id = cod_valor", where, order);
		try{
			System.out.println(consulta);
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				value = new OperacionValue();
				String canti = rs.getString("cantidad");
				value.setCantidad(Integer.parseInt(canti));

				String corsol = rs.getString("cod_solicitante");
				value.setCorSolicitante(corsol);

				String date = rs.getString("fecha_orden");
				if(date!=null)
					try {
						value.setFecha(new SimpleDateFormat("DD-MM-YYYY").parse(date));
					} catch (ParseException e) {
						e.printStackTrace();
					}
				else
					value.setFecha(null);

				String id = rs.getString("operacion_id");
				value.setId(Integer.parseInt(id));

				String idVal = rs.getString("cod_valor");
				value.setIdValor(idVal);

				String precio = rs.getString("precio");
				value.setPrecio(Integer.parseInt(precio));

				String tipo = rs.getString("tipo_compra_venta");
				value.setTipoCompraVenta(tipo);

				String corinter = rs.getString("cod_intermediario");
				value.setCorIntermediario(corinter);

				String nomVal = rs.getString("nombre");
				value.setNomValor(nomVal);

				rta.add(value);

			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da todos los valores que le asigno a un intermediario para vender
	 * @throws SQLException 
	 */
	public ArrayList<String> darValoresVentaIntermediario(String correo) throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("VALOR.nombre, VALOR.valor_id, VALOR.precio, USUARIO.nombre, OPERACION.cantidad");
		where.add("((OPERACIONES_INT.cod_intermediario = '" + correo + "') AND OPERACION.estado = 'Registrada') AND OPERACION.tipo_compra_venta = 'Venta'");
		String consulta = creadorDeSentencias(select, "(((VALOR JOIN OPERACION ON OPERACION.cod_valor = VALOR.valor_id) JOIN OPERACIONES_INT ON OPERACION.operacion_id = OPERACIONES_INT.cod_operacion) "
				+ "JOIN USUARIO ON USUARIO.correo = VALOR.cod_oferente_creador)", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString(1)+"-"+rs.getString(2)+"-"+rs.getString(3)+"-"+rs.getString(4)+"-"+rs.getString(5));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//-----------------------------------------------------------
	//Metodos asociados a los Insert, Deletes, Updates.
	//-----------------------------------------------------------
	/**
	 * Ordena una operacion bursatil.
	 */
	public boolean ordenarOperacion(OperacionValue op, String codIntermediario) throws SQLException{
		PreparedStatement state = null;
		int id = generarId("OPERACION");
		String fechi = new SimpleDateFormat("dd-MM-YYYY").format(op.getFecha());
		String consulta = "INSERT INTO OPERACION VALUES (TO_DATE('"+ fechi + "', 'DD-MM-YYYY') , '" + op.getTipoCompraVenta() + "', "
				+  op.getIdValor() + ", '" + op.getCorSolicitante() + "', 'No Registrada', " + id + ", " + op.getCantidad() + ")";
		String consulta2 = "INSERT INTO OPERACIONES_INT VALUES ("+ id + ", '" +  codIntermediario + "')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
			return state.execute(consulta2);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Cancela la operacion bursatil.
	 * @return
	 * @throws SQLException 
	 */
	public boolean cancelarOperacionBursatil(String idUser, int idOperacion) throws SQLException{
		PreparedStatement state = null;
		String consulta = "DELETE FROM OPERACION WHERE operacion_id = '"+idOperacion+"'";
		String consulta2 = "DELETE FROM OPERACIONES_INT WHERE cod_operacion = '"+idOperacion +"' AND cod_intermediario ='" + idUser + "'";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta2);
			return state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			System.out.println(consulta2);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Registra una operacion bursatil.
	 * @return
	 * @throws SQLException 
	 */
	@SuppressWarnings("resource")
	public String registrarOperacionBursatil(OperacionValue op) throws SQLException{
		PreparedStatement state = null;
		String consulta="";
		String rta = "";

		//Se crean diferentes sentencias para todos los posibles casos que se puedan dar
		String consultaUpValor="";
		String consultaUpOperacion="";
		String consultaUpOperacion2="";
		String consultaUpOperaciones_int="";
		String consultaUpOperaciones_int2="";
		String consultaUpValorPropietarios="";
		String consultaUpPortafolioValor="";
		String consultaUpValorPorcentaje="";


		if(op.getTipoCompraVenta().equals("Venta")){
			consultaUpValor = "UPDATE VALOR SET disponible = 'T' WHERE valor_id = "+op.getIdValor();
			consultaUpOperacion = "UPDATE OPERACION SET estado = 'Registrada' WHERE operacion_id = "+op.getId();

			String[] cantidadDisp = darCantidadRestante(op.getIdValor(), op.getCorSolicitante() + "").split("-");

			int cant = Integer.parseInt(cantidadDisp[0]);
			if(cant > op.getCantidad()){

				consultaUpValorPropietarios = "UPDATE VALOR_PROPIETARIOS SET cantidad_valor = (cantidad_valor - "+ op.getCantidad()  +  ")  WHERE correo_propietario = '"+op.getCorSolicitante()
						+ "' AND valor_id = " + op.getIdValor();
			}
			else{
				consultaUpValorPropietarios = "DELETE FROM VALOR_PROPIETARIOS WHERE correo_propietario = '"+op.getCorSolicitante()
						+ "' AND valor_id = " + op.getIdValor();
			}

		}
		else{

			String[] cantidadDisp = darCantidadRestante(op.getIdValor(), "").split("-");
			int cant = Integer.parseInt(cantidadDisp[0]);

			consultaUpOperaciones_int = "DELETE FROM OPERACIONES_INT WHERE cod_operacion = " + op.getId();
			consultaUpOperacion = "DELETE FROM OPERACION WHERE operacion_id = " + op.getId();

			if(cant>op.getCantidad()){
				consultaUpOperacion2 = "UPDATE OPERACION SET cantidad = (cantidad - " + op.getCantidad() + ") WHERE operacion_id = " + cantidadDisp[1]; 
			}

			else if(cant==op.getCantidad()){
				consultaUpOperaciones_int2 = "DELETE FROM OPERACIONES_INT WHERE cod_operacion = " + cantidadDisp[1]; 
				consultaUpOperacion2 = "DELETE FROM OPERACION WHERE operacion_id = " + cantidadDisp[1]; 
				consultaUpValor = "UPDATE VALOR SET disponible = 'F' WHERE valor_id = "+op.getIdValor();
				consultaUpPortafolioValor = "DELETE FROM PORTAFOLIO_VALOR WHERE cod_valor = " + op.getIdValor(); 
				consultaUpValorPorcentaje = "DELETE FROM VALOR_PORCENTAJE WHERE cod_valor = " + op.getIdValor(); 
			}
			else{
				rta = "Ya no se encuentra disponible la cantidad deseada a comprar del valor, la operacion de compra sera cancelada"
						+ ", si lo desea puede ordenar una nueva orden de compra por una cantidad menor.";
			}
			if(rta.isEmpty()){

				if(tieneValor(op.getCorSolicitante(), op.getIdValor())){
					consultaUpValorPropietarios = "UPDATE VALOR_PROPIETARIOS SET cantidad_valor = (cantidad_valor + "+ op.getCantidad()  +  ")  WHERE correo_propietario = '"+op.getCorSolicitante()
							+ "' AND valor_id = " + op.getIdValor();
				}
				else{
					consultaUpValorPropietarios = "INSERT INTO VALOR_PROPIETARIOS VALUES ('"+ op.getCorSolicitante() + "', " +  op.getIdValor() + ", "+
							op.getCantidad()+ ")";
				}
			}
		}
		try{
			establecerConexion(cadenaConexion, usuario, clave);

			// Se ejecutan las sentencias de actualizacion si fueron definidas anteriormente
			if(!consultaUpOperaciones_int.isEmpty()){state = conexion.prepareStatement(consultaUpOperaciones_int);state.execute(consultaUpOperaciones_int);consulta=consultaUpOperaciones_int;}
			if(!consultaUpOperacion.isEmpty()){state = conexion.prepareStatement(consultaUpOperacion);state.execute(consultaUpOperacion);consulta=consultaUpOperacion;}
			if(!consultaUpValorPropietarios.isEmpty()){state = conexion.prepareStatement(consultaUpValorPropietarios);state.execute(consultaUpValorPropietarios);consulta=consultaUpValorPropietarios;}
			if(!consultaUpValor.isEmpty()){state = conexion.prepareStatement(consultaUpValor);state.execute(consultaUpValor);consulta=consultaUpValor;}
			if(!consultaUpOperaciones_int2.isEmpty()){state = conexion.prepareStatement(consultaUpOperaciones_int2);state.execute(consultaUpOperaciones_int2);consulta=consultaUpOperaciones_int2;}
			if(!consultaUpOperacion2.isEmpty()){state = conexion.prepareStatement(consultaUpOperacion2);state.execute(consultaUpOperacion2);consulta=consultaUpOperacion2;}
			if(!consultaUpPortafolioValor.isEmpty()){state = conexion.prepareStatement(consultaUpPortafolioValor);state.execute(consultaUpPortafolioValor);consulta=consultaUpPortafolioValor;}
			if(!consultaUpValorPorcentaje.isEmpty()){state = conexion.prepareStatement(consultaUpValorPorcentaje);state.execute(consultaUpValorPorcentaje);consulta=consultaUpValorPorcentaje;}


		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Retorna la cantidad disponible de un valor en cierta operacion
	 * @throws SQLException 
	 */
	public String darCantidadRestante(String codValor, String codProp) throws SQLException{
		int total = 0;
		String rta = total + "-NA";
		PreparedStatement state = null;
		String consulta;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		if(!codValor.isEmpty()){
			select.add("cantidad, operacion_id");
			where.add("cod_valor = '" + codValor + "' AND estado = 'Registrada'");
			consulta = creadorDeSentencias(select, "OPERACION", where, order);
		}

		else{
			select.add("cantidad_valor, valor_id");
			where.add("correo_propietario = '" + codProp + "' AND valor_id = " + codValor);
			consulta = creadorDeSentencias(select, "VALOR_PROPIETARIOS", where, order);
		}
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				if(total<rs.getInt(1)){
					total = rs.getInt(1);
					rta = total + "-" + rs.getString(2);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Informa si un usuario tiene el valor dado por parametro
	 * @throws SQLException 
	 */
	public boolean tieneValor(String corUsuario, String codValor) throws SQLException{
		boolean rta = false;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("valor_id = '" + codValor + "' AND correo_propietario = '" + corUsuario + "'");
		String consulta = creadorDeSentencias(select, "VALOR_PROPIETARIOS", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			if(rs.next()){
				rta = true;
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//----------------------------
	//Visualizar Usuarios
	//----------------------------

	/**
	 * Devuelve un oferente dado su correo
	 * @param correo
	 * @return
	 * @throws SQLException 
	 */
	public OferenteValue darInfoOferente(String correo) throws SQLException{
		OferenteValue rta = null;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("USUARIO.correo = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "USUARIO JOIN OFERENTE ON USUARIO.correo = OFERENTE.cod_usuario", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			if(rs.next()){
				rta = new OferenteValue();
				rta.setCiudad(rs.getString("ciudad"));
				rta.setCodPostal(rs.getInt("codigopostal"));
				rta.setCorreo(correo);
				rta.setDepartamento(rs.getString("departamento"));
				rta.setDireccion(rs.getString("direccion"));
				rta.setIdRepresentante(rs.getString("idrepresentante"));
				rta.setNombreRepresentante(rs.getString("nombrerepresentante"));
				rta.setNacionalidad(rs.getString("nacionalidad"));
				rta.setNombre(rs.getString("nombre"));
				rta.setTelefono(rs.getString("telefono"));
				rta.setTipoEntidad(rs.getString("tipo_entidad"));
				rta.setValores(darValoresInfoUs(correo, "Ofer"));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Devuelve un inversionista dado su correo
	 * @param correo
	 * @return
	 * @throws SQLException 
	 */
	public InversionistaValue darInfoInversionista(String correo) throws SQLException{
		InversionistaValue rta = null;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("USUARIO.correo = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "USUARIO JOIN INVERSIONISTA ON USUARIO.correo = INVERSIONISTA.cod_usuario", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			if(rs.next()){

				rta = new InversionistaValue();
				rta.setCiudad(rs.getString("ciudad"));
				rta.setCodPostal(rs.getInt("codigopostal"));
				rta.setCorreo(correo);
				rta.setDepartamento(rs.getString("departamento"));
				rta.setDireccion(rs.getString("direccion"));
				rta.setIdRepresentante(rs.getString("idrepresentante"));
				rta.setNombreRepresentante(rs.getString("nombrerepresentante"));
				rta.setNacionalidad(rs.getString("nacionalidad"));
				rta.setNombre(rs.getString("nombre"));
				rta.setTelefono(rs.getInt("telefono"));
				rta.setDocIdentidad(rs.getInt("documento_identidad"));
				rta.setValores(darValoresInfoUs(correo, "Inver"));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Devuelve los valores que tiene asociados un oferente
	 */
	public ArrayList<String[]> darValoresInfoUs(String correo, String tipo)throws SQLException{
		ArrayList<String[]> rta = new ArrayList<String[]>();
		String[] val = null;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		String consulta = "";
		select.add("*");
		if(tipo.equals("Ofer")){
			where.add("VALOR.cod_oferente_creador = '" + correo + "'");
			consulta = creadorDeSentencias(select, "VALOR JOIN VALOR_PROPIETARIOS ON VALOR.valor_id = VALOR_PROPIETARIOS.valor_id" , where, order);
		}

		else{
			where.add("VALOR_PROPIETARIOS.correo_propietario = '" + correo + "'");
			consulta = creadorDeSentencias(select, "VALOR JOIN VALOR_PROPIETARIOS ON VALOR.valor_id = VALOR_PROPIETARIOS.valor_id" , where, order);
		}


		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				val = new String[6];
				val[0] = rs.getString("nombre");
				val[1] = rs.getString("correo_propietario");
				val[2] = rs.getString("cantidad_valor");
				val[3] = rs.getString("precio");
				val[4] = rs.getString("mercado");
				val[5] = rs.getString("cod_oferente_creador");
				rta.add(val);
			}

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Devuelve un intermediario dado su correo
	 * @param correo
	 * @return
	 * @throws SQLException 
	 */
	public IntermediarioValue darInfoIntermediario(String correo) throws SQLException{
		IntermediarioValue rta = null;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("*");
		where.add("USUARIO.correo = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "USUARIO JOIN INTERMEDIARIO ON USUARIO.correo = INTERMEDIARIO.cod_usuario", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			if(rs.next()){
				rta = new IntermediarioValue();
				rta.setCiudad(rs.getString("ciudad"));
				rta.setCodPostal(rs.getInt("codigopostal"));
				rta.setCorreo(correo);
				rta.setDepartamento(rs.getString("departamento"));
				rta.setDireccion(rs.getString("direccion"));
				rta.setIdRepresentante(rs.getString("idrepresentante"));
				rta.setNombreRepresentante(rs.getString("nombrerepresentante"));
				rta.setNacionalidad(rs.getString("nacionalidad"));
				rta.setTelefono(rs.getString("telefono"));
				rta.setNombre(rs.getString("nombre"));
				rta.setTipoEntidad(rs.getString("tipo_entidad"));
				rta.setNumRegistro(rs.getString("num_registro"));
				rta.setValoresneg(darValoresInter(correo));
				rta.setPortafolios(darPortafoliosIntermediario(correo));
				rta.setSocios(darSociosIntermediario(correo));

			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da los valores negociados por un intermediario
	 * @throws SQLException 
	 */
	public ArrayList<ValorValue> darValoresInter(String correo) throws SQLException{
		ArrayList<ValorValue> rta = new ArrayList<ValorValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("OPERACIONES_INT.cod_intermediario = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "(OPERACIONES_INT JOIN OPERACION ON OPERACIONES_INT.cod_operacion = OPERACION.operacion_id) JOIN VALOR ON OPERACION.cod_valor = VALOR.valor_id", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				ValorValue val = new ValorValue();
				val.setCreador(rs.getString("cod_oferente_creador"));
				val.setNombre(rs.getString("nombre"));
				val.setMercado(rs.getString("mercado"));
				val.setPrecio(rs.getInt("precio"));
				rta.add(val);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da los socios de un intermediario
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<String[]> darSociosIntermediario(String correo) throws SQLException{
		ArrayList<String[]> rta = new ArrayList<String[]>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("SOCIOS.correo_intermediario = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "SOCIOS JOIN USUARIO ON SOCIOS.correo_inversionista = USUARIO.correo", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				String[] soc = new String[4];
				soc[0] = rs.getString("nombre");
				soc[1] = rs.getString("nacionalidad");
				soc[2] = rs.getString("correo");
				soc[3] = rs.getString("telefono");
				rta.add(soc);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da la informacion de los portafolios de un intermediario
	 * @throws SQLException 
	 */
	public ArrayList<PortafolioValue> darPortafoliosIntermediario(String correo) throws SQLException{
		ArrayList<PortafolioValue> rta = new ArrayList<PortafolioValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;		
		select.add("*");
		where.add("PORTAFOLIO.cod_intermediario = '" + correo + "'");
		String consulta = creadorDeSentencias(select, "PORTAFOLIO", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				PortafolioValue port = new PortafolioValue();
				port.setId(rs.getInt("portafolio_id"));
				port.setNombre(rs.getString("nombre_portafolio"));
				port.setTipoRiesgo(rs.getString("nivelriesgo"));
				rta.add(port);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//-----------------------------
	//AUTENTICACION
	//------------------------------

	/**
	 * Autenticacion.
	 * @throws SQLException 
	 */
	@SuppressWarnings("resource")
	public Object login(String correo, String contrasenia) throws SQLException{
		Object rta = null;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		where.add("INVERSIONISTA.COD_USUARIO = '" + correo + "'");
		select.add("*");
		String consulta = creadorDeSentencias(select, "INVERSIONISTA", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				ArrayList<String>selectInv = new ArrayList<String>();
				ArrayList<String>whereInv = new ArrayList<String>();
				ArrayList<String>orderInv = new ArrayList<String>();
				selectInv.add("*");
				whereInv.add("USUARIO.CORREO = '"+correo+"' AND USUARIO.CONTRASENIA = '"+contrasenia+"'");
				String consultaInv = creadorDeSentencias(selectInv, "INVERSIONISTA JOIN USUARIO ON COD_USUARIO = CORREO ", whereInv, orderInv);
				state = conexion.prepareStatement(consultaInv);
				System.out.println(consultaInv);
				ResultSet rs1 = state.executeQuery();
				while(rs1.next()){
					InversionistaValue invVal =  new InversionistaValue();
					String ciudad = rs1.getString("CIUDAD");
					invVal.setCiudad(ciudad);
					int codPostal = rs1.getInt("CODIGOPOSTAL");
					invVal.setCodPostal(codPostal);
					String contrasenia1 = rs1.getString("CONTRASENIA");
					invVal.setContrasenia(contrasenia1);
					String correo1 = rs1.getString("CORREO");
					invVal.setCorreo(correo1);
					String departamento = rs1.getString("DEPARTAMENTO");
					invVal.setDepartamento(departamento);
					String direccion = rs1.getString("DIRECCION");
					invVal.setDireccion(direccion);
					String idRep = rs1.getString("IDREPRESENTANTE");
					invVal.setIdRepresentante(idRep);
					String nacionalidad = rs1.getString("NACIONALIDAD");
					invVal.setNacionalidad(nacionalidad);
					String nombre = rs1.getString("NOMBRE");
					invVal.setNombre(nombre);
					String nomRep = rs1.getString("NOMBREREPRESENTANTE");
					invVal.setNombreRepresentante(nomRep);
					int telefono = Integer.parseInt(rs1.getString("TELEFONO"));
					invVal.setTelefono(telefono);
					int docIdentidad = rs1.getInt("DOCUMENTO_IDENTIDAD");
					invVal.setDocIdentidad(docIdentidad);

					rta = (contrasenia1.equals(contrasenia))?invVal:null;
				}}
			//-------------------------------------------------
			//-------------------------------------------------
			if(rta == null){ArrayList<String>selectOf = new ArrayList<String>();
			ArrayList<String>whereOf = new ArrayList<String>();
			ArrayList<String>orderOf = new ArrayList<String>();
			selectOf.add("*");
			whereOf.add("USUARIO.CORREO = '"+correo+"' AND USUARIO.CONTRASENIA = '"+contrasenia+"'");
			String consultaOf = creadorDeSentencias(selectOf, "OFERENTE JOIN USUARIO ON COD_USUARIO = CORREO ", whereOf, orderOf);
			state = conexion.prepareStatement(consultaOf);
			System.out.println(consultaOf);
			ResultSet rs2 = state.executeQuery();
			while(rs2.next()){
				OferenteValue invOf =  new OferenteValue();
				String ciudad = rs2.getString("CIUDAD");
				invOf.setCiudad(ciudad);
				int codPostal = rs2.getInt("CODIGOPOSTAL");
				invOf.setCodPostal(codPostal);
				String contrasenia1 = rs2.getString("CONTRASENIA");
				invOf.setContrasenia(contrasenia1);
				String correo1 = rs2.getString("CORREO");
				invOf.setCorreo(correo1);
				String departamento = rs2.getString("DEPARTAMENTO");
				invOf.setDepartamento(departamento);
				String direccion = rs2.getString("DIRECCION");
				invOf.setDireccion(direccion);
				String idRep = rs2.getString("IDREPRESENTANTE");
				invOf.setIdRepresentante(idRep);
				String nacionalidad = rs2.getString("NACIONALIDAD");
				invOf.setNacionalidad(nacionalidad);
				String nombre = rs2.getString("NOMBRE");
				invOf.setNombre(nombre);
				String nomRep = rs2.getString("NOMBREREPRESENTANTE");
				invOf.setNombreRepresentante(nomRep);
				String telefono = rs2.getString("TELEFONO");
				invOf.setTelefono(telefono);
				invOf.setTipoEntidad(rs2.getString("TIPO_ENTIDAD"));
				invOf.setIdRentabilidad(rs2.getInt("COD_RENTABILIDAD"));

				rta = (contrasenia1.equals(contrasenia))?invOf:null;
			}}

			//-------------------------------------------------
			//-------------------------------------------------
			if(rta == null){ArrayList<String>selectInt = new ArrayList<String>();
			ArrayList<String>whereInt = new ArrayList<String>();
			ArrayList<String>orderInt = new ArrayList<String>();
			selectInt.add("*");
			whereInt.add("USUARIO.CORREO = '"+correo+"' AND USUARIO.CONTRASENIA = '"+contrasenia+"'");
			String consultaInt = creadorDeSentencias(selectInt, "INTERMEDIARIO JOIN USUARIO ON COD_USUARIO = CORREO ", whereInt, orderInt);
			state = conexion.prepareStatement(consultaInt);
			System.out.println(consultaInt);
			ResultSet rs3 = state.executeQuery();
			while(rs3.next()){
				IntermediarioValue invInt =  new IntermediarioValue();
				String ciudad = rs3.getString("CIUDAD");
				invInt.setCiudad(ciudad);
				int codPostal = rs3.getInt("CODIGOPOSTAL");
				invInt.setCodPostal(codPostal);
				String contrasenia1 = rs3.getString("CONTRASENIA");
				invInt.setContrasenia(contrasenia1);
				String correo1 = rs3.getString("CORREO");
				invInt.setCorreo(correo1);
				String departamento = rs3.getString("DEPARTAMENTO");
				invInt.setDepartamento(departamento);
				String direccion = rs3.getString("DIRECCION");
				invInt.setDireccion(direccion);
				String idRep = rs3.getString("IDREPRESENTANTE");
				invInt.setIdRepresentante(idRep);
				String nacionalidad = rs3.getString("NACIONALIDAD");
				invInt.setNacionalidad(nacionalidad);
				String nombre = rs3.getString("NOMBRE");
				invInt.setNombre(nombre);
				String nomRep = rs3.getString("NOMBREREPRESENTANTE");
				invInt.setNombreRepresentante(nomRep);
				String telefono = rs3.getString("TELEFONO");
				invInt.setTelefono(telefono);
				invInt.setTipoEntidad(rs3.getString("TIPO_ENTIDAD"));
				invInt.setNumRegistro(rs3.getString("NUM_REGISTRO"));

				rta = (contrasenia1.equals(contrasenia))?invInt:null;
			}
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//____________________________________________________________
	//
	//PORTAFOLIOS
	//____________________________________________________________
	//
	//------------------------------------------------------------
	//Portafolio Inversionista
	//------------------------------------------------------------

	/**
	 * Crea un In-Portafolio a partir de un portafolio del intermediario
	 * @throws SQLException 
	 */

	public void crearInPortafolio(String corInver, int codPorta, String nomPorta, int cantidad ) throws SQLException{
		PreparedStatement state = null;
		String consulta = "INSERT INTO IN_PORTAFOLIO VALUES ( '" + corInver + "', " + codPorta + ", " + nomPorta + ", " +  0 + ")";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Cambiar el procentaje de un valor dados el codigo del portafolio, el cod del valor y el nuevo proccentaje. 	
	 * @throws SQLException 
	 */
	public void cambiarInPorcentaje(String codInv, int codPortafolio, int codVal, int nPor) throws SQLException{
		PreparedStatement state = null;
		String consulta = "UPDATE VALOR_PORCENTAJE SET porcentaje = "+nPor+" WHERE cod_inversionista = '" + codInv + "' AND cod_portafolio = "+codPortafolio+" AND cod_valor = "+codVal;
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Elimina un valor de un portafolio dados el cod del valor y el id del portafolio. 	
	 * @throws SQLException 
	 */
	public void eliminarValorInPortafolio(String codInv, int codPortafolio, int codVal) throws SQLException{
		PreparedStatement state = null;
		String consulta = "DELETE FROM VALOR_PROCENTAJE WHERE cod_inversionista = '"+codInv+"' AND cod_portafolio = "+codPortafolio+", cod_valor = "+codVal;
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Agrega un nuevo valor a un portafolio dados el cod del portafolio el codigo del valor y el porcentaje inicial del valor. 	
	 * @throws SQLException 
	 */
	public void agregarValorInPortafolio(String codInv, int codPortafolio, int codVal) throws SQLException{
		PreparedStatement state = null;
		String consulta = "INSERT INTO VALOR_PORCENTAJE VALUES ( 0," + codVal + ", " + codPortafolio + ", '" +  codInv + "')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Dar valores del portafolio y sus porcentajes dado el id del portafolio.
	 * @throws SQLException
	 */
	public ArrayList<ValorPorcentajeInversionValue> darValoresInPortafolio(String codInv, int codPortafolio) throws SQLException{
		ArrayList<ValorPorcentajeInversionValue> rta = new ArrayList<ValorPorcentajeInversionValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("cod_inversionista = '"+codInv+"' AND cod_Portafolio = " + codPortafolio);
		String consulta = creadorDeSentencias(select, "VALOR_PORCENTAJE JOIN VALOR ON cod_valor = valor_id", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				ValorPorcentajeInversionValue val = new ValorPorcentajeInversionValue();
				val.setPorcentajeInversion(rs.getInt("porcentaje"));
				ValorValue valor = new ValorValue();
				valor.setCreador(rs.getString("cod_oferente_creador"));
				valor.setDisponible((rs.getString("disponible").equals("T"))?true:false);
				valor.setFechaExpiracion(rs.getDate("fecha_expiracion"));
				valor.setMercado(rs.getString("mercado"));
				valor.setNombre(rs.getString("nombre"));
				valor.setPrecio(rs.getInt("precio"));
				valor.setCodigo(rs.getInt("valor_id"));
				val.setValor(valor);
				rta.add(val);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da todos los portafolios de los intermediarios asociados al inversionista
	 * @throws SQLException 
	 */
	public ArrayList<InPortafolioValue> darInPortafolios(String correoInv) throws SQLException{
		ArrayList<InPortafolioValue> rta = new ArrayList<InPortafolioValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("IN_PORTAFOLIO.cod_inversionista = '" + correoInv + "'");
		String consulta = creadorDeSentencias(select, "IN_PORTAFOLIO JOIN PORTAFOLIO ON IN_PORTAFOLIO.cod_portafolio = PORTAFOLIO.portafolio_id", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				InPortafolioValue val = new InPortafolioValue();
				PortafolioValue val2 = new PortafolioValue();

				val.setCantidadGlobal(rs.getInt("cantidad_acciones"));
				val2.setId(rs.getInt("portafolio_id"));
				val2.setNombre(rs.getString("nombre_portafolio"));
				val2.setTipoRiesgo(rs.getString("nivelriesgo"));
				val2.setCorreoInter(rs.getString("cod_intermediario"));

				val.setParentPortafolio(val2);
				rta.add(val);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//------------------------------------------------------------
	//Portafolio Intermediario
	//------------------------------------------------------------

	/**
	 * Crea un portafolio para un intermediario dado su codigo, el nombre del portafolio y el nivel de riesgo.
	 * @throws SQLException 
	 */
	public void crearPortafolio(String codIntermediario, String nombrePortafolio, String nivelRiesgo) throws SQLException{
		PreparedStatement state = null;
		int id = generarId("PORTAFOLIO");
		String consulta = 	"INSERT INTO PORTAFOLIO VALUES ( '"+codIntermediario+"', " + id + ", '" +  nombrePortafolio + "', '" +  nivelRiesgo + "')";

		ArrayList<String> socios = darCorreosSocios(codIntermediario);
		ArrayList<String> consultas = new ArrayList<String>();

		for(int i = 0; i<socios.size();i++){
			String cons = "INSERT INTO IN_PORTAFOLIO VALUES ( '"+socios.get(i)+"', " + id + ", 0)";
			consultas.add(cons);
			System.out.println(cons);
		}

		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);

			for(int i = 0; i<consultas.size();i++){
				state = conexion.prepareStatement(consultas.get(i));
				state.execute(consultas.get(i));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Añade un valor a un portafolio de un intermediario dados el codigo del valor y el codigo del portafolio.
	 * @throws SQLException 
	 */
	public void anadirValorAPortafolio(int codPortafolio, int codValor) throws SQLException{
		PreparedStatement state = null;
		String consulta = 	"INSERT INTO PORTAFOLIO_VALOR VALUES ( "+codPortafolio+", "+codValor+")";	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Elimina un valor de un portafolio dados el cod del portafolio y el cod del valor.
	 * @throws SQLException 
	 */
	public void eliminarValorDeProtafolio(int codPortafolio, int codValor) throws SQLException{
		PreparedStatement state = null;
		String consulta = 	"DELETE FROM PORTAFOLIO_VALOR WHERE cod_portafolio = "+codPortafolio+" AND cod_valor = "+codValor;	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Da todos los valores que hay en un portafolio de intermediario
	 * @throws SQLException 
	 */
	public ArrayList<ValorValue> darValoresPortafolio(int idPortafolio, String corIntermediario) throws SQLException{
		ArrayList<ValorValue> rta = new ArrayList<ValorValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("PORTAFOLIO.cod_intermediario = '" + corIntermediario + "' AND PORTAFOLIO.portafolio_id =" + idPortafolio);
		String consulta = creadorDeSentencias(select, "(PORTAFOLIO JOIN PORTAFOLIO_VALOR ON PORTAFOLIO.portafolio_id = PORTAFOLIO_VALOR.cod_portafolio)"
				+ " JOIN VALOR ON VALOR.valor_id = PORTAFOLIO_VALOR.cod_valor", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			System.out.println(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				ValorValue val = new ValorValue();
				val.setCreador(rs.getString("cod_oferente_creador"));
				val.setDisponible((rs.getString("disponible").equals("T"))?true:false);
				val.setFechaExpiracion(rs.getDate("fecha_expiracion"));
				val.setMercado(rs.getString("mercado"));
				val.setNombre(rs.getString("nombre"));
				val.setPrecio(rs.getInt("precio"));
				val.setCodigo(rs.getInt("valor_id"));
				rta.add(val);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * Da todos los socios de un intermediario dado
	 * @throws SQLException 
	 */
	public ArrayList<String> darCorreosSocios(String correoInter) throws SQLException{
		ArrayList<String> rta = new ArrayList<String>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("SOCIOS.correo_intermediario = '" + correoInter + "'");
		String consulta = creadorDeSentencias(select, "SOCIOS JOIN INVERSIONISTA ON SOCIOS.correo_inversionista = INVERSIONISTA.cod_usuario", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta.add(rs.getString("correo_inversionista"));
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//-----------------------------------------------------------------------
	//RETIRAR INTERMEDIARIO
	//-----------------------------------------------------------------------
	/**
	 * RETIRAR INTERMEDIARIO, retira el intermediario completamente dado su codigo y el de su sucesor.
	 */
	public void retirarIntermediario(String intRetirado, String intAsociado) throws SQLException{
		String cCambiarPort = "UPDATE PORTAFOLIO SET cod_intermediario = '"+intAsociado+"' WHERE cod_intermediario = '"+intRetirado+"'";
		String cCambiarSocios = "UPDATE SOCIOS SET correo_intermediario = '"+intAsociado+"' WHERE correo_intermediario = '"+intRetirado+"'";
		String cCambiarOperaciones = "UPDATE OPERACIONES_INT SET cod_intermediario = '"+intAsociado+"' WHERE cod_intermediario = '"+intRetirado+"'";
		String cEliminarIntermediario = "DELETE FROM INTERMEDIARIO WHERE cod_usuario = '"+intRetirado+"'";
		String cEliminarIntermediarioUS = "DELETE FROM USUARIO WHERE correo = '"+intRetirado+"'";
		Statement state = null;
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.createStatement();
			state.addBatch(cCambiarPort);
			state.addBatch(cCambiarSocios);
			state.addBatch(cCambiarOperaciones);
			state.addBatch(cEliminarIntermediario);
			state.addBatch(cEliminarIntermediarioUS);
			state.executeBatch();
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println("Ejecutando :");
			System.out.println(cCambiarPort);
			System.out.println(cCambiarSocios);
			System.out.println(cCambiarOperaciones);
			System.out.println(cEliminarIntermediario);
			System.out.println(cEliminarIntermediarioUS);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
	}

	/**
	 * Da todos los intermediarios de la bolsa de valores
	 * @throws SQLException 
	 */
	public ArrayList<IntermediarioValue> darTodosIntermediarios() throws SQLException{
		ArrayList<IntermediarioValue> rta = new ArrayList<IntermediarioValue>();
		ArrayList<String> select = new ArrayList<String>();
		ArrayList<String> where = new ArrayList<String>();
		ArrayList<String> order = new ArrayList<String>();
		PreparedStatement state = null;
		select.add("*");
		where.add("rownum < 305");
		String consulta = creadorDeSentencias(select, "INTERMEDIARIO JOIN USUARIO ON USUARIO.correo = INTERMEDIARIO.cod_usuario", where, order);	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				IntermediarioValue val = new IntermediarioValue();
				val = new IntermediarioValue();
				val.setCiudad(rs.getString("ciudad"));
				val.setCodPostal(rs.getInt("codigopostal"));
				val.setCorreo(rs.getString("correo"));
				val.setDepartamento(rs.getString("departamento"));
				val.setDireccion(rs.getString("direccion"));
				val.setIdRepresentante(rs.getString("idrepresentante"));
				val.setNombreRepresentante(rs.getString("nombrerepresentante"));
				val.setNacionalidad(rs.getString("nacionalidad"));
				val.setNombre(rs.getString("nombre"));
				val.setTelefono(rs.getString("telefono"));
				val.setTipoEntidad(rs.getString("tipo_entidad"));
				val.setNumRegistro(rs.getString("num_registro"));
				rta.add(val);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//------------------------------------------------------------------------
	//Consultas Iteracion 4
	//------------------------------------------------------------------------

	/**
	 * Solucion a los RFC de movimiento de valores
	 * Los parametros fechaInicial, fechaFinal e incluirFiltros son obligatorios, es decir siempre seran diferentes de "---"
	 * Los demas parametros pueden ser "---" si son vacios/
	 * El parametro incluirFiltros determina si se buscaran valores que cumplan con los filtros (true si cumplen, false no cumplen)
	 * Las fechas siempre seran incluidas sin importar el parametro incluirFiltros	
	 * @return 
	 * @throws SQLException 
	 */
	public ArrayList<OperacionValue> consultarMovimientos(String fechaInicial, String fechaFinal, boolean incluirFiltros, String nomValor, String tipoValor, String tipoRentabilidad,
			String tipoOperacion, String correoOfInv, String correoIntermediario) throws SQLException{
		ArrayList<OperacionValue> rta = new ArrayList<OperacionValue>();
		PreparedStatement state = null;
		String consulta = "";
		if(incluirFiltros){
			consulta = "SELECT * FROM (SELECT * FROM OPERACION WHERE FECHA_ORDEN > to_date('"+fechaInicial+"','DD/MM/YYYY') AND FECHA_ORDEN < to_date('"+fechaFinal+"','DD/MM/YYYY')) JOIN VALOR ON COD_VALOR = VALOR_ID";
			//CORE: SELECT * FROM OPERACION WHERE FECHA_ORDEN > to_date(fechaInicial,'DD/MM/YYYY') AND FECHA_ORDEN < to_date(fechaFinal,'DD/MM/YYYY')
			if(!tipoOperacion.equals( "---" ))consulta = consulta + " WHERE TIPO_COMPRA_VENTA = '"+tipoOperacion+"'";
			if(!correoOfInv.equals( "---" ) && !tipoOperacion.equals( "---"))consulta = consulta + " AND COD_SOLICITANTE = '"+correoOfInv+"'";
			if(!correoOfInv.equals( "---" )&& tipoOperacion.equals( "---" ))consulta = consulta + " WHERE COD_SOLICITANTE = '"+correoOfInv+"'";
			consulta = (!nomValor.equals("---") && tipoOperacion.equals( "---" ) && correoOfInv.equals( "---" ))?consulta + " WHERE VALOR.NOMBRE = '"+nomValor+"'": consulta + "";
			if(!correoIntermediario .equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN OPERACIONES_INT ON OPERACION_ID = COD_OPERACION WHERE COD_INTERMEDIARIO = '"+correoIntermediario+"'";
			if(!tipoValor.equals( "---" )&& nomValor.equals( "---" ))consulta = "SELECT * FROM (SELECT * FROM ("+consulta+") JOIN VALOR ON COD_VALOR = VALOR_ID) JOIN TIPO_VALOR ON COD_TIPO_VALOR = TIPO_VALOR_ID WHERE TIPO_VALOR.NOMBRE = '"+tipoValor+"'";
			if(!tipoValor.equals( "---" ) && !nomValor.equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN TIPO_VALOR ON COD_TIPO_VALOR = TIPO_VALOR_ID WHERE TIPO_VALOR.NOMBRE = '"+tipoValor+"'";
			if(!tipoRentabilidad.equals( "---" ) && nomValor.equals( "---" ))consulta = "SELECT * FROM (SELECT * FROM ("+consulta+") JOIN VALOR ON COD_VALOR = VALOR_ID) JOIN RENTABILIDAD ON COD_RENTABILIDAD = RENTABILIDAD_ID WHERE RENTABILIDAD.NOMBRE = '"+tipoRentabilidad+"'";
			if(!tipoRentabilidad.equals( "---" ) && !nomValor.equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN RENTABILIDAD ON COD_RENTABILIDAD = RENTABILIDAD_ID WHERE RENTABILIDAD.NOMBRE = '"+tipoRentabilidad+"'";
		}else{
			consulta = "SELECT * FROM (SELECT * FROM OPERACION WHERE FECHA_ORDEN > to_date('"+fechaInicial+"','DD/MM/YYYY') AND FECHA_ORDEN < to_date('"+fechaFinal+"','DD/MM/YYYY')) JOIN VALOR ON COD_VALOR = VALOR_ID";
			//CORE: SELECT * FROM OPERACION WHERE FECHA_ORDEN > to_date(fechaInicial,'DD/MM/YYYY') AND FECHA_ORDEN < to_date(fechaFinal,'DD/MM/YYYY')
			if(!tipoOperacion .equals( "---" ))consulta = consulta + " WHERE TIPO_COMPRA_VENTA != '"+tipoOperacion+"'";
			consulta = (!correoOfInv .equals( "---" ) && tipoOperacion .equals( "---"))?consulta + " WHERE COD_SOLICITANTE != '"+correoOfInv+"'":consulta + " AND COD_SOLICITANTE != '"+correoOfInv+"'";
			consulta = (!nomValor .equals( "---" ) && tipoOperacion .equals( "---" ) && correoOfInv .equals( "---" ))?consulta + " WHERE NOMBRE != '"+nomValor+"'":consulta +  " AND NOMBRE != '"+nomValor+"'";
			if(!correoIntermediario .equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN OPERACIONES_INT ON OPERACION_ID = COD_OPERACION WHERE COD_INTERMEDIARIO != '"+correoIntermediario+"'";
			if(!tipoValor .equals( "---" ) && nomValor .equals( "---"))consulta = "SELECT * FROM (SELECT * FROM ("+consulta+") JOIN VALOR ON COD_VALOR = VALOR_ID WHERE NOMBRE != '"+nomValor+"') JOIN TIPO_VALOR ON COD_TIPO_VALOR = TIPO_VALOR_ID WHERE TIPO_VALOR.NOMBRE != '"+tipoValor+"'";
			if(!tipoValor .equals( "---" ) && !nomValor .equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN TIPO_VALOR ON COD_TIPO_VALOR = TIPO_VALOR_ID WHERE TIPO_VALOR.NOMBRE != '"+tipoValor+"'";
			if(!tipoRentabilidad .equals( "---" ) && nomValor .equals( "---"))consulta = "SELECT * FROM (SELECT * FROM ("+consulta+") JOIN VALOR ON COD_VALOR = VALOR_ID WHERE NOMBRE != '"+nomValor+"') JOIN RENTABILIDAD ON COD_RENTABILIDAD = RENTABILIDAD_ID WHERE RENTABILIDAD.NOMBRE != '"+tipoRentabilidad+"'";
			if(!tipoRentabilidad .equals( "---" ) && !nomValor .equals( "---" ))consulta = "SELECT * FROM ("+consulta+") JOIN RENTABILIDAD ON COD_RENTABILIDAD = RENTABILIDAD_ID WHERE RENTABILIDAD.NOMBRE != '"+tipoRentabilidad+"'";
		}
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			System.out.println(consulta);
			while(rs.next()){
				OperacionValue op = new OperacionValue();
				op.setFecha(rs.getDate(1));
				op.setTipoCompraVenta(rs.getString(2));
				op.setNomValor(rs.getString(11));
				op.setCorSolicitante(rs.getString(4));
				op.setCantidad(rs.getInt(7));
				rta.add(op);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * @return 
	 * @throws SQLException 
	 * 
	 */
	public ArrayList<PortafolioValue> consultarPortafolios(String tipoValor, int valorMayor) throws SQLException{
		ArrayList<PortafolioValue> rta = new ArrayList<PortafolioValue>();
		PreparedStatement state = null;
		String consulta = "SELECT * FROM (SELECT * FROM (SELECT * FROM PORTAFOLIO JOIN PORTAFOLIO_VALOR ON PORTAFOLIO_ID = COD_PORTAFOLIO)a JOIN (SELECT VALOR_ID FROM VALOR JOIN TIPO_VALOR ON TIPO_VALOR_ID = COD_TIPO_VALOR WHERE TIPO_VALOR.NOMBRE = '"+tipoValor+"')b ON a.COD_VALOR = b.VALOR_ID) JOIN OPERACION ON VALOR_ID = COD_VALOR WHERE CANTIDAD > "+valorMayor;	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			System.out.println(consulta);
			while(rs.next()){
				PortafolioValue op = new PortafolioValue();
				op.setCorreoInter(rs.getString("COD_INTERMEDIARIO"));
				op.setNombre(rs.getString("NOMBRE"));
				op.setTipoRiesgo(rs.getString("TIPO_RIESGO"));
				rta.add(op);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	/**
	 * 
	 * @param idValor
	 * @return 
	 * @throws SQLException 
	 */
	public ArrayList<PortafolioValue> consultarValorAlt(String idValor) throws SQLException{
		//El parametro siempre es diferente de "---"
		ArrayList<PortafolioValue> rta = new ArrayList<PortafolioValue>();
		PreparedStatement state = null;
		String consulta = "SELECT * FROM PORTAFOLIO_VALOR JOIN PORTAFOLIO ON COD_PORTAFOLIO = PORTAFOLIO_ID WHERE COD_VALOR = '"+idValor+"'";	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			System.out.println(consulta);
			while(rs.next()){
				PortafolioValue op = new PortafolioValue();
				op.setCorreoInter(rs.getString("COD_INTERMEDIARIO"));
				op.setNombre(rs.getString("NOMBRE"));
				op.setTipoRiesgo(rs.getString("TIPO_RIESGO"));
				rta.add(op);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}

	//------------------------------------------------------------------------
	//Consultas Iteracion 5.
	//------------------------------------------------------------------------
	/**
	 * Consulta de los valores mas dinamicos.
	 * Consulta los valores mas dinamicos entre un rango de fecha, en esta y en la base de datos asociada.
	 * @params String FechaInicial,
	 * @params String FechaFinal,
	 */
	public ArrayList<ValorValue> valoresMDinamicos(String fechaInicial, String fechaFinal)throws Exception{
		ArrayList<ValorValue> rta = new ArrayList<ValorValue>();
		PreparedStatement state = null;
		
		String consulta = "SELECT VALOR.* FROM VALOR JOIN (SELECT COD_VALOR FROM(SELECT COUNT (COD_VALOR)CANT, COD_VALOR FROM OPERACION WHERE FECHA_ORDEN > to_date('"+fechaInicial+"','DD/MM/YYYY') AND FECHA_ORDEN < to_date('"+fechaFinal+"','DD/MM/YYYY') GROUP BY COD_VALOR)WHERE (SELECT MAX(COUNT(COD_VALOR))CANT2 FROM OPERACION GROUP BY COD_VALOR) = CANT) ON COD_VALOR = VALOR_ID";	
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				ValorValue valor = new ValorValue();
				valor.setCreador(rs.getString("cod_oferente_creador"));
				valor.setDisponible((rs.getString("disponible").equals("T"))?true:false);
				valor.setFechaExpiracion(rs.getDate("fecha_expiracion"));
				valor.setMercado(rs.getString("mercado"));
				valor.setNombre(rs.getString("nombre"));
				valor.setPrecio(rs.getInt("precio"));
				valor.setCodigo(rs.getInt("valor_id"));
				rta.add(valor);
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}
	//------------------------------------------------------------------------
	//GENERADOR DE IDS.
	//------------------------------------------------------------------------
	/**
	 * Genera una nueva id dado el nombre de la tabla.
	 * @param nombreTabla, El nombre de la tabla a generar una id, el nombre de la tabla debe ser una de las siguientes:
	 * -OPERACION
	 * -PORTAFOLIO
	 * -RENTABILIDAD
	 * -VALOR
	 * -TIPO_VALOR
	 */
	public int generarId(String nombreTabla)throws SQLException{
		int rta = 0;
		PreparedStatement state = null;
		String consulta = "SELECT MAX("+nombreTabla+"_ID) AS "+nombreTabla+"_ID FROM "+nombreTabla;
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			while(rs.next()){
				rta = 1 + rs.getInt(nombreTabla+"_ID");
			}
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
			throw e;
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					throw e;
				}
			}
			cerrarConexion(conexion);
		}
		return rta;
	}


	// LO QUE VA DENTRO DE TODOS LOS METODOS DE DAO.
	//	ArrayList<String> select = new ArrayList<String>();
	//	ArrayList<String> where = new ArrayList<String>();
	//	ArrayList<String> order = new ArrayList<String>();
	//	PreparedStatement state = null;
	//	String consulta = creadorDeSentencias(select, "Tabla", where, order);	
	//	try{
	//		establecerConexion(cadenaConexion, usuario, clave);
	//		state = conexion.prepareStatement(consulta);
	//		ResultSet rs = state.executeQuery();
	//		while(rs.next()){
	//			
	//		}
	//	}catch(SQLException e){
	//		e.printStackTrace();
	//		System.out.println(consulta);
	//		throw e;
	//	}finally{
	//		if(state != null){
	//			try{
	//				state.close();
	//			}catch(SQLException e){
	//				throw e;
	//			}
	//		}
	//		cerrarConexion(conexion);
	//	}

	//GENERIC INSERT
	//	"INSERT INTO TABLA VALUES ("" , '" + s1 + "', " +  s2 + ")"
	//
	//	GENERIC UPDATE
	//	"UPDATE TABLA SET column = value WHERE comp = acomp"
	//	
	//	GENERIC DELETE
	//	"DELETE FROM TABLA WHERE comp = acomp"

	//-----------------------------------------------------------------
	//-----------------------JMS-Receiver&Sender-----------------------------
	//-----------------------------------------------------------------
	/**
	 * Recibe un mensaje JMS de otra aplicacion.
	 */
	public void onMessage(Message m){
		String mensaje[];
		try {
			mensaje = ((TextMessage)m).getText().split(";");
			String req = mensaje[0];
			if(req.equals("RF14")){

			}else if(req.equals("RF15")){
				try{
					retirarIntermediario(mensaje[1],mensaje[2]);
					send("SUCCESS;RF15");
				}catch(Exception e){
					try {
						send("ERROR;RF15;"+e.getMessage());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.out.println(e.getMessage());
				}
			}else if(req.equals("RFC12")){
				String rta = "ResultRFC12;";
				try{
					ArrayList<OperacionValue> ops = consultarMovimientos(mensaje[1], mensaje[2], (mensaje[3].equals("True"))?true:false, mensaje[4], mensaje[5], mensaje[6], mensaje[7], mensaje[8], mensaje[9]);
					for (OperacionValue operacionValue : ops) {
						rta = rta + ";" +operacionValue.toString();
					}
					send(rta);
				}catch(Exception e){
					try {
						send("ERROR;RFC12;"+e.getMessage());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.out.println(e.getMessage());
				}
			}else if(req.equals("RFC13")){
				String rta = "ResultRFC13;";
				try{
					ArrayList<ValorValue> ops = valoresMDinamicos(mensaje[1], mensaje[2]);
					for (ValorValue valorValue : ops) {
						rta = rta + ";" +valorValue.toString();
					}
					send(rta);
				}catch(Exception e){
					try {
						send("ERROR;RFC13;"+e.getMessage());
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					System.out.println(e.getMessage());
				}
			}else if(req.equals("ResultRFC12")){
				for(String val : mensaje){
					String data[] = val.split("#");
					OperacionValue op = new OperacionValue();
					op.setCantidad(Integer.parseInt(data[1]));
					SimpleDateFormat form = new SimpleDateFormat("dd/MM/yyyy");
					java.util.Date fecha = null;
					try {
						fecha = form.parse(data[2]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					op.setFecha(fecha);
					op.setTipoCompraVenta(data[3]);
					op.setCorSolicitante(data[4]);
					op.setCorIntermediario(data[5]);
					op.setNomValor(data[6]);
					rfc12Result.add(op);
				}
			}else if(req.equals("ResultRFC13")){
				for(String val: mensaje){
					String data[] = val.split("#");
					ValorValue op = new ValorValue();
					op.setNombre(data[1]);
					op.setPrecio(Integer.parseInt(data[2]));
					op.setNomTipoValor(data[3]);
					rfc13Result.add(op);
				}
			}else if(req.equals("ERROR")){
				String reqFallido = mensaje[1];
				if(reqFallido.equals("RF14")){
					RF14 = "ERROR";
				}else if(reqFallido.equals("RF15")){
					RF15 = "ERROR";
				}else if(reqFallido.equals("RFC12")){
					RFC12 = "ERROR";
				}else if(reqFallido.equals("RFC13")){
					RFC13 = "ERROR";
				}
			}else if(req.equals("SUCCESS")){
				String reqSucceded = mensaje[1];
				if(reqSucceded.equals("RF14")){
					RF14 = "SUCCES";
				}else if(reqSucceded.equals("RF15")){
					RF15 = "SUCCES";
				}
			}else{
				System.out.println("Error en el header de un mensaje.");
			}
		} catch (JMSException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Sender.
	 * Se encarga de enviar un mensaje por el JMS.
	 * @params String mensaje, mensaje a enviar por el JMS.
	 */
	public void send(String mensaje) throws Exception{
		DAOSender sender = new DAOSender();
		sender.send(mensaje);
	}
}

