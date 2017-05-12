package pdf.generator.frontcontroller;

import java.io.File;
import java.util.List;

import pdf.generator.PDFUtilidades;
import pdf.generator.factoria.IfactoryManipulator;
import pdf.generator.factoria.factoryManipulator;
import pdf.generator.manipuladores.ImanipuladoresPDF;
/*
 * Clase FronControllerPDF 
 * Se encarga de instanciar la factoria y ejecutar la función procesarPDFs del objeto que le devuelve la factoria
 * Si el método procesarPDFs le devuelve más de un fichero, llama a la clase de utilidades para generar un .zip
 */
public class FrontControllerPDF {
			
	public  FrontControllerPDF() {
		
	}

	public File procesarPeticion(String accion, List<File> storeFiles){
		List<File> responseFiles = null;
		try{
		//Obtenemos una factoria de manipuladores
		IfactoryManipulator factoria = new factoryManipulator();
		int action = Integer.parseInt(accion);	
		//obtenemos la clase que tratara el PDF según la acción elegida por el usuario
		ImanipuladoresPDF manipulador = factoria.getManipulatorPDF(action);
		
		//Recuperamos el fichero que vamos a devolver
		responseFiles = manipulador.procesarPDFs(storeFiles);
		if(responseFiles.size()>1){
			responseFiles = new PDFUtilidades().zipear(responseFiles);
			
		}
		}catch(Exception ex){
			ex.printStackTrace();
			System.out.println("Se ha producido una excepción al tratar el PDF");
			return null;
		}
		//modificar para incluir el zipeo si vienen más archivos
	    return responseFiles.get(0);		
		
	}
	

	
}
