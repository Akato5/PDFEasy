package pdf.generator.manipuladores;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractManipuladorPDF implements ImanipuladoresPDF {

	public static String RUTA_TEMPORAL = "C:"+File.separator+"PRUEBAS_PDF"+File.separator;
	public static String EXTENSION_PDF = ".pdf";
	
	public AbstractManipuladorPDF() {
		// TODO Auto-generated constructor stub
	}

	public List<File> procesarPDFs(List<File> storeFile) throws Exception {
		List<File> ficherosProcesados = new ArrayList<File>();				
			for (File fichero : storeFile) {	 
				ficherosProcesados.add(procesarPDF(fichero));				
			}		
		return ficherosProcesados;
	}
	
	public File procesarPDF(File file) throws Exception {
		return null;
	}

}
