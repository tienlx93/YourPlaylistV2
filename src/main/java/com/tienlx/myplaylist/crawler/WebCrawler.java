/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tienlx.myplaylist.crawler;

import com.tienlx.myplaylist.dao.ArtistDAO;
import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Artist;
import com.tienlx.myplaylist.entity.Song;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.tienlx.myplaylist.util.AccentRemover;

/**
 *
 * @author tienl_000
 */
public class WebCrawler {

    public static DB db = new DB();
    private String baseUrl = "http://m.mp3.zing.vn";
    private String basePath;

    public static void main(String[] args) {
        WebCrawler crawler = new WebCrawler();
        try {
            crawler.processPage("http://m.mp3.zing.vn");
            crawler.processSongList("http://m.mp3.zing.vn/top-100/bai-hat-Nhac-Tre/IWZ9Z088.html");
            crawler.processSong("http://m.mp3.zing.vn/bai-hat/Giu-Em-Di-Thuy-Chi/ZW6EZDII.html");
            crawler.processArtist("Sơn Tùng M-TP");
            crawler.testSaveArtist();
        } catch (IOException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processPage(String URL) throws IOException {

        Document doc = Jsoup.connect(URL).get();

        // List song by category
        Elements type = doc.select(".link-obj");
        String subUrl;
        String name;
        for (int i = 0; i < type.size(); i++) {
            Element element = type.get(i);
            subUrl = element.attr("href");
            name = element.text();
            if (subUrl.contains("top")) {
                System.out.println(subUrl + "-" + name);
                try {
                    processSongList(URL + subUrl);
                } catch (SQLException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IndexOutOfBoundsException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public void processSongList(String URL) throws SQLException, IOException, IndexOutOfBoundsException {

        Document doc = Jsoup.connect(URL).get();

        Elements type = doc.select(".content-items");
        Elements categoryElm = doc.select(".selected[selected]");

        //TODO
        String category = categoryElm.html();
        String url;
        String title, titleSearch;
        String artist, artistSearch;
        String id;
        long views;
        HashMap map;

        com.tienlx.myplaylist.entity.Song songEntity;
        com.tienlx.myplaylist.jaxb.song.Song songJaxb;
        SongDAO dao;

        for (int i = 0; i < type.size(); i++) {
            Element element = type.get(i);
            url = element.attr("href");
            id = url.substring(url.lastIndexOf("/") + 1, url.lastIndexOf(".html"));
            System.out.println("Crawl: " + url);

            if (element.html().length() == 0) {
                continue;
            }
            title = element.select("h3").get(0).text();
            titleSearch = AccentRemover.removeAccent(title);
            artist = element.select("h4").get(0).text();
            artistSearch = AccentRemover.removeAccent(artist);

            dao = new SongDAO();

            Song found = dao.get(id);
            if (found == null) {
                try {
                    songEntity = new Song(id, title, titleSearch, artist, artistSearch, 0, category);
                    map = processSong(getBaseUrl() + url); //throw exception

                    views = Long.parseLong(map.get("Views").toString());
                    songEntity.setPlayCount(views);
                    
                    dao.save(songEntity);
                    songJaxb = new com.tienlx.myplaylist.jaxb.song.Song();

                    songJaxb.setAlbumArt(map.get("AlbumArt").toString());
                    songJaxb.setLyrics(map.get("Lyrics").toString());
                    songJaxb.setSource(map.get("Source").toString());
                    songJaxb.setTitle(title);
                    songJaxb.setArtist(artist);

                    marshallXML(getBasePath() + "song/" + id + ".xml", songJaxb);
                } catch (org.hibernate.exception.DataException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IndexOutOfBoundsException ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public HashMap<String, String> processSong(String URL) throws SQLException, IOException, IndexOutOfBoundsException {

        HashMap<String, String> map = new HashMap<String, String>();

        Document doc = Jsoup.connect(URL).get();

        Elements artistImg = doc.select(".artist-img");
        Elements lyricsElm = doc.select("#conLyrics");
        Elements audio = doc.select("#mp3Player");

        String strViews;
        long views;
        strViews = doc.select(".icon-luot-nghe").get(0).text();
        views = Long.parseLong(strViews) / 100000; // reduce views

        String albumArt = artistImg.attr("src").replace("94_94", "165_165");
        String source = audio.attr("xml");
        Document dataJson = Jsoup.connect(source).get();
        String data = dataJson.text();
        String lyrics = "";
        if (lyricsElm.text().length() > 0) {
            lyrics = lyricsElm.get(0).html();
        }

        map.put("AlbumArt", albumArt);
        map.put("Lyrics", lyrics);
        map.put("Source", data);
        map.put("Views", views + "");

        return map;
    }

    public void processArtistList() {
        ArtistDAO artistDao;
        SongDAO songDao = new SongDAO();
        List<String> artists = songDao.listArtists();
        //List<String> demo = artists.subList(10, 15);
        for (String artist : artists) {
            if (artist.indexOf("ft.") > 0) {
                artist = artist.substring(0, artist.indexOf("ft")).trim();
            }
            try {
                artistDao = new ArtistDAO();
                String artistNameSearch = AccentRemover.removeAccent(artist);
                Artist found = artistDao.get(artistNameSearch);
                if (found == null) {
                    processArtist(artistNameSearch);
                }
            } catch (SQLException ex) {
                Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void processArtist(String name) throws SQLException, IOException {
        String url = getBaseUrl() + "/nghe-si/" + AccentRemover.removeAccent(name);
        System.out.println(url);
        try {
            Document doc = Jsoup.connect(url).get();

            Elements imgElm = doc.select(".artist-img");
            Elements infoElm = doc.select(".info-artist");
            Elements bioElm = doc.select("#fnBiography");


            String artistName = doc.select(".content-items h3").text();
            String artistNameSearch = AccentRemover.removeAccent(artistName);
            String image = imgElm.attr("src").replace("94_94", "165_165");
            String info = "";
            String bio = "";
            if (!infoElm.isEmpty()) {
                info = infoElm.get(0).html();
            }
            if (!bioElm.isEmpty()) {
                bio = bioElm.get(0).html();
            }
            ArtistDAO artistDao = new ArtistDAO();
            Artist found = artistDao.get(artistNameSearch);
            if (found == null) {
                com.tienlx.myplaylist.entity.Artist artist = new Artist(artistNameSearch, artistName, image, bio, info, artistNameSearch);
                artistDao.save(artist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }



    }

    public void marshallXML(String filename, com.tienlx.myplaylist.jaxb.song.Song song) {
        try {
            JAXBContext context = JAXBContext.newInstance(com.tienlx.myplaylist.jaxb.song.Song.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(song, new File(filename));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void marshallXML(String filename, Artist artist) {
        try {
            JAXBContext context = JAXBContext.newInstance(Artist.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.marshal(artist, new File(filename));
        } catch (Exception e) {
        }
    }

    public void testSaveArtist() throws SQLException, IOException {
        String category = "Nhạc trẻ";
        String url = "blah";
        String title = "Nhạc của tui";
        String artist = "Lê Tiến";
        String id = "WB024525";


        com.tienlx.myplaylist.entity.Song songEntity;
        com.tienlx.myplaylist.jaxb.song.Song songJaxb;
        SongDAO dao = new SongDAO();

        Song found = dao.get(id);
        if (found == null) {
            //songEntity = new Song(id, title, artist, 0, 0, category);
            //dao.save(songEntity);

            songJaxb = new com.tienlx.myplaylist.jaxb.song.Song();

            songJaxb.setAlbumArt("AlbumArt");
            songJaxb.setLyrics("Lyrics");
            songJaxb.setSource("Source");
            songJaxb.setTitle(title);
            songJaxb.setArtist(artist);

            //marshallXML(getBasePath() + "song/" + id + ".xml", songJaxb);

            marshallXML("web/song/" + id + ".xml", songJaxb);
        }

    }

    /**
     * @return the baseUrl
     */
    public String getBaseUrl() {
        return baseUrl;
    }

    /**
     * @param baseUrl the baseUrl to set
     */
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    /**
     * @return the basePath
     */
    public String getBasePath() {
        return basePath;
    }

    /**
     * @param basePath the basePath to set
     */
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }
}
