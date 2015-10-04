package org.ananasit.arzymo.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Path;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import org.ananasit.arzymo.AppController;
import org.ananasit.arzymo.model.Category;
import org.ananasit.arzymo.model.User;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;

public class ApiHelper {

    public static final String TAG = "[API]";
    public static final String API_KEY = "7dbe69719ab6a99e777f4a1948b6c5b82169c40c";
    public static final String ARZYMO_URL = "http://ananasit.org";
    public static final String CODE_URL = ARZYMO_URL + "/api/user/registration";
    public static final String CATEGORIES_URL = ARZYMO_URL + "/mobylive/v1/categories";
    public static final String POST_URL = ARZYMO_URL + "/mobylive/v1/posts";
    public static final String CATEGORY_POSTS_URL = ARZYMO_URL + "/mobylive/v1/posts/category";
    public static final String SUBCATEGORY_POSTS_URL = ARZYMO_URL + "/mobylive/v1/posts/subcategory";
    public static final String MEDIA_URL = ARZYMO_URL + "/mobylive/media";
    public static final String IMAGES_URL = ARZYMO_URL + "/mobylive/v1/images";

    public JSONObject getCode(String phone) throws ApiException, IOException,
            JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("sms_code", "");
        jsonObject.put("api_key", API_KEY);

        Log.i(TAG, "Sending request to: " + CODE_URL);
        //String response = POST(CODE_URL, jsonObject); //for http request
        String response = requestPost(CODE_URL, jsonObject, false); //for https request
        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public  JSONObject getToken(String phone, String code)
            throws ApiException, IOException, JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("sms_code", code);
        jsonObject.put("api_key", API_KEY);

        Log.i(TAG, "Sending request to: " + CODE_URL);
        String response = requestPost(CODE_URL, jsonObject, false);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public JSONObject getCategories()
            throws ApiException, IOException, JSONException
    {

        Log.i(TAG, "Sending request to: " + CATEGORIES_URL);
        String response = requestGet(CATEGORIES_URL);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public JSONObject sendPost(JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + POST_URL);
        String response = requestPost(POST_URL, jsonObject, true);
        //HttpResponse response = multipart_request(POST_URL);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public JSONObject sendHitcount(String url)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        String response = requestGet(url);
        Log.i(TAG, "Response: " + response);

        return new JSONObject(response);
    }

