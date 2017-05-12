package pdf.generator.manipuladores;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

public class PDFMerge extends AbstractManipuladorPDF {

	public PDFMerge() {
		// TODO Auto-generated constructor stub
	}
	
	public List<File> procesarPDFs(List<File> storeFile) throws Exception {		
		List<File> ficherosProcesados = new ArrayList<File>();	
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		//Setting the destination file
		PDFmerger.setDestinationFileName(RUTA_TEMPORAL+"mergePDF"+EXTENSION_PDF);				
		for (File fichero : storeFile) {			      
	      //adding the source files
	      PDFmerger.addSource(fichero);								
		}
		
	     //Merging the two documents
	     PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
	     
	     ficherosProcesados.add(new File(RUTA_TEMPORAL+"mergePDF"+EXTENSION_PDF));
		return ficherosProcesados;
	}


}
