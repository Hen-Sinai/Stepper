package utils;

import Exceptions.NoXmlFormat;
import jaxb.schema.generated.STStepper;
import mySchema.stStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;

public class Xml implements Serializable {
    private final static String JAXB_XML_PACKAGE_NAME = "jaxb.schema.generated";

    public static stStepper readFromXml(InputStream inputStream) throws JAXBException, IOException, NoXmlFormat {
//        if (!path.endsWith(".xml")) {
//            throw new NoXmlFormat();
//        }
        return deserializeFrom(inputStream);
    }

    private static stStepper deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        STStepper STStepper = (STStepper) u.unmarshal(in);
        return new stStepper(STStepper);
    }
}
