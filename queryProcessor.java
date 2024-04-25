import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class queryProcessor {
   String query ;

    public queryProcessor(String q) {
        query=q;
    }

    public ArrayList<String> searchly(){

        indexer indexer=new indexer();
        ArrayList<String>result=indexer.stemandremovestopwordsfunc(query);
     /*  for(String str:result){
            System.out.println(str);
      }
      */
        HashMap<String,Boolean>mp =new HashMap<String,Boolean>();
        ArrayList<String>ret=new ArrayList<String>();
        for(String str:result){
            ArrayList<String>temp  = mongo.getworddocs(str) ;
            for(String s:temp){
                if(mp.get(s)) continue;
                mp.put(s,true);
                ret.add(s);
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
            ArrayList<String>phraseResult=new ArrayList<String>();
            for(String doc:ret ){
                ArrayList<String>elementsArr=mongo.getdocelements(doc);
                int cnt = 0 ;
                int i = 0;
                for (i=0;i<test.size();i++){
                    boolean found = false ;
                    for (String e :elementsArr){
                        if(e.contains(test.get(i))){
                            found=true;
                            cnt++;
                            break;
                        }
                    }
                    if(!found){
                        break;
                    }
                }
                if(cnt==test.size()){
                    phraseResult.add(doc);
                }
            }

            return phraseResult;

        }
        ////////////////////////////////////////////////////////////////////
        return ret ;
    }

    /*
    * are u handled removing stop words and punctuations ?
    * if yes explain the func ?
    *
    * explain fast how the docs are stored
    *
    * u explain u startegy for him 
    * */

}
