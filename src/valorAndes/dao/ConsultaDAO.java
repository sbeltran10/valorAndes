package valorAndes.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import valorAndes.vos.IntermediarioValue;
import valorAndes.vos.InversionistaValue;
import valorAndes.vos.OferenteValue;
import valorAndes.vos.OperacionValue;
import valorAndes.vos.ValorValue;

public class ConsultaDAO {
	//------------------------------------------
	//Constantes
	//------------------------------------------
	/**
	 * Ruta donde se encuentra el archivo de conexion.
	 */
	private static final String ARCHIVO_CONEXION = "/conexion.properties";

	/**
	 * Conexion con la base de datos.
	 */
	public Connection conexion;

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

	/**
	 * Constructor de la clase.
	 */
	public ConsultaDAO(){

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
	public void cerrarConexion(Connection con)throws SQLException{
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
	 * Da el numero total de operaciones en el sistema
	 */
	public int darTotalOperaciones() throws SQLException{
		int total = 0;
		PreparedStatement state = null;
		ArrayList<String>select = new ArrayList<String>();
		ArrayList<String>where = new ArrayList<String>();
		ArrayList<String>order = new ArrayList<String>();
		select.add("count(*) AS total");
		String consulta = creadorDeSentencias(select, "OPERACION", where, order);
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = conexion.prepareStatement(consulta);
			ResultSet rs = state.executeQuery();
			if(rs.next()){
				total = rs.getInt("total");
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
		return total;
	}

	//-----------------------------------------------------------
	//Metodos asociados a los Insert, Deletes, Updates.
	//-----------------------------------------------------------
	/**
	 * Ordena una operacion bursatil.
	 */
	public boolean ordenarOperacion(OperacionValue op, String codIntermediario) throws SQLException{
		PreparedStatement state = null;
		String fechi = new SimpleDateFormat("dd-MM-YYYY").format(op.getFecha());
		String consulta = "INSERT INTO OPERACION VALUES (TO_DATE('"+ fechi + "', 'DD-MM-YYYY') , '" + op.getTipoCompraVenta() + "', "
				+  op.getIdValor() + ", '" + op.getCorSolicitante() + "', 'No Registrada', " + op.getId() + ", " + op.getCantidad() + ")";
		String consulta2 = "INSERT INTO OPERACIONES_INT VALUES ("+ op.getId() + ", '" +  codIntermediario + "')";
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


		if(op.getTipoCompraVenta().equals("VENTA")){
			consultaUpValor = "UPDATE VALOR SET disponible = 'T' WHERE valor_id = "+op.getIdValor();
			consultaUpOperacion = "UPDATE OPERACION SET estado = 'Registrada' WHERE operacion_id = "+op.getId();

			String[] cantidadDisp = darCantidadRestante(op.getIdValor(), op.getCorSolicitante() + "").split("-");

			int cant = Integer.parseInt(cantidadDisp[0]);
			if(cant > op.getCantidad()){

				consultaUpValorPropietarios = "UPDATE VALOR_PROPIETARIOS SET cantiad_valor = (cantiad_valor - "+ op.getCantidad()  +  ")  WHERE correo_propietario = '"+op.getCorSolicitante()
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
			}
			else{
				rta = "Ya no se encuentra disponible la cantidad deseada a comprar del valor, la operacion de compra sera cancelada"
						+ ", si lo desea puede ordenar una nueva orden de compra por una cantidad menor.";
			}
			if(rta.isEmpty()){

				if(tieneValor(op.getCorSolicitante(), op.getIdValor())){
					consultaUpValorPropietarios = "UPDATE VALOR_PROPIETARIOS SET cantiad_valor = (cantiad_valor + "+ op.getCantidad()  +  ")  WHERE correo_propietario = '"+op.getCorSolicitante()
							+ "' AND valor_id = " + op.getIdValor();
				}
				else{
					consultaUpValorPropietarios = "INSERT INTO VALOR_PROPIETARIOS VALUES ('"+ op.getCorSolicitante() + "', " +  op.getIdValor() + ", "+
							op.getCantidad() + ", 0, 'F')";
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
					String tipoPort = rs1.getString("TIPO_PORTAFOLIO");
					invVal.setTipoPortafolio(tipoPort);

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
				String tipoPort = rs2.getString("TIPO_PORTAFOLIO");
				invOf.setTipoPortafolio(tipoPort);

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
}