    public JSONObject editPost(String url, JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        String response = requestPut(url, jsonObject, true);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public JSONObject editProfile(String url, JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        String response = requestPut(url, jsonObject, true);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public JSONObject sendImage(String url, String image_path, boolean mode)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Image path : " + image_path);

        Log.i(TAG, "Sending request to: " + url);
        String response = multipart_request(url, image_path, mode);

        Log.i(TAG, "Response: " + response);
        return new JSONObject(response);
    }

    public static class ApiException extends Exception {

        public ApiException(String detailMessage) {
            super(detailMessage);
        }
    }

    public String requestPost(String _url, JSONObject json, boolean token_auth)
            throws IOException, IllegalStateException,
            JSONException {

        String result = "";
        HttpURLConnection conn = null;
        try {
        User user = AppController.getInstance().getUser();
        String message = json.toString();

        //constants
        URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();
        //conn.setReadTimeout( 10000 /*milliseconds*/ );
        //conn.setConnectTimeout( 15000 /* milliseconds */ );
        conn.setRequestMethod("POST");
            conn.setUseCaches(false);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setFixedLengthStreamingMode(message.getBytes().length);

        //make some HTTP header nicety
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty ("Content-type", "application/json; charset=UTF-8");
        if(token_auth)
            conn.setRequestProperty("Authorization", user.getClient_key());

            //open
            conn.connect();//needed?
            //setup send
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            os.close();

            //response
        InputStream in = new BufferedInputStream(conn.getInputStream());
            result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            in.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
            finally {
            if(conn != null)
            conn.disconnect();
            }

        return result;
    }

    public String requestGet(String _url)
            throws IOException, IllegalStateException,
            JSONException {

        String result = "";
        HttpURLConnection conn = null;
        try {
            //constants
            URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            //conn.setUseCaches(false);

            //make some HTTP header nicety
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json; charset=UTF-8");
            //conn.setRequestProperty("User-Agent", USER_AGENT);
            //open
            conn.connect();//needed?

            //response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            in.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(conn != null)
                conn.disconnect();
        }

        return result;

    }


    public String requestPut(String _url, JSONObject json, boolean token_auth)
            throws IOException, IllegalStateException,
            JSONException {

        String result = "";
        HttpURLConnection conn = null;

        try {
            User user = AppController.getInstance().getUser();
            String message = json.toString();

            //constants
            URL url = new URL(_url);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            //make some HTTP header nicety
            //conn.setRequestProperty("Accept", "application/json");
            //conn.setRequestProperty ("Content-type", "application/json; charset=UTF-8");
            if(token_auth)
                conn.setRequestProperty("Authorization", user.getClient_key());

            //open
            conn.connect();//needed?
            //setup send
            OutputStream os = new BufferedOutputStream(conn.getOutputStream());
            os.write(message.getBytes());
            //clean up
            os.flush();
            os.close();

            //response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            in.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(conn != null)
                conn.disconnect();
        }

        return result;
    }

    public String multipart_request(String _url, String path, boolean mode) {

        String attachmentName = "bitmap";
        String attachmentFileName = "bitmap.bmp";
        String crlf = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        //mode = true -> post
        //mode = false -> put

        String result = "";
        HttpURLConnection httpUrlConnection = null;

        try
        {
            User user = AppController.getInstance().getUser();
            File file = new File(path);

            //Setup the request:
            URL url = new URL(_url);
            httpUrlConnection = (HttpURLConnection) url.openConnection();
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setDoOutput(true);

            httpUrlConnection.setRequestMethod("POST");
            httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
            httpUrlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
            httpUrlConnection.setRequestProperty("Authorization", user.getClient_key());

            //Start content wrapper:
        DataOutputStream dataOS = new DataOutputStream(httpUrlConnection.getOutputStream());
            dataOS.writeBytes(twoHyphens + boundary + crlf);
            dataOS.writeBytes("Content-Disposition: form-data; name=\"" + attachmentName + "\";filename=\"" + attachmentFileName + "\"" + crlf);
            dataOS.writeBytes(crlf);

            // for bitmap
           //Convert Bitmap to ByteBuffer
           //I want to send only 8 bit black & white bitmaps
            /*
            byte[] pixels = new byte[bitmap.getWidth() * bitmap.getHeight()];
            for (int i = 0; i < bitmap.getWidth(); ++i) {
                for (int j = 0; j < bitmap.getHeight(); ++j) {
                    //we're interested only in the MSB of the first byte,
                    //since the other 3 bytes are identical for B&W images
                    pixels[i + j] = (byte) ((bitmap.getPixel(i, j) & 0x80) >> 7);
                }
            }
            dataOS.write(pixels);
            */
            //for bin
            byte[] file_bytes =  org.apache.commons.io.FileUtils.readFileToByteArray(file);
            dataOS.write(file_bytes);

            //End content wrapper:
            dataOS.writeBytes(crlf);
            dataOS.writeBytes(twoHyphens + boundary + twoHyphens + crlf);
            //Flush output buffer:
            dataOS.flush();
            dataOS.close();

            //Get response:
            //response
            InputStream in = new BufferedInputStream(httpUrlConnection.getInputStream());
            result = org.apache.commons.io.IOUtils.toString(in, "UTF-8");
            in.close();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            if(httpUrlConnection != null)
                httpUrlConnection.disconnect();
        }

        return result;
    }

    //2-nd version
    public static void multipart_request2(String targetURL, File file, String username, String password) throws Exception {

        String BOUNDRY = "==================================";
        HttpURLConnection conn = null;

        try {

            // These strings are sent in the request body. They provide information about the file being uploaded
            String contentDisposition = "Content-Disposition: form-data; name=\"userfile\"; filename=\"" + file.getName() + "\"";
            String contentType = "Content-Type: application/octet-stream";

            // This is the standard format for a multipart request
            StringBuffer requestBody = new StringBuffer();
            requestBody.append("--");
            requestBody.append(BOUNDRY);
            requestBody.append('\n');
            requestBody.append(contentDisposition);
            requestBody.append('\n');
            requestBody.append(contentType);
            requestBody.append('\n');
            requestBody.append('\n');
            //requestBody.append(new String(Util.getBytesFromFile(file)));
            requestBody.append("--");
            requestBody.append(BOUNDRY);
            requestBody.append("--");

            // Make a connect to the server
            URL url = new URL(targetURL);
            conn = (HttpURLConnection) url.openConnection();

            // Put the authentication details in the request
            if (username != null) {
                String usernamePassword = username + ":" + password;
                //String encodedUsernamePassword = Base64.encodeBytes(usernamePassword.getBytes());
                //conn.setRequestProperty ("Authorization", "Basic " + encodedUsernamePassword);
            }

            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDRY);

            // Send the body
            DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());
            dataOS.writeBytes(requestBody.toString());
            dataOS.flush();
            dataOS.close();

            // Ensure we got the HTTP 200 response code
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                throw new Exception(String.format("Received the response code %d from the URL %s", responseCode, url));
            }

            // Read the response
            InputStream is = conn.getInputStream();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] bytes = new byte[1024];
            int bytesRead;
            while((bytesRead = is.read(bytes)) != -1) {
                baos.write(bytes, 0, bytesRead);
            }
            byte[] bytesReceived = baos.toByteArray();
            baos.close();

