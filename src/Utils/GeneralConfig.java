/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author joako
 */
public class GeneralConfig {
    
    private File configFile = new File("generalConfig.properties");
    private Properties configProperties;
    private String percentage;
    
    public void saveProperty(String key, String value) throws FileNotFoundException, IOException{
        getConfigProperties().setProperty(key, value);
        OutputStream output;
        output = new FileOutputStream(getConfigFile());
        getConfigProperties().store(output, "general configurations");
        output.close();
    }

    public void loadProperties() throws IOException{
        Properties defaultProps = new Properties();
        InputStream input;
        setConfigProperties(defaultProps);
        try {
            input = new FileInputStream(getConfigFile());
            getConfigProperties().load(input);
            input.close();
            setPercentage(getConfigProperties().getProperty("percentage", "0"));
        } catch (FileNotFoundException ex) {
            createProperties();
        }
    }

    /**
     * @return the configFile
     */
    public File getConfigFile() {
        return configFile;
    }

    /**
     * @return the configProperties
     */
    public Properties getConfigProperties() {
        return configProperties;
    }

    /**
     * @return the percentage
     */
    public String getPercentage() {
        return percentage;
    }

    /**
     * @param configFile the configFile to set
     */
    public void setConfigFile(File configFile) {
        this.configFile = configFile;
    }

    /**
     * @param configProperties the configProperties to set
     */
    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }

    /**
     * @param percentage the percentage to set
     */
    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    private void createProperties() {
        getConfigProperties().setProperty("percentage", "0");
        OutputStream outputStream;
        try {
            outputStream = new FileOutputStream(configFile);
            getConfigProperties().store(outputStream, "general configurations");
            outputStream.close();
            setPercentage(getConfigProperties().getProperty("percentage","0"));
        } catch (IOException ex) {
            Logger.getLogger(GeneralConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
