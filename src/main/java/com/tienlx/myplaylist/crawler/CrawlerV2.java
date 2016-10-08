package com.tienlx.myplaylist.crawler;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
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
    public static final String artistBaseAPI = "http://api.mp3.zing.vn/api/mobile/artist/getartistinfo";
    public static final String lyricBaseAPI = "http://api.mp3.zing.vn/api/mobile/song/getlyrics";
    private ArtistDAO artistDao = new ArtistDAO();
    SongDAO songDao = new SongDAO();

    public static void main(String[] args) {
        CrawlerV2 crawler = new CrawlerV2();
        crawler.setBasePath("D:\\My Documents\\Documents\\_Projects\\MyPlaylist\\src\\main\\webapp\\web\\");
/*        try {
            String URL = "http://mp3.zing.vn/bai-hat/Gui-Anh-Xa-Nho-Bich-Phuong/ZW7UFI6I.html";
            crawler.processPage("http://mp3.zing.vn/bang-xep-hang/index.html");

//            HashMap<String, String> testSong = crawler.processSong(URL);
//            crawler.saveArtist(testSong);
        } catch (Exception ex) {
            Logger.getLogger(CrawlerV2.class.getName()).log(Level.SEVERE, null, ex);
        }*/
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
                    HashMap<String, String> song = processSong(subUrl);
                    if (song != null && song.get("Title") != null && song.get("Title").length() > 0) {
                        saveArtist(song);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    public HashMap<String, String> processSong(String URL) throws UnsupportedEncodingException {
        String id = URL.substring(URL.lastIndexOf("/") + 1, URL.lastIndexOf(".html"));
        HashMap<String, String> map = new HashMap<String, String>();

        Song found = songDao.get(id);
        if (found != null) {
            return null;
        }
        String url;
        url = baseAPI + "?keycode=" + apiKey + "&requestdata=" + URLEncoder.encode("{\"id\":\"" + id + "\"}", "UTF-8");
        String response = null;
        String albumArt;
        String lyrics;
        String data;
        String views;
        String title;
        String category;
        String artist;
        String artistID;
        try {
            response = HTTPRequest.getHTML(url);
            if (response == null) {
                return null;
            }
            Gson gson = new Gson();
            HashMap parsed = gson.fromJson(response, HashMap.class);
            title = (String) parsed.get("title");
            albumArt = (String) parsed.get("thumbnail");
            lyrics = (String) parsed.get("lyrics_file");
            category = (String) parsed.get("genre_name");
            category = category.substring(category.lastIndexOf(", ") >= 0 ? category.lastIndexOf(", ") + 2 : 0);
            DecimalFormat df = new DecimalFormat("#");
            views = df.format(Math.floor((Double) parsed.get("total_play") / 10000));
            LinkedTreeMap sources = (LinkedTreeMap) parsed.get("source");
            data = (String) sources.get("128");
            artist = (String) parsed.get("artist");
            try {
                Double a = (Double) parsed.get("artist_id");
                artistID = df.format(a);
            } catch (Exception e) {
                artistID = (String) parsed.get("artist_id");
            }

            map.put("Title", title);
            map.put("AlbumArt", albumArt);
            map.put("Lyrics", lyrics);
            map.put("Source", data);
            map.put("Views", views + "");
            map.put("category", category);
            map.put("artist", artist);
            map.put("artistID", artistID);
            map.put("Id", id);
            map.put("URL", URL);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return map;
    }

    public void processArtist(String artistID) throws SQLException, IOException {
        String url;
        url = artistBaseAPI + "?keycode=" + apiKey + "&requestdata=" + URLEncoder.encode("{\"id\":" + artistID + "}", "UTF-8");
        String response;
        String artistName;
        String artistNameSearch;
        String image;
        String bio;
        artistDao = new ArtistDAO();
        Artist found = artistDao.get(artistID);
        if (found != null) {
            return;
        }
        try {
            response = HTTPRequest.getHTML(url);
            if (response == null) {
                return;
            }
            Gson gson = new Gson();
            HashMap parsed = gson.fromJson(response, HashMap.class);
            artistName = (String) parsed.get("name");
            artistNameSearch = artistName != null ? AccentRemover.removeAccent(artistName) : "";
            image = imageSrc + parsed.get("avatar");
            bio = (String) parsed.get("biography");

            com.tienlx.myplaylist.entity.Artist artist = new Artist(artistNameSearch, artistName, image, bio, "", artistID);
            artistDao.save(artist);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println(url);
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

    public void saveArtist(HashMap<String, String> testSong) throws SQLException, IOException {
        String category = testSong.get("category");
        String title = testSong.get("Title");
        String artist = testSong.get("artist");
        String id = testSong.get("Id");

        String albumArt;
        String lyrics;
        String data;
        String views = testSong.get("Views");
        long viewLong = Long.parseLong(views);
        String artistID;
        com.tienlx.myplaylist.entity.Song songEntity;
        com.tienlx.myplaylist.jaxb.song.Song songJaxb;

        songDao = new SongDAO();
        Song found = songDao.get(id);
        if (found == null) {
            songEntity = new Song(id, title, AccentRemover.removeAccent(title), artist, AccentRemover.removeAccent(artist), viewLong, category);
            songDao.save(songEntity);
        } else {
            return;
        }
        albumArt = imageSrc + testSong.get("AlbumArt");
        lyrics = getLyric(testSong.get("URL"));
        data = testSong.get("Source");
        artistID = testSong.get("artistID");
        songJaxb = new com.tienlx.myplaylist.jaxb.song.Song();

        songJaxb.setAlbumArt(albumArt);
        songJaxb.setLyrics(lyrics);
        songJaxb.setSource(data);
        songJaxb.setTitle(title);
        songJaxb.setArtist(artist);
        songJaxb.setArtistID(artistID);

        marshallXML(getBasePath() + "song" + File.separator + id + ".xml", songJaxb);
        if (artistID.contains(",")) {
            String single = artistID.substring(0, artistID.indexOf(",") - 1);
            processArtist(single);
        } else {
            processArtist(artistID);
        }


    }

    private String getLyric(String URL) throws UnsupportedEncodingException {
        try {
            URL = URL.replace("mp3.zing", "m.mp3.zing");
            Document doc = Jsoup.connect(URL).get();
            Elements lyricContainer = doc.select(".lyric-song");
            if (lyricContainer == null || lyricContainer.size() == 0) {
                return null;
            }
            Element lyricEle = lyricContainer.get(0).select("div").get(0);
            return lyricEle.html();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public void fullProcess(String URL) {
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements wraperPage = doc.select(".wrapper-page a");
            Elements nav = doc.select("nav a");
            wraperPage.addAll(nav);
            Set<String> hs = new HashSet<String>();
            for (Element element : wraperPage) {
                String url = element.attr("href");
                hs.add(url);
            }
            for (String url : hs) {
                if (url != null && url.lastIndexOf(URL) >= 0 && url.lastIndexOf("/bai-hat/") < 0) {
                    processPage(url);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
