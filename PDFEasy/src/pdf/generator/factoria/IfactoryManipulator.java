package pdf.generator.factoria;

import pdf.generator.manipuladores.ImanipuladoresPDF;

public interface IfactoryManipulator {

	public ImanipuladoresPDF getManipulatorPDF(int accion);

}
