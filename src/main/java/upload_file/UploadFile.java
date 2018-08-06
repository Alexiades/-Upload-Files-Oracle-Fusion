package upload_file;


import external_file.PropertiesOracle;
import external_file.XSLT;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.zeroturnaround.zip.ZipUtil;

import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


/*************************************
 *                                   *
 *   Created by: Alejandro Alexiades *
 *                                   *
 *                                   *
 *   Funcionality:                   *
 *                                   *
 *  - Convert CSV files to DAT       *
 *  - Zip files                      *
 *  - Upload files to Fusion         *
 *  - Process files in Fusion        *
 *  - Delete the files               *
 *************************************/


public abstract class UploadFile implements Runnable {

    /**
     *  Start
     */

 //   @Override
    public void run() {

        task();
        // End the program
        System.exit(0);

    }

    public abstract void task();

    /**
     * Rename the files to DAT
     * ZIP files
     * Delete process files
     *
     * @param fold
     * @param path
     */


    public void csvToDat(final File fold,String path,String type) throws IOException {

        String dRename = "";
        String dRenameDat = "";
        String dRenameZip = "";
        File file2;
        File fileZ;

        for (final File fileEntry : fold.listFiles()) {
            if (fileEntry.isDirectory()) {
                // ZIP directory
                ZipUtil.pack(new File(fileEntry.getAbsolutePath()), new File(fileEntry.getAbsolutePath() + ".zip"));
                delete(fileEntry);
                type = "Directory";
                csvToDat(fold, path, type);
            } else {

                if (type != "Directory") { // The file is already transform.
                    try {
                        // File (or directory) with old name
                        dRename = fileEntry.getName();
                        if (dRename != null && dRename.contains(".dat")) {
                            dRenameDat = dRename;
                            dRename = dRename.substring(0, dRename.lastIndexOf('.'));
                            dRenameZip = path + "/" + dRename + ".zip";
                            dRename = path + "/" + dRename + ".dat";
                            // File (or directory) with new name
                            fileZ = new File(dRenameZip);
                            if (fileZ.exists())
                                throw new java.io.IOException("file exists");
                            // Rename file (or directory)

                            zipFiles(dRenameZip, dRename, dRenameDat);
                            fileEntry.delete();
                        } else if (dRename != null && dRename.contains(".")) {

                            dRename = dRename.substring(0, dRename.lastIndexOf('.'));
                            dRenameDat = dRename + ".dat";
                            dRenameZip = path + "/" + dRename + ".zip";
                            dRename = path + "/" + dRename + ".dat";

                            // File (or directory) with new name
                            file2 = new File(dRename);

                            fileZ = new File(dRenameZip);

                            if (file2.exists() || fileZ.exists())
                                throw new java.io.IOException("file exists");

                            // Rename file (or directory)
                            boolean success = fileEntry.renameTo(file2);
                            boolean successZip = fileEntry.renameTo(fileZ);


                            // Zip the file

                            ZipOutputStream zipOuot = new ZipOutputStream(new FileOutputStream(dRenameZip));
                            ZipEntry entry = new ZipEntry(dRenameDat);
                            FileInputStream fin = new FileInputStream(dRename);
                            try {
                                zipOuot.putNextEntry(entry);
                                for (int a = fin.read(); a != -1; a = fin.read()) {
                                    zipOuot.write(a);
                                }
                                fin.close();
                                zipOuot.close();
                            } catch (IOException ioe) {
                            }


                            /*** Compress end ***/

                            if (!success || !successZip) {
                                // File was not successfully renamed
                            }
                            System.out.println(fileEntry.getName());
                            // Delete de Dat file
                            file2.delete();
                        }
                    } catch (IOException e) {
                        System.err.println("Error in the rename");
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * Extract the path of the files
     * and process each file one by one
     * to take the ContentID of each file
     *
     * @param folder, user, pass, url
     * @return list of ContentID
     */

    public String[][] listFilesForFolder(final File folder,String user, String pass, String url) throws IOException {

        PropertiesOracle propertiesOracle = PropertiesOracle.getInstance();
        Configuration oracleConf = null;
        try {
            oracleConf = propertiesOracle.builder.getConfiguration();
        } catch (ConfigurationException e) {
            e.printStackTrace();
        }

        String dDocAccount ="hcm/dataloader/import";
        String rutaJar= oracleConf.getString("path_jar_fus");
        String dDocTitle = "";
        String primaryFile = "";
        BufferedReader inStream = null;
        int row =  folder.listFiles().length;
        int colum = 2;
        int colum0 = 0;
        int colum1 = 1;
        String [][] listID = new String[row][colum];
        int i=0;

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry,user,pass,url);
            } else {

                try
                {
                    dDocTitle = fileEntry.getName();
                    primaryFile = fileEntry.getAbsolutePath();
                    System.out.println(fileEntry.getName());
                    System.out.println(fileEntry.getAbsolutePath());
                    Process ConIdProcess = Runtime.getRuntime().exec("java -classpath "+rutaJar+" oracle.ucm.client.UploadTool url="+url+" username="+user+" password="+pass+" primaryFile="+primaryFile+" dDocTitle="+dDocTitle+" -dDocAccount="+dDocAccount);
                    BufferedReader in = new BufferedReader(new InputStreamReader(ConIdProcess.getInputStream()));
                    String line;
                    String liner = "";
                    while ((line = in.readLine()) != null) {
                        liner = line;
                    }

                    //Extract the return values of the Fusion command
                    String str = liner.substring(liner.lastIndexOf("[")+1, liner.indexOf("|")+1);
                    String str2= liner.substring(liner.lastIndexOf("|") + 1, liner.indexOf("]")+1);
                    String dID = str.substring(str.lastIndexOf("=") + 1, str.lastIndexOf("|"));
                    dID = dID.replaceAll("\\s+"," ");
                    String dDocName= str2.substring(str2.lastIndexOf("=") + 1, str2.indexOf("]"));
                    ConIdProcess.waitFor();
                    listID[i][colum0]= dID;
                    listID[i][colum1]= dDocName;
                    i = i+1;
                    fileEntry.delete();

                }
                catch(IOException e)      {
                    System.err.println("Error in the exec() call to Fusion");
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return listID;
    }



    /**
     *
     * Processes the files in status Upload/Import in Fusion
     *
     * @param Files
     * @param user
     * @param pass
     * @param endPoint
     * @param soapDat
     * @throws IOException
     */

    public void listFilesForFolderProcess(String[][] Files ,String user, String pass, String endPoint, String soapDat) throws IOException {

        ClassLoader classLoader = getClass().getClassLoader();

        for(int i= 0 ; i < Files.length;i++) {

            //Self-identify in SOAPIU in the standard way

            HttpURLConnection connection = (HttpURLConnection) new URL(soapDat).openConnection();
            String encoded = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes(StandardCharsets.UTF_8));  //Java 8
            connection.setRequestProperty("Authorization", "Basic " + encoded);

            //Replace the ContentId variables inside the XML of the Web Service
            String inputXML = "<root><contentId>" + Files[i][1] + "</contentId></root>";
            File xsltFile = new File(classLoader.getResource("xslt/importAndLoadDataAsync.xslt").getFile());
            String output = null;
            try {
                output = XSLT.transform(inputXML, xsltFile);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            }


            // call Web Service

            // Send SOAP Message to SOAP Server
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost(endPoint);

            // Create the del XML header and define the security
            httppost.addHeader("Content-type", "text/xml; charset=utf-8");
            httppost.setHeader("Authorization", "Basic " + encoded);

            StringEntity requestEntity = new StringEntity(output, ContentType.APPLICATION_XML);
            httppost.setEntity(requestEntity);
            CloseableHttpResponse response = httpclient.execute(httppost);
            HttpEntity ent = response.getEntity();
            String responseString = EntityUtils.toString(ent, "UTF-8");

        }
    }

        /**
         *
         * Compress files
         *
         * @param zip ZIP name
         * @param name
         * @param dat
         * @throws IOException
         */

        public void zipFiles(String zip,String name,String dat) throws IOException{

            // Zip the file

            ZipOutputStream zipOuot = new ZipOutputStream(new FileOutputStream(zip));
            ZipEntry entry = new ZipEntry(dat);
            FileInputStream fin = new FileInputStream(name);
            try {
                zipOuot.putNextEntry(entry);
                for (int a = fin.read(); a != -1; a = fin.read()) {
                    zipOuot.write(a);
                }
                fin.close();
                zipOuot.close();
            } catch (IOException ioe) {
            }
            /*** compress end ***/
        }

    /**
     * Delete a file or a directory and its children.
     * @param file The directory to delete.
     * @throws IOException Exception when problem occurs during deleting the directory.
     */
    private static void delete(File file) throws IOException {

        for (File childFile : file.listFiles()) {

            if (childFile.isDirectory()) {
                delete(childFile);
            } else {
                if (!childFile.delete()) {
                    throw new IOException();
                }
            }
        }

        if (!file.delete()) {
            throw new IOException();
        }
    }


    }

