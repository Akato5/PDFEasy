package pdf.generator;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

/* Clase PDFUtilidades
 * 
 * Cualquier utilidad genérica para todo el proyecto debería estar aquí para su reutilización
 */
public class PDFUtilidades {
	
	public static String RUTA_TEMPORAL = "C:"+File.separator+"PRUEBAS_PDF"+File.separator;
	public static String NOMBRE = "ficheros";
	public static String EXTENSION_ZIP = ".zip";	
	
	/*  zipear
	 * 
	 * Crea un fichero .zip con la lista de ficheros que se le pasa como parámetro
	 */
	public List<File> zipear(List<File> ficheros) throws IOException{
		List<File> zipeo = new ArrayList<File>();
		ZipOutputStream out = null;
		FileOutputStream fos = null;
		try {
	
			File zip = new File(RUTA_TEMPORAL+NOMBRE+EXTENSION_ZIP);
			fos = new FileOutputStream(zip);
			out = new ZipOutputStream(fos);
			ZipEntry e = null;
			InputStream in = null;
			byte[] data = null;
			for(File fichero:ficheros){
				e = new ZipEntry(fichero.getName());
				out.putNextEntry(e);					
				in = new FileInputStream(fichero);
				data = IOUtils.toByteArray(in);
				out.write(data, 0, data.length);
				out.closeEntry();
				in.close();
			}			
			out.finish();
			out.close();
			zipeo.add(zip);

		} catch (Exception e1) {
			e1.printStackTrace();
		}finally{
			if(out != null)
				out.close();
			if(fos != null)
				fos.close();
		
		}
		
		return zipeo;
	}
	
}	
	
	
