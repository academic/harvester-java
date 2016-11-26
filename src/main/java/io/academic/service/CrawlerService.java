package io.academic.service;

import io.academic.entity.DBArticle;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.xml.DcXMLParser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Crawls xmls and sends to data management services
 */
@Service
public class CrawlerService {


    public static DBArticle parse (String href) throws IOException, TikaException, SAXException {


        //Document doc = Jsoup.connect(href).get();
       // Document doc = Jsoup.parse(href);

       // Element e = doc.select("metadata").first();
        // String a = e.attr("dc:identifier");
        // InputStream stream = new ByteArrayInputStream(e.getBytes(StandardCharsets.UTF_8));
        // InputStream stream = IOUtils.toInputStream(href);


        DcXMLParser parser = new DcXMLParser();
        InputStream stream =  new URL(href).openStream();
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext parseContext = new ParseContext();


        List<Metadata> metadatas = new ArrayList<>();

        parser.parse(stream,handler,metadata,parseContext);

        DBArticle article = new DBArticle();

      //  article.setId(metadata.get("identifier"));
        article.setTitle(metadata.get("title"));
        article.setBody(metadata.get("creator"));

        stream.close();

//        String a = metadata.get("creator");




        return article;

    }


}
