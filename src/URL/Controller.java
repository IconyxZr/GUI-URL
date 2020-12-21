/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package URL;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
/**
 *
 * @author ASUS
 */
public class Controller {

    private GUIURL view;

    public Controller(GUIURL view) {
        this.view = view;

        this.view.getBtnInput().addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    searchUrl(view.getInputURL().getText());
                } catch (IOException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void searchUrl(String url) throws MalformedURLException, IOException {
        URL alamat = new URL(url);
        URLConnection yc = alamat.openConnection();
        BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            System.out.println(inputLine);
        }

        // JUDUL
        InputStream response = null;
        response = new URL(url).openStream();
        Scanner scanner = new Scanner(response);
        String responseBody = scanner.useDelimiter("\\A").next();
        this.view.getOutput_judul().setText(responseBody.substring(responseBody.indexOf("<title>") + 7, responseBody.indexOf("</title>")));

        Document doc = (Document) Jsoup.connect(url).get();
        String keywords = "";
        String description = "";
        if(!doc.select("meta[name=keywords]").isEmpty() || !doc.select("meta[name=description]").isEmpty()){
            keywords = doc.select("meta[name=keywords]").first().attr("content");
            description = doc.select("meta[name=description]").get(0).attr("content");
        }else{
            this.view.getOutput_keyword().append("");
            JOptionPane.showMessageDialog(null, "Keyword atau Deskripsi tidak ditemukan");
        }
        
        // KEYWORD
        this.view.getOutput_keyword().append("Keyword : \n");
        this.view.getOutput_keyword().append(keywords);
        
        // DESKRIPSI
        this.view.getOutput_deskripsi().append("\nDescription : \n");
        this.view.getOutput_deskripsi().append(description);
            
        // CONTENT
        this.view.getOutput_isi().setText(responseBody.substring(responseBody.indexOf("<p>") + 7, responseBody.indexOf("</p>")));   
            
        // TAMPILAN WEB
        this.view.getTampil_halaman().setPage(url);
        
        in.close();

    }
            
}
