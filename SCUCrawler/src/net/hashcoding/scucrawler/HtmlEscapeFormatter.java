package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.utils.StringEscapeUtil;
import us.codecraft.webmagic.model.formatter.ObjectFormatter;

public class HtmlEscapeFormatter implements ObjectFormatter<String> {

    @Override
    public String format(String raw) throws Exception {
    	if (raw == null) 
    		return null;
    	raw = raw.trim();
    	return StringEscapeUtil.unescapeHTML(raw, 0);
    }

    @Override
    public Class<String> clazz() {
        return String.class;
    }

    @Override
    public void initParam(String[] extra) {
    }
   
}
