public void SAXReaderDtd01() {
        // import org.dom4j.io.SAXReader;
        SAXReader reader = new SAXReader();
        // 不完全禁用DTD
        // 防XXE 漏洞
        reader.setFeature("http://xml.org/sax/features/external-general-entities", false);
        // 防Bomb 漏洞
        reader.setFeature("http://xml.org/sax/features/external-parameter-entities", false);

        // ...
    }
    public void SAXReaderDtd02() {
        SAXReader reader = new SAXReader();
        //
        reader.setFeature("http://apache.org/xml/features/disallow-doctype-decl",true);

        // ...
    }

    public void XMLInputFactoryDtd01() {
        File f = new File("demo.xml");
        InputStream in = new FileInputStream(f);
        // import javax.xml.stream.XMLInputFactory;
        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        // 不完全禁用DTD
        xmlFactory.setProperty(XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, false);
        xmlFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
        // ...
    }

    public void XMLInputFactoryDtd02() {
        File f = new File("demo.xml");
        InputStream in = new FileInputStream(f);
        // import javax.xml.stream.XMLInputFactory;
        XMLInputFactory xmlFactory = XMLInputFactory.newInstance();
        // 完全禁用DTD
        xmlFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
        // ...
    }
