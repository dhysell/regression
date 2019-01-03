package repository.cc.framework.gw.cc.policyPlugin;


import repository.cc.framework.gw.cc.policyPlugin.dto.Policy;
import repository.cc.framework.gw.http.HttpGet;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PolicyPluginListener {

    public static Policy getPolicyInformationFor(String policyNumber, LocalDate asOfDate) {
        policyNumber = policyNumber.replaceAll("-", "");
        String date = asOfDate.format(DateTimeFormatter.ofPattern("YYYY-MM-dd"));
        try {
            String response = HttpGet.get("http://mbdev.idfbins.com:7080/nexus/policy/retrieve?policyNumber=" + policyNumber + "&asOfDate=" + date);
            Unmarshaller unmarshaller = JAXBContext.newInstance(Policy.class).createUnmarshaller();
            XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(new StringReader(response));

            return unmarshaller.unmarshal(xmlStreamReader, Policy.class).getValue();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
