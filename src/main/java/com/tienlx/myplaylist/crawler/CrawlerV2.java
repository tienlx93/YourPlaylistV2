package com.tienlx.myplaylist.crawler;

import com.google.gson.Gson;
import com.tienlx.myplaylist.dao.ArtistDAO;
import com.tienlx.myplaylist.dao.SongDAO;
import com.tienlx.myplaylist.entity.Artist;
import com.tienlx.myplaylist.entity.Song;
import com.tienlx.myplaylist.util.AccentRemover;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by ASUS on 10/08/2016.
 */
public class CrawlerV2 {
    private String baseUrl = "http://m.mp3.zing.vn";
    private String basePath;
    public static final String imageSrc = "http://image.mp3.zdn.vn/";
    public static final String apiKey = "fafd463e2131914934b73310aa34a23f";
    public static final String baseAPI = "http://api.mp3.zing.vn/api/mobile/song/getsonginfo";

    public static void main(String[] args) {
        CrawlerV2 crawler = new CrawlerV2();
        try {
            String URL = "ZW67FWWF";
            crawler.processSong(URL);
            //crawler.processPage("http://mp3.zing.vn/bang-xep-hang/index.html");
            /*crawler.processSongList("http://m.mp3.zing.vn/top-100/bai-hat-Nhac-Tre/IWZ9Z088.html");
            crawler.processSong("http://m.mp3.zing.vn/bai-hat/Giu-Em-Di-Thuy-Chi/ZW6EZDII.html");
            crawler.processArtist("S?n Tùng M-TP");*/
            crawler.testSaveArtist();
        } catch (IOException ex) {
            Logger.getLogger(CrawlerV2.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(CrawlerV2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void processPage(String URL) throws IOException {

        Document doc = Jsoup.connect(URL).get();

        // List song by category
        Elements type = doc.select("._trackLink");
        String subUrl;
        String name;
        for (int i = 0; i < type.size(); i++) {
            Element element = type.get(i);
            subUrl = element.attr("href");
            name = element.text();
            if (subUrl.contains("bai-hat")) {
                System.out.println(subUrl + "-" + name);
                try {
                    processSong(subUrl);
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

        String url;
        url = baseAPI + "?keycode=" + apiKey + "&requestdata={\"id\":\"" + URL + "\"}";
        String response = null;
        String albumArt;
        String lyrics;
        String data;
        String views;
        String title;
        try {
            response = HTTPRequest.getHTML(url);
            if (response == null) {
                return null;
            }
            Gson gson = new Gson();
            HashMap parsed = gson.fromJson(response, Map.class);
            albumArt = (String) parsed.get("thumbnail");
            lyrics = (String) parsed.get("lyrics_file");
            views = (String) parsed.get("likes");
            HashMap sources = (HashMap) parsed.get("source");
            data = (String) sources.get("128");

            map.put("Title", title);
            map.put("AlbumArt", albumArt);
            map.put("Lyrics", lyrics);
            map.put("Source", data);
            map.put("Views", views + "");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


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
                com.tienlx.myplaylist.entity.Artist artist = new Artist(artistNameSearch, artistName, image, bio, info);
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
        String category = "Nh?c tr?";
        String url = "blah";
        String title = "Nh?c c?a tui";
        String artist = "Lê Ti?n";
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
