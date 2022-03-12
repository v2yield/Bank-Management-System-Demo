
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
public class PDFOutput  {
    public static final String RESULT="./AnnualReport.pdf";
    String para;
    public PDFOutput(String para){
        this.para=para;
    }
    public  void Result() throws Exception{
        Document document=new Document();
        PdfWriter.getInstance(document, new FileOutputStream(RESULT));
        document.open();
        document.add(new Paragraph(para));
        document.close();
    }
}
