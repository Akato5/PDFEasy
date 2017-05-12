package pdf.generator.manipuladores;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;




public class PDFToText extends AbstractManipuladorPDF{

	private static String EXTENSION_FICHERO = ".txt";

	
		public File procesarPDF(File file) throws Exception {
	        PDFParser parser;
	        String parsedText = null;
	        PDFTextStripper pdfStripper = null;
	        PDDocument pdDoc = null;
	        COSDocument cosDoc = null;
	        RandomAccessBufferedFileInputStream randomFileinput = null;
	        BufferedWriter bw = null;
	        File texto = null;
	        if (!file.isFile()) {
	            System.err.println("File does not exist.");
	            return null;
	        }
	        try {
	        	randomFileinput = new RandomAccessBufferedFileInputStream(file);
	            parser = new PDFParser(randomFileinput);

	        } catch (IOException e) {
	            System.err.println("Unable to open PDF Parser. " + e.getMessage());
	            return null;
	        }
	        	
	        
	        try {
	            parser.parse();
	            cosDoc = parser.getDocument();
	            

	            pdfStripper = new PDFTextStripper();
	            pdDoc = new PDDocument(cosDoc);
	            
	            parsedText = pdfStripper.getText(pdDoc);
	            
	            texto = new File(RUTA_TEMPORAL+file.getName()+EXTENSION_FICHERO);
	            
	            bw = new BufferedWriter(new FileWriter(texto));
	            bw.write(parsedText);
	            
	            
	        } catch (Exception e) {
	            System.err
	                    .println("An exception occured in parsing the PDF Document."
	                            + e.getMessage());
	        } finally {
	            try {
	            	if(randomFileinput != null)
	            		randomFileinput.close();
	                if (cosDoc != null)
	                    cosDoc.close();
	                if (pdDoc != null)
	                    pdDoc.close();
	                if(bw != null)
	                	bw.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        return texto;
	    }
	   
	   	
	
}
