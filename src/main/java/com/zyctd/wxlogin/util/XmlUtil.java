package com.zyctd.wxlogin.util;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 封装/解析xml消息的工具类
 */
public class XmlUtil {

    /**
     * 扩展xstream，使其支持CDATA块
     * @return
     */
    public XStream getXstreamInclueUnderline(){
        XStream stream = new XStream(new XppDriver(new NoNameCoder()) {

            @Override
            public PrettyPrintWriter createWriter(Writer out) {
                return new PrettyPrintWriter(out) {
                    // 对所有xml节点的转换都增加CDATA标记
                    boolean cdata = true;

                    @Override
                    @SuppressWarnings("rawtypes")
                    public void startNode(String name, Class clazz) {
                        super.startNode(name, clazz);
                    }

                    @Override
                    public String encodeNode(String name) {
                        return name;
                    }

                    @Override
                    protected void writeText(QuickWriter writer, String text) {
                        if (cdata) {
                            writer.write("<![CDATA[");
                            writer.write(text);
                            writer.write("]]>");
                        } else {
                            writer.write(text);
                        }
                    }
                };
            }
        });

        return stream;
    }

    /**
     * 根据字符串 解析XML为map集合
     * @param xml
     * @return
     * @throws DocumentException
     */
    public Map<String, String> parseXML(String xml) throws DocumentException{
        Document document = DocumentHelper.parseText(xml);
        Element element =document.getRootElement();
        List<Element> childElements = element.elements();
        Map<String,String> map = new HashMap<String, String>();

        map = getAllElements(childElements,map);

        map.forEach((k,v)->{
            System.out.println(k+">>>>"+v);
        });

        return map;
    }
    /**
     * 获取 子节点的被迭代方法
     * @param childElements
     * @param mapEle
     * @return
     */
    private Map<String, String> getAllElements(List<Element> childElements,Map<String,String> mapEle) {
        for (Element ele : childElements) {
            if(ele.elements().size()>0){
                mapEle = getAllElements(ele.elements(), mapEle);
            }else{
                mapEle.put(ele.getName(), ele.getText());
            }
        }
        return mapEle;
    }


}
