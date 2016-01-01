package org.horrgs.jgooglenews;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Horrgs on 12/30/2015.
 */
public class Site implements Article {
    public String url;
    private URLConnection urlConnection;

    public Site(String url) {
        this.url = url;
    }

    public Site() {
        super();
    }

    public URLConnection openURL(String url) {
        System.out.println("An attempt is being made to make a connection to: " + url);
        try {
            this.url = url;
            URLConnection urlConnection = new URL(url).openConnection();
            urlConnection.setUseCaches(false);
            urlConnection.setDefaultUseCaches(false);
            System.out.println("Connection has been established to: " + url);
            this.urlConnection = urlConnection;
            return urlConnection;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public URLConnection getUrlConnection() {
        return urlConnection;
    }

    public void setUrlConnection(URLConnection urlConnection) {
        this.urlConnection = urlConnection;
    }

    private JSONObject jsonObject = null;


    /**
     * @param args An optional argument, but when used "val" is required. An example would be when you make
     *             a Google Search it has a "?q=" in it, standing for query, this is what args represents.
     * @param val  Required when args is used, this would be the value following "?q=" so if you googled for
     *             "define Potus", it'd be "?q=define20%potus".
     */
    public void startJson(String args, String val) {
        try {
            if (args != null && val != null) {
                this.jsonObject = new JSONObject(getResponse(openURL(getURL(args.replace(" ", "%20") + val.replace(" ", "%20")))));
            } else {
                this.jsonObject = new JSONObject(getResponse(openURL(getURL(null))));
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private String getURL(String args) {
        if (args != null) return this.url + args;
        return this.url;
    }

    public void setURL(String url) {
        this.url = url;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }



    private String getResponse(URLConnection urlConnection) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public <T> T get(String value) {
        try {
            return (T) jsonObject.getJSONObject("responseData").getJSONArray("results").getJSONObject(getId()).get(value);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Article[] getArticles(String args, String val, int count) {
        Article[] articles = new Article[count];
        for(int x = 0; x < count; x++) {
            Site site = new Site(this.url);
            site.startJson(args, val);
            site.setId(x);
            articles[x] = site;
        }
        return articles;
    }

    int id;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this. id = id;
    }

    @Override
    public String getTitle() {
        return get("title").toString().replace("&#39;", "'").replace("</b>", "").replace("<b>", "");
    }

    @Override
    public String getPublishedDate() {
        return get("publishedDate");
    }

    @Override
    public String getPublisher() {
        return get("publisher");
    }

    @Override
    public String getLink() {
        return get("unescapedUrl");
    }
}
