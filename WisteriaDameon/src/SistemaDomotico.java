
enum EstadoRele {ABIERTO, CERRADO};
enum ModoSistema{ON, OFF, CLIMATIZADOR};
enum OpcionesModo{SALON, DORMITORIO,HABITACION1, HABITACION2,  MEDIA, MINIMO, MAXIMO};

enum ErroresSistema{BBDD, SENSORES,ACTUADOR, REGISTRO, TEMP_PLACA, SENSOR_PLACA};


public class SistemaDomotico {



	private float temp;
	private Double temp_ext;	
	private float temp_climatizador;
	private float humedad;	
	private EstadoRele rele = EstadoRele.CERRADO;
	private ModoSistema modo;
	private OpcionesModo opciones_modo;
	
	private boolean alcanzoTemperatura = false;
	private ErroresSistema errorSistema;

	private float temp_dormitorio;
	private float humedad_dormitorio;

	private float temp_habitacion1;
	private float humedad_habitacion1;
	
	private float temp_habitacion2;
	private float humedad_habitacion2;
	
	private float temp_raspi;
	
	private boolean enviarNotificaciones;
	
	
	
	
	public void setEnviarNotificaciones(boolean enviar)
	{
		this.enviarNotificaciones = enviar;		
	}
	public Boolean getEnviarNotificaciones()
	{
		return enviarNotificaciones;
	}
	public void setErrorSistema(ErroresSistema error)
	{
		this.errorSistema = error;
	}
	
	public ErroresSistema getErrorSistema()
	{
		return errorSistema;
	}
	


	public void setAlcanzoTemperatura (boolean alcanzoTemperatura )
	{
		this.alcanzoTemperatura = alcanzoTemperatura ;
	}


	public boolean getAlcanzoTemperatura ()
	{
		return  alcanzoTemperatura;
	}


	
	public Double getTempExterna()
	{
		return temp_ext;
	}
	public float getHumedad()
	{
		return humedad;
	}
	public float getHumedad_dormitorio()
	{
		return humedad_dormitorio;
	}
	public void setModoSistema (ModoSistema mod)
	{
		modo = mod;
	}
	public void setEstadoRele (EstadoRele estado)
	{
		rele = estado;
	}
	public EstadoRele getEstadoRele()
	{
		return rele;
	}

	public float getTemperatura_Climatizador()
	{
		return temp_climatizador;
	}

	public float getTemperatura()
	{
		return temp;
	}
	public float getTemperatura_habitacion1()
	{
		return temp_habitacion1;
	}
	public float getTemperatura_habitacion2()
	{
		return temp_habitacion2;
	}
	public float getTemperatura_dormitorio()
	{
		return temp_dormitorio;
	}
	public ModoSistema getModoSistema ()
	{
		return modo;
	}

	public SistemaDomotico ()
	{
		rele = EstadoRele.CERRADO;
		modo = ModoSistema.OFF;
	}

	
	
	public void setTempExterna (Double temp_ext)
	{
		this.temp_ext = temp_ext;
	}


	public void setTemp_habitacion1 (Float temp)
	{
		this.temp_habitacion1= temp;
	}
	
	public void setHumedad_habitacion1 (Float humedad)
	{
		this.humedad_habitacion1= humedad;
	}
	
	public void setTemp_habitacion2 (Float temp)
	{
		this.temp_habitacion2= temp;
	}
	
	public void setHumedad_habitacion2 (Float humedad)
	{
		this.humedad_habitacion2= humedad;
	}
	public void setTemp (Float temp)
	{
		this.temp = temp;
	}

	public void setTempdormitorio (Float temp)
	{
		this.temp_dormitorio= temp;
	}

	public void setHumedad (Float humedad)
	{
		this.humedad = humedad;
	}

	public void setHumedaddormitorio (Float humedad)
	{
		this.humedad_dormitorio = humedad;
	}

	public void setTempclimatizador (Float temp)
	{
		this.temp_climatizador = temp;
	}


	public void set_opcionesModo (OpcionesModo opciones)
	{
		this.opciones_modo = opciones;
	}


	public OpcionesModo get_opcionesModo()
	{
		return opciones_modo;
	}

	public float calcularTemperaturaMedia()
	{
		float res = 0;		

		res = (temp+temp_dormitorio+temp_habitacion1+temp_habitacion2)/4;
		return res;		 									
	}
	
	public float calcularTemperaturaMinima()
	{
		float res = 0;		
		float[] datos = {temp,temp_dormitorio,temp_habitacion1,temp_habitacion2};
		
		res = datos[0];
		for (int i=0;i<datos.length;i++)
		{
			if (datos[i]<res)
				res = datos[i];
				
		}
		
		return res;		 									
	}
	
	public float calcularTemperaturaMaxima()
	{
		float res = 0;		
		float[] datos = {temp,temp_dormitorio,temp_habitacion1,temp_habitacion2};
		
		res = datos[0];
		for (int i=0;i<datos.length;i++)
		{
			if (datos[i]>res)
				res = datos[i];
				
		}
		
		return res;		 									
	}
	
	
	public float getTemperatura_raspi()
	{
		return temp_raspi;
	}

	public void setTemp_raspi (Float temp)
	{
		this.temp_raspi= temp;
	}

	
	
	public String toString ()
	{
		String res = "Salón: " + temp + "º " + humedad + "% HR -  " ;
		res += "Dormitorio: " + temp_dormitorio+ "º " + humedad_dormitorio + "% HR -  " ;
		res += "Habitacion1: " + temp_habitacion1+ "º " + humedad_habitacion1 + "% HR -  " ;
		res += "Habitacion2: " + temp_habitacion2+ "º " + humedad_habitacion2+ "% HR -  " ;
		res += "Externa: " + temp_ext + "º -  " ;
		res += "Raspberry: " + temp_raspi + "º -  " ;
		
		res += "Modo: " + modo + " (" + opciones_modo+ ") "+ temp_climatizador + "º -  " ;
		res += "Rele: " + rele;

		return res;

	}

	public String toString_info ()
	{
		String res = "Salón: " + temp + "º - " ;
		res += "Dormitorio: " + temp_dormitorio+ "º - " ;
		res += "Habitacion1: " + temp_habitacion1+ "º - " ;
//		res += "Habitacion2: " + temp_habitacion2+ "º -  " ;
		res += "Temp Externa: " + temp_ext + "º -  " ;
		res += "Raspberry: " + temp_raspi + "º -  " ;
		
		res += "Modo: " + modo + " (" + opciones_modo+ ") "+ temp_climatizador + "º -  " ;
		res += "Rele: " + rele;

		return res;

	}

}
