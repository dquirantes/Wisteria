package com.dqg.sistema;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



public class GenerarXML extends TimerTask {

	private static final Logger log = Logger.getLogger("Dameon");

	private SistemaDomotico sistema;
	private String fichero;

	public GenerarXML (SistemaDomotico sis, String fich)
	{
		this.sistema = sis;
		this.fichero = fich;
	}


	public void run() 
	{
		try
		{
			log.debug("Generar XML");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			DOMImplementation implementation = builder.getDOMImplementation();
			Document document = implementation.createDocument(null, "Wisteria", null);
			document.setXmlVersion("1.0");

			//Main Node
			Element raiz = document.getDocumentElement();


			Element itemNode0 = document.createElement("Habitaciones"); 
			raiz.appendChild(itemNode0);


			Element itemNode1 = document.createElement("Salon"); 
			itemNode0.appendChild(itemNode1);

			Element itemNode1T = document.createElement("Temperatura"); 
			itemNode1.appendChild(itemNode1T);
			itemNode1T.appendChild(document.createTextNode(sistema.getTemperatura()+""));


			Element itemNode1H = document.createElement("Humedad"); 
			itemNode1.appendChild(itemNode1H);
			itemNode1H.appendChild(document.createTextNode(sistema.getHumedad()+""));



			Element itemNode2 = document.createElement("Dormitorio"); 
			itemNode0.appendChild(itemNode2);

			Element itemNode2T = document.createElement("Temperatura"); 
			itemNode2.appendChild(itemNode2T);
			itemNode2T.appendChild(document.createTextNode(sistema.getTemperatura_dormitorio()+""));


			Element itemNode2H = document.createElement("Humedad"); 
			itemNode2.appendChild(itemNode2H);
			itemNode2H.appendChild(document.createTextNode(sistema.getHumedad_dormitorio()+""));



			Element itemNode3 = document.createElement("Habitacion1"); 
			itemNode0.appendChild(itemNode3);

			Element itemNode3T = document.createElement("Temperatura"); 
			itemNode3.appendChild(itemNode3T);
			itemNode3T.appendChild(document.createTextNode(sistema.getTemperatura_habitacion1()+""));


			Element itemNode3H = document.createElement("Humedad"); 
			itemNode3.appendChild(itemNode3H);
			itemNode3H.appendChild(document.createTextNode(sistema.getHumedad_Habitacion1()+""));


			Element itemNode4 = document.createElement("Habitacion2"); 
			itemNode0.appendChild(itemNode4);

			Element itemNode4T = document.createElement("Temperatura"); 
			itemNode4.appendChild(itemNode4T);
			itemNode4T.appendChild(document.createTextNode(sistema.getTemperatura_habitacion2()+""));


			Element itemNode4H = document.createElement("Humedad"); 
			itemNode4.appendChild(itemNode4H);
			itemNode4H.appendChild(document.createTextNode(sistema.getHumedad_Habitacion2()+""));




			Element itemNode5 = document.createElement("Rele"); 
			raiz.appendChild(itemNode5);
			itemNode5.appendChild(document.createTextNode(sistema.getEstadoRele().toString()));
			
			Element itemNode6 = document.createElement("Modo"); 
			raiz.appendChild(itemNode6);
			itemNode6.appendChild(document.createTextNode(sistema.getModoSistema().toString()));

			Element itemNode7 = document.createElement("OpcionesModo"); 
			raiz.appendChild(itemNode7);
			itemNode7.appendChild(document.createTextNode(sistema.get_opcionesModo().toString()));


			Element itemNode8 = document.createElement("TemperaturaClima"); 
			raiz.appendChild(itemNode8);
			itemNode8.appendChild(document.createTextNode(sistema.getTemperatura_Climatizador()+""));

			Element itemNode9 = document.createElement("Externa"); 
			raiz.appendChild(itemNode9);
			itemNode9.appendChild(document.createTextNode(sistema.getTempExterna()+""));

			Element itemNode10 = document.createElement("Raspberry"); 
			raiz.appendChild(itemNode10);
			itemNode10.appendChild(document.createTextNode(sistema.getTemperatura_raspi()+""));

			
			DecimalFormat df = new DecimalFormat("#.##");
			String temp_media=df.format(sistema.calcularTemperaturaMedia());			
			
			Element itemNode11 = document.createElement("TemperaturaMedia"); 
			raiz.appendChild(itemNode11);
			itemNode11.appendChild(document.createTextNode(temp_media.replace(",", ".")));			
 
			Element itemNode12 = document.createElement("TiempoFuncionamiento"); 
			raiz.appendChild(itemNode12);
			itemNode12.appendChild(document.createTextNode(sistema.getTiempoFuncionando().replace(",", ".")));			
 			
			


			Date date= new java.util.Date();

			Element itemNode14 = document.createElement("Timestamp"); 
			raiz.appendChild(itemNode14);
			//System.out.println ("********************************** " + new Timestamp(date.getTime()));
			itemNode14 .appendChild(document.createTextNode(new Timestamp(date.getTime())+""));




			
			//Generate XML
			Source source = new DOMSource(document);
			//Indicamos donde lo queremos almacenar
			Result result = new StreamResult(new java.io.File(fichero));
			Transformer transformer  = TransformerFactory.newInstance().newTransformer();
			transformer.transform(source, result);
			
			log.debug("Fichero XML " + fichero + " generado");
			
		}catch (Exception e)
		{
			log.error("Error generación XML de información");
			e.printStackTrace();
		}
	}
}