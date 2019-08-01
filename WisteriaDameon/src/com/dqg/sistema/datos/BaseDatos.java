package com.dqg.sistema.datos;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.log4j.Logger;

import com.dqg.config.Configuracion;




public class BaseDatos {

	private static final Logger log = Logger.getLogger("Dameon");

	Connection cadena_conexion; 

	int registro;
	int codigo_modo;



	public BaseDatos(Configuracion configuracion) 
	{

		try 
		{
			log.debug("Conexion con MariaDB");
			//Class.forName("com.mysql.jdbc.Driver");
			//cadena_conexion = DriverManager.getConnection ("jdbc:mysql://" + configuracion.getBDServidor() + "/" + configuracion.getBDName(),configuracion.getBDUsuario(), configuracion.getBDPassword());
			cadena_conexion = DriverManager.getConnection("jdbc:mariadb://" + configuracion.getBDServidor() + "/" + configuracion.getBDName(),configuracion.getBDUsuario(), configuracion.getBDPassword());
			
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
			ResultSet rs = s.executeQuery ("SELECT MODO,TEMPERATURA,T2.COD_MODO,COD_INSTRUCCION,true,OPCIONES_MODO FROM MODOFUNCIONAMIENTO AS T1,INSTRUCCION AS T2 WHERE T1.COD_MODO=T2.COD_MODO order by cod_instruccion desc limit 1");




			// Recorremos el resultado, mientras haya registros para leer, y escribimos el resultado en pantalla. 
			if (rs.next()) 
			{ 				 

				codigo_modo = Integer.parseInt(rs.getString(3));

				res = rs.getString(1) + ";" +rs.getString(2) + ";" +rs.getString(4) + ";" + rs.getBoolean(5) + ";" + rs.getString(6);										
			}else
				log.debug("No se ha recibido nada de la BBDD");



		} catch (Exception e)
		{			
			log.error("Fallo obtener orden: " + e);			
		}


		log.debug("Fin obtener orden");
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

	public Boolean insertarSistema(float temperatura, float humedad, Double tempertura_externa, String rele, float temperatura_climatizacion, float temperatura_habitacion, float humedad_habitacion, float temperatura_habitacion1, float humedad_habitacion1,float temperatura_habitacion2, float humedad_habitacion2, float temp_rapsberry, String opciones_modo, float temp_media) 
	{
		String query = "INSERT INTO SISTEMA (FECHA,COD_MODO,RELE,TEMP_SALON,HUMEDAD_SALON,TEMP_EXTERNA,TEMP_CLIMATIZADOR,TEMP_DORMITORIO, HUMEDAD_DORMITORIO,TEMP_HABITACION1, HUMEDAD_HABITACION1,TEMP_HABITACION2, HUMEDAD_HABITACION2,TEMP_RASPI,OPCIONES_MODO, TEMP_MEDIA) values (now(),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";


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

			preparedStmt.setFloat(9, temperatura_habitacion1);
			preparedStmt.setFloat(10, humedad_habitacion1);
			preparedStmt.setFloat(11, temperatura_habitacion2);
			preparedStmt.setFloat(12, humedad_habitacion2);

			preparedStmt.setFloat(13, temp_rapsberry);
			preparedStmt.setString(14, opciones_modo);

			// Temperatura media
			preparedStmt.setFloat(15, temp_media);
			
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
	public Boolean insertarInstruccion(int codigo_modo,float temperatura,String usuario, String opciones_modo) 
	{
		String query = "INSERT INTO INSTRUCCION (COD_MODO,TEMPERATURA,USUARIO,FECHA,OPCIONES_MODO)"
				+ "values (?,?,?,now(),?)";

		try
		{

			PreparedStatement preparedStmt = cadena_conexion.prepareStatement(query);
			preparedStmt.setInt(1, codigo_modo);
			preparedStmt.setFloat(2, temperatura);
			preparedStmt.setString(3, usuario);									
			preparedStmt.setString(4, opciones_modo);

			preparedStmt.execute();

			log.debug ("Insertada informacion en la BBDD correctamente");			


		}catch (Exception e )
		{
			log.error ("Fallo insertarInstruccion " + e);
			e.printStackTrace();
			log.error ("query:" + query);					
			return false;			
		}

		return true;

	}

}	