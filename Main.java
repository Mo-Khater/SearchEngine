import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
   public String parseHtmlDoc(Document doc,String type)
   {
       StringBuilder resultSB=new StringBuilder();
       Elements elements = doc.select(type);
       for (Element element : elements) {
           String text = element.text();
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
    public static ArrayList<String> afterStemmingandeliminatestopwords(Document doc,String type) {
        Main main = new Main(); // Create an instance of Main class
        return main.stemandremovestopwordsfunc(main.parseHtmlDoc(doc,type));
    }
    public static void main(String[] args) throws IOException {
        ArrayList<String> ResultOfStemming=new ArrayList<>();
        try {
            // Load HTML file
            File input = new File("example.html");
            Document doc = Jsoup.parse(input, "UTF-8");
            ArrayList<String> result = afterStemmingandeliminatestopwords(doc,"p");
            for (String element:result)
            {
                System.out.print(element+" ");
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
}

}
