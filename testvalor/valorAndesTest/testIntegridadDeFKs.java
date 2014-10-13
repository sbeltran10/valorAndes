package valorAndesTest;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import junit.framework.TestCase;
public class testIntegridadDeFKs extends TestCase{
	private Connection con;
	private String usuario;
	private String clave;
	private String cadenaConexion;
	/**
	 * Obtiene los datos necesarios para establecer una conexion
	 * Los datos se obtienen a partir de un archivo properties.
	 * @param path ruta donde se encuentra el archivo properties.
	 */
	public void inicializar(String path){
		try{
			File file = new File ("C:/Users/Samuel/Documents/Uniandes/Sistrans/JBoss/jboss-4.2.2.GA/server/default/data/valorAndes/conexion.properties");
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
			con = DriverManager.getConnection(url, usuario, clave);
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

	public void testFKSValor(){		
		PreparedStatement state = null;
		String consulta = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','13','9','dummyInv','dummy','dummy','1','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta);
			state.execute(consulta);
		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					fail("No deberia arrojar excepcion.");
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String consulta2 = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','14','9','aaa','dummy','dummy','1','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta3 = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','15','9','dummyInv','aaaa','dummy','1','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta3);
			state.execute(consulta3);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta3);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta4 = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','16','9','dummyInv','dummy','aaaaa','1','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta4);
			state.execute(consulta4);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta4);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta5 = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','17','9','dummyInv','dummy','dummy','0','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta6 = "INSERT INTO VALOR VALUES ('aa','11-11-2011','p','aa','18','9','dummyInv','dummy','dummy','1','0')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void testOperacionFKs(){
		PreparedStatement state = null;
		String consulta = "INSERT INTO OPERACION VALUES ('1','11-11-2011','11','VENTA','6','aaa','dummyOf','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta);
			state.execute(consulta);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta2 = "INSERT INTO OPERACION VALUES ('1','11-11-2011','11','VENTA','6','dummyInv','aaa','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta3 = "INSERT INTO OPERACION VALUES ('1','11-11-2011','11','VENTA','6','dummyInv','dummyOf','9')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta3);
			state.execute(consulta3);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta3);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void testOferenteFKs(){
		PreparedStatement state = null;
		String consulta1 = "INSERT INTO OFERENTE VALUES ('aa','aaaa','dummyInt','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta1);
			state.execute(consulta1);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta1);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		String consulta2 = "INSERT INTO OFERENTE VALUES ('aa','prueba','aaaa','1')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		String consulta3 = "INSERT INTO OFERENTE VALUES ('aa','prueba','dummyInt','0')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta3);
			state.execute(consulta3);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta3);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void testInversionistaFKs(){
		PreparedStatement state = null;
		String consulta1 = "INSERT INTO INVERSIONISTA VALUES ('aa','aaaaaa','prueba')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta1);
			state.execute(consulta1);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta1);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		String consulta2 = "INSERT INTO INVERSIONISTA VALUES ('aa','dummyInt','aaaaaaa')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta2);
			state.execute(consulta2);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta2);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public void testIntermediarioFKs(){
		PreparedStatement state = null;
		String consulta1 = "INSERT INTO INTERMEDIARIO VALUES ('aa','aa','aaa')";
		try{
			establecerConexion(cadenaConexion, usuario, clave);
			state = con.prepareStatement(consulta1);
			state.execute(consulta1);

		}catch(SQLException e){
			e.printStackTrace();
			System.out.println(consulta1);
		}finally{
			if(state != null){
				try{
					state.close();
				}catch(SQLException e){
					//Debe pasar por aca.
				}
			}
			try {
				cerrarConexion(con);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
