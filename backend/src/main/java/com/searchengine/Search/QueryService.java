package com.searchengine.Search;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class QueryService {

@Autowired
private IndexerRepository indexerRepository;

@Autowired
private IndexerDocumentRepository indexerDocumentRepository;

public ArrayList<documnentOfWord> getWordDocuments(String w)
{
    System.out.println(w);

    // this line is commented to test the DB connection problem
    List<word> temp= indexerRepository.findByWordID(w);


    ArrayList<documnentOfWord>wordDocument= new ArrayList<>();

    for (word ww :temp)
    {
        for (documnentOfWord doc : ww.getDocuments()) {
            wordDocument.add( doc);
        }
    }
    return wordDocument;
}

    public  ArrayList<String> getdocelements(String docURl)
    {
        @SuppressWarnings("unchecked")
        List<docelement>temp=indexerDocumentRepository.findByUrl(docURl) ;
        ArrayList<String> elements = new ArrayList<>() ;
        for(docelement d : temp){
            elements.addAll(d.getElements());
        }
        return elements;
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
    public ArrayList<ArrayList<String>> searchly(String query){

        ArrayList<String>result = stemandremovestopwordsfunc(query);
     /*  for(String str:result){
            System.out.println(str);
      }
      */
        HashMap<String,Double> mp =new HashMap<String,Double>();
        ArrayList<String>ret=new ArrayList<String>();
        for(String str:result){
            ArrayList<documnentOfWord>temp  = getWordDocuments(str) ;

            for(documnentOfWord s:temp){

                if(mp.containsKey(s.getUrl())==true){
                    mp.put(s.getUrl(),mp.get(s.getUrl())+s.getFinal_score());
                }else {
                    mp.put(s.getUrl(), s.getFinal_score());
                    ret.add(s.getUrl());
                }
            }
        }
        /////////////////////////////this part to handle pharse searching////
        if(query.contains("\"")){
            // will handle if thier is multiple " in one query we will search for all of them
            String [] arr=query.split("\"");
            ArrayList<String>test=new ArrayList<String>();
            for (int i=0;i<arr.length;i++) {
                if (i % 2 == 1) {
                    test.add(arr[i]);
                }
            }
            ArrayList<ArrayList<String>>phraseResult=new ArrayList<ArrayList<String>>();
            for(String doc:ret ){
                ArrayList<String >toadd =new ArrayList<String>();
                toadd.add(doc);

                ArrayList<String>elementsArr=getdocelements(doc);
                ArrayList<Integer>eleFreq = new ArrayList<>() ;
                int cnt = 0 ;
                int i = 0;
                int mxfrq = 0 ;
                int mxind = 0 ;

                for (i=0;i<test.size();i++){
                    boolean found = false ;
                    int ind = 0 ;
                    int cntt = 0 ;
                    for (int k = 0 ; k<elementsArr.size();k++){
                        if(i==0) eleFreq.add(0);
                        if(elementsArr.get(k).contains(test.get(i))){
                            found=true;
                            eleFreq.set(ind,eleFreq.get(ind)+1);
                            elementsArr.set(k,elementsArr.get(k).replace(test.get(i),"<mark>"+test.get(i)+"</mark>" )) ;
                            cntt++;
                        }
                        ind++;
                    }
                    if(cntt>0)cnt++;
                    if(!found){
                        break;
                    }
                }
                if(cnt==test.size()){
                    int maxValue = Collections.max(eleFreq);
                    int maxIndex = eleFreq.indexOf(maxValue);
                    toadd.add(elementsArr.get(maxIndex));
                    phraseResult.add(toadd);
                }else{
                    mp.remove(doc);
                }
            }

            Collections.sort(phraseResult, new Comparator<ArrayList<String>>() {
                @Override
                public int compare(ArrayList<String> arr1,ArrayList<String> arr2) {
                    return Double.compare(mp.get(arr2.get(0)) , mp.get(arr1.get(0)));  // for descending order
                }
            });

            return phraseResult ;

        }


        ArrayList<ArrayList<String>>retu=new ArrayList<ArrayList<String>>();

        for(String url:ret ){
            ArrayList<String>toAdd=new ArrayList<>();
            toAdd.add(url);
            int freq = 0 ;
            String temp ="" ;
            ArrayList<String>elementsArr=getdocelements(url);
            for (String e :elementsArr) {
                String eAfterEdit= e ;
                String [] wordsOfEle=e.split(" ");
                int cnt = 0;
                for(String w : wordsOfEle){

                    ArrayList<String> strarr = stemandremovestopwordsfunc(w);
                    String str = "";
                    if (strarr.size() > 0) str = strarr.get(0);
                    for (String stemWord : result) {

                        boolean check = (str.length()==stemWord.length());
                        if(check){
                            int j = 0 ;
                            for (;check&&j<str.length();j++){
                                check=check&&(str.charAt(j)==stemWord.charAt(j));
                            }
                        }
                    if(check){
                        cnt++;
                        e=e.replace(w,"<p>"+w+"</p>");
                    }
                    }
                }
                if(cnt>freq){
                    freq=cnt;
                    temp=e;
                }
            }
            toAdd.add(temp);
            // here if the elem is  "" we may multiply the final score of the url by 0 so it appears last in the results
            retu.add(toAdd);
        }

        Collections.sort(retu, new Comparator<ArrayList<String>>() {
            @Override
            public int compare(ArrayList<String> arr1,ArrayList<String> arr2) {
                return Double.compare(mp.get(arr2.get(0)) , mp.get(arr1.get(0)));  // for descending order
            }
        });

        // Print the sorted ArrayList
        for (ArrayList<String> str : retu) {
            for (String s:str)
            System.out.println(s);
        }

        ////////////////////////////////////////////////////////////////////
        return retu ;
    }

}
