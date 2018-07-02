package external_file;


import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URISyntaxException;

/*************************************
 *                                   *
 *   Created by: Asier               *
 *                                   *
 *   Funcionality:                   *
 *                                   *
 *  - Sturctures the XSLT text       *
 *                                   *
 *************************************/


public class XSLT {

    public static String transform(String input, File xsltFile) throws IOException, URISyntaxException, TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        Source xslt = new StreamSource(xsltFile);
        Transformer transformer = factory.newTransformer(xslt);

        Source text = new StreamSource(new StringReader(input));
        StringWriter writer = new StringWriter();
        transformer.transform(text, new StreamResult(writer));
        return writer.toString();
    }
}
