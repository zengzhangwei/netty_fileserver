package other;
import java.io.File;
import java.io.FileInputStream;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.ContentHandler;

public class MimeTikaTest {

    //private static String localfilename = "C:\\Users\\scream\\Desktop\\只传路径接口设计.docx";
   // private static String localfilename = "D:\\大数据Hadoop基础实战班\\培训视频\\20170207 dashujujichuban 01.mp4";
    private static String localfilename = "C:\\Users\\scream\\Pictures\\Camera Roll\\zgr.jpg";
    public static void main(String args[]) throws Exception {

        FileInputStream is = null;
        try {
            File f = new File(localfilename);
            is = new FileInputStream(f);

            ContentHandler contenthandler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
            AutoDetectParser parser = new AutoDetectParser();
            // OOXMLParser parser = new OOXMLParser();
            ParseContext context = new ParseContext();
            context.set(Parser.class, parser);
            parser.parse(is, contenthandler, metadata);
            System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
            System.out.println("Title: " + metadata.get(Metadata.TITLE));
            System.out.println("Author: " + metadata.get(Metadata.AUTHOR));
            System.out.println("content: " + contenthandler.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (is != null) is.close();
        }
    }
}