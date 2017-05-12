package pdf.generator.manipuladores;

import java.io.File;
import java.util.List;

public interface  ImanipuladoresPDF {
	
	public List<File> procesarPDFs(List<File> storeFile) throws Exception;
	public File procesarPDF(File file) throws Exception ; 

}
