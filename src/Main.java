import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class Main {
    private static int MAX_CHUNK_SIZE = 5;
    private static DocumentBuilder docBuilder = null;
    private static Transformer docTransformer = null;

    public static void main(String[] args) throws SAXException, IOException,
            ParserConfigurationException, TransformerException {

        initializeBuilderAndTransformer();
        Document sourceXml = docBuilder.parse(new File("src/books.xml"));

        int currentChunkSize = 0;
        Document targetXml = createNewChunk();
        NodeList nodeList = sourceXml.getElementsByTagName("book");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                if (currentChunkSize < MAX_CHUNK_SIZE) {
                    addNodeToRoot(targetXml, node);
                    currentChunkSize++;
                } else {
                    emitChunkedXml(targetXml);
                    targetXml = createNewChunk();
                    addNodeToRoot(targetXml, node);
                    currentChunkSize = 1;
                }
            }
        }
        emitChunkedXml(targetXml);
    }

    private static void initializeBuilderAndTransformer() throws ParserConfigurationException, TransformerConfigurationException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilder = docBuilderFactory.newDocumentBuilder();
        TransformerFactory factory = TransformerFactory.newInstance();
        docTransformer = factory.newTransformer();
    }

    private static Document createNewChunk() {
        Document chunkedXml = docBuilder.newDocument();
        Element rootElement = chunkedXml.createElement("catalog");
        chunkedXml.appendChild(rootElement);
        return chunkedXml;
    }

    private static void addNodeToRoot(Document xml, Node node) {
        Node rootElement = xml.getElementsByTagName("catalog").item(0);
        rootElement.appendChild(xml.importNode(node, true));
    }

    private static void emitChunkedXml(Document xml) throws TransformerException {
        Source source = new DOMSource(xml);
        StringWriter stringWriter = new StringWriter();
        Result result = new StreamResult(stringWriter);
        docTransformer.transform(source, result);
        System.out.println(stringWriter.getBuffer());
    }
}