            is.close();
            String response = new String(bytesReceived);

            // TODO: Do something here to handle the 'response' string

        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

    }

    public String responseText(String status)
    {
        if(status.equals("ACTIVATION_CODE_SENT"))
            return "Вам отправлен SMS с Вашим кодом.";
        if(status.equals("CODE_IS_USED"))
            return "Активация c этим кодом уже производилась.";
        if(status.equals("WRONG_ACTIVATION_CODE"))
            return "Неверный код активации.";
        if(status.equals("WRONG_API_KEY"))
            return "Неверный ключ API.";
        if(status.equals("ACTIVATION_PERIOD_EXPIRED"))
            return "Истек период активации.";
        if(status.equals("LOGIN_ERROR"))
            return "При входе возникла ошибка.";
        if(status.equals("USER_ALREADY_EXISTS"))
            return "Пользователь с таким номером телефона уже зарегистрирован.";
        if(status.equals("SEND_MESSAGE_ERROR"))
            return "Ошибка при попытке отправки сообщения.";
        if(status.equals("ACCOUNT_ACTIVATED"))
            return "Ваш аккаунт был активирован. Спасибо, за регистрацию.";

        return "";
    }

    public static String getCategoryPostsUrl(int page)
    {
        if(GlobalVar.Category != null && GlobalVar.Category.getSubcats() == null)
        {
            if(GlobalVar.Category.getIdParent().equals(""))
            {
                return CATEGORY_POSTS_URL + "/" + GlobalVar.Category.getId() + "/" + page;
            }
            else
            {
                return SUBCATEGORY_POSTS_URL + "/" + GlobalVar.Category.getId() + "/" + page;
            }
        }
        return "";
    }

    public static String getMyPostsUrl(String user_id, int page)
    {
        if(!user_id.equals(""))
        {
            return POST_URL + "/user/" + user_id + "/" + page;
        }
        return "";
    }

    public static Category getCategoryByIds(String id_category, String id_subcategory)
    {
        Category category = null;
        try
        {
            for(Iterator<Category> i = GlobalVar._categories.iterator(); i.hasNext(); ) {
                Category item = i.next();

                if(!item.getIdParent().equals(""))
                {
                    if(item.getIdParent().equals(id_category) && item.getId().equals(id_subcategory))
                    {
                        category = item;
                    }
                }
                else
                {
                    if(item.getId().equals(id_category))
                    {
                        category = item;
                    }
                }
            }
        }
        catch (NullPointerException ex)
        {
            ex.printStackTrace();
        }

        return category;
    }

    public static boolean isConnected(Context context)
    {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}