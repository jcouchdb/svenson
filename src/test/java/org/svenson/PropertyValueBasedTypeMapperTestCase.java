package org.svenson;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.junit.Test;


public class PropertyValueBasedTypeMapperTestCase
{
    protected static Logger log = LoggerFactory.getLogger(PropertyValueBasedTypeMapperTestCase.class);

    @Test
    public void thatItWorks() throws IOException
    {
        String json = "{\n" +
        		"    \"total_rows\": 3,\n" +
        		"    \"offset\": 0,\n" +
        		"    \"rows\": [{ \"type\":\"foo\", \"value\":\"aaa\" },{ \"type\":\"bar\", \"value\":\"bbb\" },{ \"value\":\"ccc\",\"type\":\"bar\"  }]\n" +
        		"}\n" +
        		"";

        log.info(json);

        JSONParser parser = new JSONParser();
        PropertyValueBasedTypeMapper mapper = new PropertyValueBasedTypeMapper();
        mapper.setParsePathInfo(".rows[]");
        mapper.addFieldValueMapping("foo", Foo.class);
        mapper.addFieldValueMapping("bar", Bar.class);
        parser.setTypeMapper(mapper);

        Map m = parser.parse(HashMap.class, json);

        List rows = (List)m.get("rows");
        assertThat(rows.size(), is(3));

        Foo foo = (Foo)rows.get(0);

        assertThat(foo.getValue(), is("aaa"));

        Bar bar= (Bar)rows.get(1);
        assertThat(bar.getValue(), is("bbb"));
        bar = (Bar)rows.get(2);
        assertThat(bar.getValue(), is("ccc"));
    }

    @Test
    public void thatItWorksOnRootLevel() throws IOException
    {
        String json = "{'type':'foo','value':'aaa'}".replace('\'', '"');

        log.info(json);

        JSONParser parser = new JSONParser();
        PropertyValueBasedTypeMapper mapper = new PropertyValueBasedTypeMapper();
        mapper.setParsePathInfo("");
        mapper.addFieldValueMapping("foo", Foo.class);
        parser.setTypeMapper(mapper);

        // this only really makes sense to do with a common base class and a type decision 
        // based on some property. In this case this cast would be to the common base class.
        Foo foo = (Foo)parser.parse(json);
        
        assertThat(foo,is(notNullValue()));
        assertThat(foo.getValue(), is(("aaa")));
    }
    
    public static class Foo extends AbstractDynamicProperties
    {
        /**
         * 
         */
        private static final long serialVersionUID = 1L;
        private String type, value;

        public String getType()
        {
            return type;
        }

        public void setType(String type)
        {
            this.type = type;
        }

        public String getValue()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value = value;
        }

        @Override
        public String toString()
        {
            return super.toString()+": value = "+value;
        }
    }

    public static class Bar extends Foo
    {

        /**
         * 
         */
        private static final long serialVersionUID = 1L;

    }
}
