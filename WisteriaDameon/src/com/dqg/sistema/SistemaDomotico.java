package com.dqg.sistema;

import com.dqg.tipos.*;


//enum EstadoRele {ABIERTO, CERRADO};
//enum OpcionesModo{SALON, DORMITORIO,HABITACION1, HABITACION2,  MEDIA, MINIMO, MAXIMO};
//enum ModoSistema{ON_FIJO, OFF, CLIMATIZADOR};


//enum ErroresSistema{BBDD, SENSORES,ACTUADOR, REGISTRO, TEMP_PLACA, SENSOR_PLACA, SHELLY};


public class SistemaDomotico {



	private float temp_salon;
	private Double temp_ext;	
	private float temp_climatizador;
	private float humedad_salon;	
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
	
	//private boolean enviarNotificaciones;
	
	private int arranques = 0;
	private long tiempoFuncionando = 0;
	private long tiempoultimoArranque = 0;
	
	
	public void inicializarTiempoFuncionando()
	{
		tiempoFuncionando=0;
		tiempoultimoArranque=System.currentTimeMillis();
	}
	
	public String getTiempoFuncionando()
	{		
		// Tiempo de funcionamiento	
		float horas_funcionamiento = this.tiempoFuncionando;
		
		
		// Incremente lo que lleve en el último ciclo
		if (this.getEstadoRele()==EstadoRele.ABIERTO)
			horas_funcionamiento += System.currentTimeMillis() -  tiempoultimoArranque;

		
		// Convierte a float horas
		horas_funcionamiento= (float)horas_funcionamiento/(float)3600000;
		
		
		return String.format("%.2f", horas_funcionamiento);		
		
	}
	
	public void setTiempoParada()
	{
		
		tiempoFuncionando = tiempoFuncionando + System.currentTimeMillis() -  tiempoultimoArranque;
	}
	
	public void setTiempoArranque()
	{
		tiempoultimoArranque = System.currentTimeMillis();		
	}
	
	/*public void incializarTiempoFuncionando()
	{
		tiempoFuncionando=0;
	}*/
	
	public int getArranques()
	{
		return arranques;
	}
	
	public void aumentarArranques()
	{
		arranques++;
	}
	
	public void inicializarArranques()
	{
		arranques=0;
	}
	
	

	
	/*public void setEnviarNotificaciones(boolean enviar)
	{
		this.enviarNotificaciones = enviar;		
	}
	public Boolean getEnviarNotificaciones()
	{
		return enviarNotificaciones;
	}*/
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
		return humedad_salon;
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
		return temp_salon;
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
		this.temp_salon = temp;
	}

	public void setTempdormitorio (Float temp)
	{
		this.temp_dormitorio= temp;
	}

	public void setHumedad (Float humedad)
	{
		this.humedad_salon = humedad;
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
		int sensores=0;
		
		if (temp_salon!=0)
		{
			sensores++;
			res += temp_salon;
		}
		
		if (temp_dormitorio!=0)
		{
			sensores++;
			res += temp_dormitorio;
		}

		if (temp_habitacion1!=0)
		{
			sensores++;
			res += temp_habitacion1;
		}
		
		if (temp_habitacion2!=0)
		{
			sensores++;
			res += temp_habitacion2;
		}
		

		return (res/sensores);		 									
	}
	
	public float calcularTemperaturaMinima()
	{
		float res = 0;		
		float[] datos = {temp_salon,temp_dormitorio,temp_habitacion1,temp_habitacion2};
		
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
		float[] datos = {temp_salon,temp_dormitorio,temp_habitacion1,temp_habitacion2};
		
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

	public float getHumedad_Habitacion1()
	{
		return humedad_habitacion1;
	}
	
	public float getHumedad_Habitacion2()
	{
		return humedad_habitacion2;
	}
	public void setTemp_raspi (Float temp)
	{
		this.temp_raspi= temp;
	}

	
	
	public String toString ()
	{
		String res = "Salón: " + temp_salon + "º " + humedad_salon + "% HR -  " ;
		res += "Dormitorio: " + temp_dormitorio+ "º " + humedad_dormitorio + "% HR -  " ;
		res += "Habitacion1: " + temp_habitacion1+ "º " + humedad_habitacion1 + "% HR -  " ;
		res += "Habitacion2: " + temp_habitacion2+ "º " + humedad_habitacion2+ "% HR -  " ;
		res += "Externa: " + temp_ext + "º -  " ;
		res += "Raspberry: " + temp_raspi + "º -  " ;
	
		if (modo.equals(ModoSistema.CLIMATIZADOR))
			res += "Modo: " + modo + " (" + opciones_modo+ ") "+ temp_climatizador + "º -  " ;			
		else
			res += "Modo: " + modo + " - " ;
				
		res += "Caldera: " + rele + " - ";
		res += "Media: " + calcularTemperaturaMedia() + " - ";		
		res += "Uso: " + this.getTiempoFuncionando();
		
		return res;

	}

	public String toString_info ()
	{
		String res = "Salón: " + temp_salon + "º - " ;		
		res += "Dormitorio: " + temp_dormitorio+ "º - " ;
		
		if (temp_habitacion1!=0)
			res += "Habitacion1: " + temp_habitacion1+ "º - " ;

		if (temp_habitacion2!=0)
			res += "Habitacion2: " + temp_habitacion2+ "º -  " ;
		
		
		res += "Temp Externa: " + temp_ext + "º -  " ;
		res += "Raspberry: " + temp_raspi + "º -  " ;
		
		if (modo.equals(ModoSistema.CLIMATIZADOR))
			res += "Modo: " + modo + " (" + opciones_modo+ ") "+ temp_climatizador + "º -  " ;			
		else
			res += "Modo: " + modo + " - " ;
		
		res += "Caldera: " + rele + " - ";

		res += "Media: " + calcularTemperaturaMedia() + " - ";		
		res += "Uso: " + this.getTiempoFuncionando();

		return res;

	}

}
