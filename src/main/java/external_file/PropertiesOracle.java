package external_file;


import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.sync.ReadWriteSynchronizer;



/*************************************
 *                                   *
 *   Created by: Asier               *
 *   Modify by: Alejandro Alexiades  *
 *                                   *
 *   Funcionality:                   *
 *                                   *
 *  - Sturctures the XSLT text       *
 *                                   *
 *************************************/


public class PropertiesOracle {

    //inicializacion de las variables
    public static PropertiesOracle ourInstance = new PropertiesOracle();
    public static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;


    public static PropertiesOracle getInstance() {
        return ourInstance;
    }

    private PropertiesOracle() {
        //super("oracle.properties");
        Parameters params = new Parameters();
        try
        {
            builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.properties().setFileName("oracle.properties"));
            Configuration config;
            config = builder.getConfiguration();
            config.setSynchronizer(new ReadWriteSynchronizer());
        }
        catch(ConfigurationException cex)
        {
            cex.printStackTrace();
        }
    }


    public static void ChangeValues(String value){

    }
}