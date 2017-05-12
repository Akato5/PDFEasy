package pdf.generator.factoria;

import pdf.generator.manipuladores.ImanipuladoresPDF;
import pdf.generator.manipuladores.PDFMerge;
import pdf.generator.manipuladores.PDFSplit;
import pdf.generator.manipuladores.PDFToText;

public class factoryManipulator implements IfactoryManipulator {

	public factoryManipulator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ImanipuladoresPDF getManipulatorPDF(int accion) {
	    switch(accion){
	    case 0:
	    	return new PDFToText();    	
	    case 1:
	    	return new PDFMerge();
	    case 2:
	    	return new PDFSplit();
	    default:
	    	return null;
	    }
	}

}
