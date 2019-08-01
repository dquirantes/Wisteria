package com.dqg.utilidades;
import java.io.*; 

import org.apache.log4j.Logger;


public class ProgramaExterno {


	private static final Logger log = Logger.getLogger("Dameon");

	public String ejecutar (String programa) throws Exception
	{
		return ejecutar (programa, null);
	}
	
	public String ejecutar (String programa, String arg1) throws Exception
	{
		String res = null;
	

		// Se lanza el ejecutable
		Process p;

		if (arg1==null)
		{
			log.debug ("ejecutar " + programa);
			p=Runtime.getRuntime().exec (programa);
		}else
		{
			log.debug ("ejecutar " + programa + " " + arg1);
			p = Runtime.getRuntime().exec(new String[] { programa, arg1});
		}


		// Se obtiene el stream de salida del programa 
		InputStream is = p.getInputStream(); 

		/* Se prepara un bufferedReader para poder leer la salida más comodamente. */ 
		BufferedReader br = new BufferedReader (new InputStreamReader (is)); 

		// Se lee la primera linea 
		res = br.readLine(); 


		return res;
	}  
}



