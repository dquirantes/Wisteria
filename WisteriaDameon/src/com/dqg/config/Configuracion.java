package com.dqg.config;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.log4j.Logger;


public class Configuracion {

	private static final Logger log = Logger.getLogger("Dameon");

//	private String programa_abrir;
//	private String programa_cerrar;
	private String programa_sensor;
//	private String programa_notificaciones;
	private String programa_placa;
	private String url_tiempo;


	private String fichero_temporal;

	private String bd_name;
	private String bd_servidor;
	private String bd_puerto;
	private String bd_usuario;
	private String bd_password;

	private boolean enviarCorreo;
	private String correo_from;
	private String correo_to;	
	private String correo_usuario;
	private String correo_password;
	private String correo_host;
	private String correo_puerto;
	private String correo_asunto;


	private String sensorSalon;
	private String sensorDormitorio;
	private String sensorHabitacion1;
	private String sensorHabitacion2;

	private float tempMaximaPlaca;

	private long tActuador;
	private long tSensor;	
	private long tExterno;
	private long tBucle;
	private long tRegistro;
	//private long tRegistroWeb;
	//private long tNotificaciones;
	private long tEstado;

	private float tmp_Margen;

	//private String fichero_web;

	//private int puerto_Web;

//	private double latitud_casa;
//	private double longitud_casa;

	private String ficheroEstado;

	private String ShellyEstado;
	private String ShellyEnceder;
	private String ShellyApagar;
	
	private String urlTelegram;
	private String urlMosquito;
	
	public String getUrlMosquito() {
		return urlMosquito;
	}

	public void setUrlMosquito(String urlMosquito) {
		this.urlMosquito = urlMosquito;
	}

	public String getUrlTelegram() {
		return urlTelegram;
	}

	public void setUrlTelegram(String urlTelegram) {
		this.urlTelegram = urlTelegram;
	}

	public String getShellyEstado() {
		return ShellyEstado;
	}

	public void setShellyEstado(String shellyEstado) {
		ShellyEstado = shellyEstado;
	}

	public String getShellyEnceder() {
		return ShellyEnceder;
	}

	public void setShellyEnceder(String shellyEnceder) {
		ShellyEnceder = shellyEnceder;
	}

	public String getShellyApagar() {
		return ShellyApagar;
	}

	public void setShellyApagar(String shellyApagar) {
		ShellyApagar = shellyApagar;
	}

