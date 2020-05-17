package net.okhotnikov.everything.util;

import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by Sergey Okhotnikov.
 */
public class DataUtil {

    public static final String RECORDS = "records";
    public static final String TYPE = "type";
    public static final String QUOTATION = "quotation";
    public static final String POEM = "poem";
    public static final String CHAPTER = "chapter";

    public static Map<String,Object> retainQuotations(Map<String,Object> full){
        List<Map<String,Object>> records =(List<Map<String,Object>>) full.get(RECORDS);
        ListIterator<Map<String,Object>> it= records.listIterator();
        while (it.hasNext()){
            Map<String,Object> record = it.next();
            String type = (String)record.get(TYPE);
            if(QUOTATION.equals(type) || POEM.equals(type) || CHAPTER.equals(type))
                continue;

            it.remove();
        }

        return full;
    }
}
