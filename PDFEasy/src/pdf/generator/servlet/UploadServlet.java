package pdf.generator.servlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import pdf.generator.frontcontroller.FrontControllerPDF;
import pdf.generator.manipuladores.PDFToText;

@WebServlet("/upload")
public class UploadServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6947774154075386893L;

    // location to store file uploaded
    private static final String UPLOAD_DIRECTORY = "upload";
    
    private static final String ACCION = "accion";
    
    private static final String ENCODING_DEFECTO = "UTF-8";
 
    // upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
 
    
    public void init(){
    	
    	System.out.println("***************************************************** Arrancando UploadServlet");
    }
    
    
    /**
     * Upon receiving file upload submission, parses the request to read
     * upload data and saves the file on disk.
     */
    protected void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	File responseFile = null;
    	List<FileItem> formItems = null;
    	List<File> ficheros = new ArrayList<File>();
    	String fieldname = "";
    	String accion = "";
    	boolean isOK = true;
    	try{
    		// Recuperamos los campos del formulario y los o el fichero enviado
    		formItems = getRequestParser(request, response);
    		
            if (formItems != null && formItems.size() > 0) {
                // iterates over form's fields
                for (FileItem item : formItems) {
                    // processes only fields that are not form fields
                    if (!item.isFormField()) {
                    	if(item.getContentType().indexOf("application/pdf") == -1){
                    		isOK = false;
                    		break;
                    	}
                    	//leemos el fichero y lo almacenamos para su tratamiento
                    	ficheros.add(getFileFromRequest(item));
                    }else{
                    	fieldname = item.getFieldName();
                    	if(fieldname != null && fieldname.equalsIgnoreCase(ACCION) )
                    		accion = item.getString();	                    		                    	
                    }
                }
                if(isOK && accion != null && !"".equals(accion)){
	    	        FrontControllerPDF frontcontroller = new FrontControllerPDF();
	    	        responseFile = frontcontroller.procesarPeticion(accion, ficheros);
	    	        if(responseFile != null)
	    	        	responder(request, response,responseFile);
	    	        else
	    	        	isOK = false;
                }                	                
            }
            if(!isOK)
            	responderError(request,response);

    	}catch(Throwable th){
    		th.printStackTrace();
    		responderError(request,response);
    	}                
    }
    
    public void responder(HttpServletRequest request,HttpServletResponse response, File responseFile) throws Exception{
    	FileInputStream in = null;
    	OutputStream out = null;
    	try{
	        // obtains ServletContext
	        ServletContext context = getServletContext();
	        
	        // gets MIME type of the file
	        String mimeType = context.getMimeType(responseFile.getAbsolutePath());
	        if (mimeType == null) {
	        	// set to binary type if MIME mapping not found
				mimeType = "application/octet-stream";
	        }
	        
	        System.out.println("MIME type: " + mimeType);
	        
	        // modifies response
			response.setContentType(mimeType);
			response.setContentLength((int) responseFile.length());
			
			// forces download
			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename="+responseFile.getName();
			response.setHeader(headerKey, headerValue);        
			//response.setHeader("Content-Encoding","UTF-8;");
			//response.setHeader("charset","UTF-8;");        
	        
	        out = response.getOutputStream();
	        in = new FileInputStream(responseFile);
	        byte[] buffer = new byte[4096];
	        int length;
	        while ((length = in.read(buffer)) > 0){
	            out.write(buffer, 0, length);
	        }

    	}catch(Exception ex){
    		throw ex;
    	}finally{
    		responseFile = null;
	        if (in != null)
	        	in.close();
	        if(out != null){
	        	out.flush();
	        	out.close();
	        }
    	}
    	
    }
    
    public void responderError(HttpServletRequest request,HttpServletResponse response){
        PrintWriter writer = null;
		try {
			writer = response.getWriter();
	        writer.println("Error: Se ha producido un error al procesar los PDFs");
	        writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(writer != null)
				writer.close();			
		}        
        
    }
    
    public List<FileItem> getRequestParser(HttpServletRequest request,HttpServletResponse response) throws Exception{
    	
    	try{
	    	// checks if the request actually contains upload file
	        if (!ServletFileUpload.isMultipartContent(request)) {
	            responderError(request,response);
	            return null;
	        }
	       
	        String encoding = request.getCharacterEncoding();
	        System.out.println("encoding: " + encoding);
	        // Si no llega encoding en la petición se establece por defecto UTF-8
	        if(encoding == null || "".equals(encoding)){
	        	encoding = ENCODING_DEFECTO;
	        }
	        // establecemos encoding
	        response.setCharacterEncoding(encoding);
	        
	        // configures upload settings
	        DiskFileItemFactory factory = new DiskFileItemFactory();
	        // sets memory threshold - beyond which files are stored in disk
	        factory.setSizeThreshold(MEMORY_THRESHOLD);
	        // sets temporary location to store files
	        factory.setRepository(new File(System.getProperty("java.io.tmpdir")));
	 
	        
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        
	        upload.setHeaderEncoding(encoding);
	         
	        // sets maximum size of upload file
	        upload.setFileSizeMax(MAX_FILE_SIZE);
	         
	        // sets maximum size of request (include file + form data)
	        upload.setSizeMax(MAX_REQUEST_SIZE);        
	        
	        // parses the request's content to extract file data
	        @SuppressWarnings("unchecked")
	        List<FileItem> formItems = upload.parseRequest(request);
	        
	        return formItems;
    	}catch(Exception ex){
    		throw ex;
    	}
    	
    }
    
    public File getFileFromRequest(FileItem item){
        File storeFile = null;
    	try{

	        // constructs the directory path to store upload file
	        // this path is relative to application's directory
	        String uploadPath = getServletContext().getRealPath("")
	                + File.separator + UPLOAD_DIRECTORY;
	         
	        // creates the directory if it does not exist
	        File uploadDir = new File(uploadPath);
	        if (!uploadDir.exists()) {
	            uploadDir.mkdir();
	        }

	        String fileName = item.getFieldName();
            String filePath = uploadPath + File.separator + fileName;
            storeFile = new File(filePath);
            item.write(storeFile);	        

	        
	        String fieldName = item.getFieldName();	        
	        String contentType = item.getContentType();
	        boolean isInMemory = item.isInMemory();
	        long sizeInBytes = item.getSize();
	        
	        System.out.println("Estos son todos los datos : Nombre: " + fileName + "| nombreCampo :" + fieldName + "| ContentType :"+ contentType);
	        System.out.println("Est en memoria :" + isInMemory + "| size :" + sizeInBytes);
	     
	        
	        
        }catch(Exception ex){
        	System.out.println("Se ha producido una excepción :" + ex.getMessage());
        	ex.printStackTrace();
        }
        return storeFile;
    	
    }
	
	
}