	public Configuracion(String pathProperties) {

		try
		{
			Properties prop = new Properties();
			prop.load(new FileInputStream(pathProperties));


			//programa_abrir = prop.getProperty("programa_abrir");	
			//programa_cerrar = prop.getProperty("programa_cerrar");
			programa_sensor = prop.getProperty("programa_sensor");
			//programa_notificaciones = prop.getProperty("programa_notificaciones");
			programa_placa = prop.getProperty("programa_placa");


			url_tiempo = prop.getProperty("url_tiempo");

			fichero_temporal = prop.getProperty("fichero_temporal");

			bd_name = prop.getProperty("bd_name");
			bd_servidor = prop.getProperty("bd_servidor");
			bd_puerto = prop.getProperty("bd_puerto");
			bd_usuario = prop.getProperty("bd_usuario");
			bd_password = prop.getProperty("bd_password");

			enviarCorreo = Boolean.parseBoolean(prop.getProperty("enviar_correo"));			
			correo_from = prop.getProperty("correo_from");
			correo_to = prop.getProperty("correo_to");
			correo_usuario = prop.getProperty("correo_usuario");
			correo_password = prop.getProperty("correo_password");
			correo_host = prop.getProperty("correo_host");			
			correo_puerto = prop.getProperty("correo_puerto");			
			correo_asunto = prop.getProperty("correo_asunto");			


			sensorSalon = prop.getProperty("sensorSalon");
			sensorDormitorio = prop.getProperty("sensorDormitorio");
			sensorHabitacion1 = prop.getProperty("sensorHabitacion1");
			sensorHabitacion2 = prop.getProperty("sensorHabitacion2");

			tempMaximaPlaca = Float.parseFloat(prop.getProperty("tempMaximaPlaca"));
			tmp_Margen = Float.parseFloat(prop.getProperty("tmp_Margen"));


			tActuador= Long.parseLong(prop.getProperty("tActuador"));
			tSensor= Long.parseLong(prop.getProperty("tSensor"));
			tExterno= Long.parseLong(prop.getProperty("tExterno"));
			tBucle= Long.parseLong(prop.getProperty("tBucle"));
			tRegistro= Long.parseLong(prop.getProperty("tRegistro"));
			//tRegistroWeb= Long.parseLong(prop.getProperty("tRegistroWeb"));
			//tNotificaciones= Long.parseLong(prop.getProperty("tNotificaciones"));
			tEstado= Long.parseLong(prop.getProperty("tEstado"));
			

			//fichero_web = prop.getProperty("fichero_web");
			//puerto_Web= Integer.parseInt(prop.getProperty("puerto_Web"));

			//latitud_casa= Double.parseDouble(prop.getProperty("latitud_casa"));
			//longitud_casa= Double.parseDouble(prop.getProperty("longitud_casa"));

			ficheroEstado= prop.getProperty("ficheroEstado");
			
			ShellyEstado= prop.getProperty("ShellyEstado");
			ShellyEnceder= prop.getProperty("ShellyEnceder");
			ShellyApagar= prop.getProperty("ShellyApagar");
			
			urlTelegram= prop.getProperty("urlTelegram");
			
			urlMosquito= prop.getProperty("urlMosquito");



		}catch (Exception e)
		{
			log.error ("Fallo en la configuración. Revise el fichero: " + pathProperties);
			e.printStackTrace();
			System.exit (-1);
		}


	}

	public boolean getEnviarCorreo()
	{
		return enviarCorreo;
	}

	public Float gettmp_Margen()
	{
		return tmp_Margen;
	}
	public long gettActuador()
	{
		return tActuador;
	}

	public long gettSensor()
	{
		return tSensor;
	}

	public long gettExterno()
	{
		return tExterno;
	}

	public long gettBucle()
	{
		return tBucle;
	}

	/*public long gettNotificaciones()
	{
		return tNotificaciones;
	}*/

	public long gettRegistro()
	{
		return tRegistro;
	}

	
	public Float get_tempMaximaPlaca()
	{
		return tempMaximaPlaca;
	}
	public String getSensorSalon()
	{
		return sensorSalon;
	}

	public String getSensorDormitorio()
	{
		return sensorDormitorio;
	}

	public String getSensorHabitacion1()
	{
		return sensorHabitacion1;
	}


	public String getSensorHabitacion2()
	{
		return sensorHabitacion2;
	}




	public String getCorrreoAsunto()
	{
		return correo_asunto;
	}

	public String getCorrreoTo()
	{
		return correo_to;
	}

	public String getCorrreoFrom()
	{
		return correo_from;
	}



	public String getCorrreoUsuario()
	{
		return correo_usuario;
	}

	public String getCorrreoPuerto()
	{
		return correo_puerto;
	}	

	public String getCorrreoPassword()
	{
		return correo_password;
	}	
	public String getCorrreoHost()
	{
		return correo_host;
	}
	

	public String getProgramaSensor()
	{
		return programa_sensor;
	}

	public String getURLTiempo()
	{
		return url_tiempo;	
	}

	public String getBDPassword()
	{
		return bd_password;		
	}

	public String getBDUsuario()
	{
		return bd_usuario;		
	}


	public String getBDPuerto()
	{
		return bd_puerto;		
	}

	public String getBDName()
	{
		return bd_name;		
	}

	public String getBDServidor()
	{
		return bd_servidor;		
	}

	public String getFicheroTemporal()
	{
		return fichero_temporal;		
	}

	public String getProgramaPlaca()
	{
		return programa_placa;
	}	
	

	public String getFicheroEstado()
	{
		return this.ficheroEstado;
	}
	
	public long getTestado()
	{
		return this.tEstado;
	}
	
}

