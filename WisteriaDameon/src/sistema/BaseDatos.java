package sistema;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;




public class BaseDatos {

	private static final Logger log = Logger.getLogger("Dameon");

	Connection cadena_conexion; 

	int registro;
	int codigo_modo;



	public BaseDatos(Configuracion configuracion) 
	{

		try 
		{
			Class.forName("com.mysql.jdbc.Driver");
			cadena_conexion = DriverManager.getConnection ("jdbc:mysql://" + configuracion.getBDServidor() + "/" + configuracion.getBDName(),configuracion.getBDUsuario(), configuracion.getBDPassword());

			log.debug ("BBDD " + cadena_conexion);
		}
		catch (Exception e)
		{
			log.error ("Error BBDD " + e);
		}		



	}


	public String obtenerOrden()
	{
		String res = "";


		log.debug("obtenerOrden");
		try
		{

			log.debug ("BBDD obtenerOrden");
			Statement s = cadena_conexion.createStatement(); 
			ResultSet rs = s.executeQuery ("SELECT MODO,TEMPERATURA,T2.COD_MODO,COD_INSTRUCCION,NOTIFICAR,OPCIONES_MODO FROM MODOFUNCIONAMIENTO AS T1,INSTRUCCION AS T2 WHERE T1.COD_MODO=T2.COD_MODO order by cod_instruccion desc limit 1");



			
			// Recorremos el resultado, mientras haya registros para leer, y escribimos el resultado en pantalla. 
			while (rs.next()) 
			{ 				 

				codigo_modo = Integer.parseInt(rs.getString(3));
				
				res = rs.getString(1) + ";" +rs.getString(2) + ";" +rs.getString(4) + ";" + rs.getBoolean(5) + ";" + rs.getString(6);						
				return res;

			}



		} catch (Exception e)
		{			
			log.error(e);			
		}


		return res;


	}

	public void cerrar(float  temp_fin)
	{
		String query = "UPDATE REGISTROUSO SET FIN=now(), TEMP_FIN='" + temp_fin + "' WHERE COD_REGISTRO=" + registro;

		log.debug ("query de cerrar " + query);

		try
		{

			PreparedStatement preparedStmt = cadena_conexion.prepareStatement(query);
			//Statement s = cadena_conexion.createStatement();

			preparedStmt.execute();
		}catch (Exception e)

		{
			log.error("Fallo actualizacion de la BBDD " + e);			
			e.printStackTrace();
		}



	}

	public void abrir(Float temp) 
	{

		try
		{
			String query = "INSERT INTO REGISTROUSO (COD_MODO,INICIO,TEMP_INICIO) values (1,now()," + temp + ")";		

			PreparedStatement preparedStmt = cadena_conexion.prepareStatement(query);


			Statement s = cadena_conexion.createStatement();

			preparedStmt.execute();


			query = "SELECT @@identity AS id";

			//Statement s = cadena_conexion.createStatement(); 
			ResultSet rs = s.executeQuery (query);


			if (rs.next()) 
			{ 

				registro = Integer.parseInt(rs.getString(1));
				log.debug ("Devuelvo: " + registro);
			}

			log.debug ("Query es " + query);
		}catch (Exception e )
		{


			e.printStackTrace();
		}


	}
	
	public Boolean insertarSistema(float temperatura, float humedad, Double tempertura_externa, String rele, float temperatura_climatizacion, float temperatura_habitacion, float humedad_habitacion) 
	{
		String query = "INSERT INTO SISTEMA (FECHA,COD_MODO,RELE,TEMP_SALON,HUMEDAD_SALON,TEMP_EXTERNA,TEMP_CLIMATIZADOR,TEMP_HABITACION, HUMEDAD_HABITACION) values (now(),?,?,?,?,?,?,?,?)";
		
		
		try
		{
				
			PreparedStatement preparedStmt = cadena_conexion.prepareStatement(query);

			preparedStmt.setInt(1, codigo_modo);
			preparedStmt.setString(2, rele);
			preparedStmt.setFloat(3, temperatura);
			preparedStmt.setFloat(4, humedad);
			preparedStmt.setDouble(5, tempertura_externa);
			preparedStmt.setFloat(6, temperatura_climatizacion);
			preparedStmt.setFloat(7, temperatura_habitacion);
			preparedStmt.setFloat(8, humedad_habitacion);
			
			preparedStmt.execute();

			
			log.debug ("Insertada informacion en la BBDD correctamente");			
					
			
		}catch (Exception e )
		{
			log.error ("Fallo actualizar sistema BBDD " + e);
			log.error ("query:" + query);					
			return false;			
		}

		return true;

	}
}