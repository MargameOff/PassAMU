package com.passamu.xml;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import java.io.*;

public class XmlConverter<T> {

    private final Class<T> documentClass;
    private final XmlMapper mapper;

    public XmlConverter(Class<T> documentClass) {
        this.documentClass = documentClass;
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        this.mapper = new XmlMapper(module);
        this.mapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.mapper.enable(ToXmlGenerator.Feature.WRITE_XML_DECLARATION);
    }

    
    /** 
     * @param document est un des paramètre utilisé dans cette fonction, il correspond au document d'entrée dans lequel écrire.
     * @param outputStream est le flux de bytes pour écrire sur la cible, qui est ici document.
     * @throws IOException Gestion des exceptions.
     */
    public void write(T document, OutputStream outputStream) throws IOException {
        mapper.writeValue(outputStream, document);
    }

    
    /** 
     * @param inputStream
     * Flux de donnée depuis la source, ici documentClass, permettant la lecture dans celui-ci.
     * @return T XmlMapper
     * @throws IOException
     * Gestion des exceptions.
     */
    public T read(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, documentClass);
    }
}
