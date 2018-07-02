package upload_file;

import java.io.*;


import upload_file.UploadFile;
import org.apache.commons.configuration2.Configuration;
import external_file.PropertiesOracle;


/*************************************
 *                                   *
 *   Created by: Alejandro Alexiades *
 *                                   *
 *   Child class UploadFile          *
 *                                   *
 *   Funcionality:                   *
 *                                   *
 *   -Process and import CSV files   *
 *   to Oracle Fusion                *
 *   Son class of UploadFile         *
 *                                   *
 *************************************/


public class UploadMain extends UploadFile implements Runnable {

    @Override
    public void task() {

        try {

                // Obtenemos el codigo de sesion
                //xslt
                PropertiesOracle propertiesOracle = PropertiesOracle.getInstance();
                Configuration oracleConf = propertiesOracle.builder.getConfiguration();
                String endPoint = oracleConf.getString("dat_end");
                String url = oracleConf.getString("dat_url");

                //Inicializo las variables
                String pathNomina = oracleConf.getString("dat_path_nomina");
                String user = oracleConf.getString("user");
                String password = oracleConf.getString("password");
                String soapDat = oracleConf.getString("dat_soap");
                String [][] contentIdList = new String[0][2];

                /******** Change files format and compress to ZIP ************/


                final File fold = new File(pathNomina);
                if (fold.listFiles().length != 0) {
                        csvToDat(fold, pathNomina,"");


                /******** Import Files to Fusion  **************/

                //Extraigo el ContentID para poder dejar ficheros

                final File folder = new File(pathNomina);
                contentIdList = listFilesForFolder(folder, user, password, url);

                /******** Process files in Fusion **************/

                    listFilesForFolderProcess(contentIdList, user, password, endPoint, soapDat);

                }

            }catch(Exception e){
                e.printStackTrace();
            }
    }
}


