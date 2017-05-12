package pdf.generator.manipuladores;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.multipdf.Splitter;
import org.apache.pdfbox.pdmodel.PDDocument;

/*
 * Clase PDFSplit
 * Obtiene en un nuevo pdf cada página del fichero pdf enviado, 
 * dividiendo por tanto el pdf en tantas hojas tenga
 */
public class PDFSplit extends AbstractManipuladorPDF {

	public PDFSplit() {
	
	}
	
	public List<File> procesarPDFs(List<File> storeFile) throws Exception {	

		List<File> ficherosProcesados = new ArrayList<File>();			
		List<File> paginasprocesadas = new ArrayList<File>();
		for(File fichero:storeFile){
			paginasprocesadas = dividirPDF(fichero);
			for(File pagina:paginasprocesadas){
				ficherosProcesados.add(pagina);
			}
		}
		return ficherosProcesados;
	}
	
	
	public List<File> dividirPDF(File storeFile) throws Exception {	
		List<File> paginas = new ArrayList<File>();
		PDDocument document = null;
		try{
			//Cargamos el documento. Cogemos solo el primero
			document = PDDocument.load(storeFile);
			Splitter splitter = new Splitter();		
			// indicamos por cuantas páginas queremos separar
			//splitter.setSplitAtPage(2);
			List<PDDocument> splittedDocuments = splitter.split(document);	      
		     int i = 0;
		     File pagina = null;
			for(PDDocument documentos:splittedDocuments){
				i++;
				pagina = new File(RUTA_TEMPORAL+storeFile.getName()+"_"+i+EXTENSION_PDF);
				documentos.save(pagina);
				paginas.add(pagina);
				documentos.close();
			}
			return paginas;
		}finally{
	      if(document!=null)
	    	  document.close();	      
		}
	}

}
