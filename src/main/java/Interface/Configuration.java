package Interface;

import external_file.PropertiesOracle;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.convert.DefaultListDelimiterHandler;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.FileBased;
import org.apache.commons.configuration2.io.FileHandler;
import org.apache.commons.configuration2.sync.ReadWriteSynchronizer;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Configuration extends JPanel {
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField4;
    private JButton returnButton;
    private JButton applyButton;
    private JPasswordField passwordField1;
    JPanel interfazConf;
    public static FileBasedConfigurationBuilder<FileBasedConfiguration> builder;


    /**
     *  Constructor of the Configuration page
     */

    public Configuration() {


        returnButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //frame.setVisible(false);
                MainInterface main = new MainInterface();
                main.visible();
            }
        });

        applyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


                org.apache.commons.configuration2.Configuration config=configPara();

                    config.setProperty("endpoint", textField1.getText());
                    config.setProperty("user", textField2.getText());
                    config.setProperty("password", passwordField1.getPassword());
                    config.setProperty("path", textField4.getText());

                    // configuration of the password:
                    String pass = StringUtils.join(config.getList("password")).replace("[","").replace("]","").replace(",","");
                    // encriptation of the password
                    config.clearProperty("password");
                try {
                    pass = Base64.getEncoder().encodeToString(pass.getBytes("utf-8"));
                } catch (UnsupportedEncodingException e1) {
                    e1.printStackTrace();
                }

                config.setProperty("password1",pass);


                // Create a file handler and associate it with the configuration
                FileHandler handler = new FileHandler((FileBased) config);

                // Make changes persistent
                    try
                    {
                        builder.save();
                    }
                    catch(ConfigurationException cex)
                    {
                    }
            }
        });
        //Load the save configuration to access to the Api of Fusion
        setting();
    }

    public org.apache.commons.configuration2.Configuration configPara() {

        Parameters params = new Parameters();
        org.apache.commons.configuration2.Configuration config = null;

        try
        {
        File propertiesFile = new File("oracle.properties");
        builder = new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class).configure(params.properties().setFile(propertiesFile));
            config = builder.getConfiguration();
            return config;
        }
        catch(ConfigurationException cex)
        {
            cex.printStackTrace();
        }
        return config;
    }

    /**
     * Set up the configuration settings
     */

    public void setting(){

        org.apache.commons.configuration2.Configuration config=configPara();

               if(config != null){
                   String pass = "";
                   int len=config.getString("password1").length();

                   textField1.setText(config.getString("endpoint"));
                   textField2.setText(config.getString("user"));
                   //desencrypt the password
                   if (len!=0) {

                       try {

                           byte[] decode = Base64.getDecoder().decode(config.getString("password1").getBytes());
                           pass = new String(decode, "utf-8");
                       } catch (UnsupportedEncodingException ex) {
                           ex.printStackTrace();
                       }
                   }
                   passwordField1.setText(pass);
                   textField4.setText(config.getString("path"));
               }
    }
}