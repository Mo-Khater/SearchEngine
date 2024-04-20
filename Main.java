import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;


public class Main {
   public String parseHtmlDoc(Document doc,String type,ArrayList<String >docelments)
   {
       StringBuilder resultSB=new StringBuilder();
       Elements elements = doc.select(type);
       for (Element element : elements) {
           String text = element.text();
            docelments.add(text);
           resultSB.append(text);
           resultSB.append(" ");
       }
       String result=resultSB.toString();
       return result;
   }
   public ArrayList<String> stemandremovestopwordsfunc(String text)
   {

       ArrayList<String> result=new ArrayList<>();
       try {
           // Create an EnglishAnalyzer
           Analyzer analyzer = new EnglishAnalyzer();

           // Tokenize,remove stopwords and stem the text
           TokenStream stream = analyzer.tokenStream(null, new StringReader(text)); // tokenStream(fieldname,Stream of words)
           CharTermAttribute termAtt = stream.addAttribute(CharTermAttribute.class);
           stream.reset();
           while (stream.incrementToken()) {
               result.add(termAtt.toString());
           }
           stream.close();
           analyzer.close();
       } catch (IOException e) {
           e.printStackTrace();
       }

    return result;
   }
    public static void indexingandputindatabase(Document doc,String type,String tag,String Html,ArrayList<String> Docelements) {
        Main main = new Main(); // Create an instance of Main class
        ArrayList<String> result= main.stemandremovestopwordsfunc(main.parseHtmlDoc(doc,type,Docelements));
        for (String element:result)
        {
            mongo.connectmongo(element,Html,tag);
        }

    }
    public static void main(String[] args) throws IOException {

        ArrayList<String> ResultOfStemming=new ArrayList<>();
        ArrayList<String> docelements=new ArrayList<>();
        try {
            // Load HTML file
            File input = new File("example.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            indexingandputindatabase(doc,"p","normal","doc2",docelements);
            indexingandputindatabase(doc,"h1","Heading","doc2",docelements);
            indexingandputindatabase(doc,"title","title","doc2",docelements);
            indexingandputindatabase(doc, "li", "normal","doc2",docelements);
            mongo.putdocelments("doc2",docelements);
//            ArrayList<String>test=mongo.getdocelements("doc2");
//            for (String e:test)
//            {
//                System.out.print(e+"\n");
//            }

            Main main=new Main();
            ArrayList<String>result=main.stemandremovestopwordsfunc("apple");
            ArrayList<String>test=mongo.getworddocs(result.get(0));
            for (String e:test)
            {
                System.out.print(e+"\n");
            }
//            System.out.print(mongo.df_count(result.get(0)));
//            System.out.print(mongo.metadata(result.get(0),"doc1"));
//            System.out.print(mongo.tf_count(result.get(0),"doc1"));


        }catch (Exception e)
        {
            e.printStackTrace();
        }
}

}