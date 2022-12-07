package com.viamedsalud.gvp.util;

import com.viamedsalud.gvp.modelo.Episodio;
import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class EpisodioExporterPDF {

	private Episodio episodio;

	public EpisodioExporterPDF(Episodio episodio) {
		super();
		this.episodio = episodio;
	}

	public void exportar(HttpServletResponse response) throws DocumentException, IOException {
		Document documento = new Document(PageSize.A4.rotate(), 28f, 0f, 10f, 0f);
		PdfWriter pdfw =PdfWriter.getInstance(documento, response.getOutputStream());

		documento.open();

		String pattern = "dd-MM-yyyy";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		String fechaNacimientoFormat = simpleDateFormat.format(episodio.getFechaNac());

		PdfContentByte cb = pdfw.getDirectContent();

		Barcode39 code39 = new Barcode39();
		code39.setBaseline(-1);
		code39.setFont(null);
		code39.setCode(episodio.getEpisodio());
		code39.setCodeType(Barcode39.UPCA);
		Image codigo = code39.createImageWithBarcode(cb, null, null);
		codigo.scaleAbsolute(150,50);


		PdfPTable tabla = new PdfPTable(2);

		tabla.setWidthPercentage(100);
		tabla.setWidths(new float[] { 0.28f, 1f });

		PdfPCell celdaApellido = new PdfPCell(new Phrase("Apellido: "+ episodio.getApellPaciente()));
		PdfPCell celdaNombre = new PdfPCell(new Phrase("Nombre: "+ episodio.getNomPaciente()));
		PdfPCell celdaFNacimiento = new PdfPCell(new Phrase("F.Nac: " + fechaNacimientoFormat));
		PdfPCell celdaCodigo = new PdfPCell(codigo);

		celdaApellido.setBorder(Rectangle.NO_BORDER);
		celdaNombre.setBorder(Rectangle.NO_BORDER);
		celdaFNacimiento.setBorder(Rectangle.NO_BORDER);
		celdaCodigo.setBorder(Rectangle.NO_BORDER);
		celdaCodigo.setRowspan(3);

		//Agregamos las celdas
		tabla.addCell(celdaApellido);
		tabla.addCell(celdaCodigo);
		tabla.addCell(celdaNombre);
		tabla.addCell(celdaFNacimiento);


		// Agregamos la tabla al documento
		documento.add(tabla);

		documento.close();
	}
}
