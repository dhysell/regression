package repository.gw.enums;

public enum DocumentFileType {
	BitmapImage("Bitmap Image"),
	CSV("CSV (comma separated values)"),
	GIFImage("GIF Image"),
	HTML("HTML"),
	JPEGImage("JPEG Image"),
	MicrosoftAudio("Microsoft Audio"),
	MicrosoftExcelWorksheet("Microsoft Excel Worksheet"),
	MicrosoftExcelWorksheetWindows98("Microsoft Excel Worksheet (Windows 98)"),
	MicrosoftMODI("Microsoft MODI"),
	MicrosoftVideo("Microsoft Video"),
	MicrosoftWordDocument("Microsoft Word Document"),
	MPEGVideo("MPEG Video"),
	OCTETStream("OCTET Stream"),
	OpenXMLPresentation("Open XML presentation"),	
	OpenXMLSpreadSheet("Open XML spread sheet"),
	OpenXMLWordDocument("Open XML word document"),
	PDF("PDF"),	
	PlainText("Plain Text"),
	PNGImage("PNG Image"),
	Postscript("Postscript"),
	PowerPoint("Power Point"),
	ProgressiveJPEGImage("Progressive JPEG Image"),
	QuickTimeVideo("QuickTime Video"),
	RichText("Rich Text"),
	RichTextFormat("Rich Text Format"),
	TIFFImage("TIFF Image"),
	WaveAudio("Wave Audio"),
	XML("XML"),
	;
	
	String value;
	
	private DocumentFileType(String type){
		value = type;
	}
	
	public String getValue(){
		return value;
	}
}
