package org.ananasit.arzymo.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.ananasit.arzymo.AppController;
import org.ananasit.arzymo.model.Category;
import org.ananasit.arzymo.model.User;
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
import java.io.File;
import java.io.IOException;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Iterator;

public class ApiHelper {

    public static final String TAG = "[API]";
    public static final String API_KEY = "7dbe69719ab6a99e677f4a1948b6c5b82162c40c";
    public static final String ARZYMO_URL = "http://192.168.0.109";
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
        HttpResponse response = requestPost(CODE_URL, jsonObject, false); //for https request
        Log.i(TAG, "Response: " + response);
        String responseStr = responseToStr(response);
        Log.i(TAG, "ResponseStr: " + responseStr);
        return new JSONObject(responseStr);
    }

    public  JSONObject getToken(String phone, String code)
            throws ApiException, IOException, JSONException {

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        jsonObject.put("sms_code", code);
        jsonObject.put("api_key", API_KEY);

        Log.i(TAG, "Sending request to: " + CODE_URL);
        HttpResponse response = requestPost(CODE_URL, jsonObject, false);

        String responseStr = responseToStr(response);

        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public JSONObject getCategories()
            throws ApiException, IOException, JSONException
    {

        Log.i(TAG, "Sending request to: " + CATEGORIES_URL);
        HttpResponse response = requestGet(CATEGORIES_URL);

        String responseStr = responseToStr(response);

        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public JSONObject sendPost(JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + POST_URL);
        HttpResponse response = requestPost(POST_URL, jsonObject, true);
        //HttpResponse response = multipart_request(POST_URL);

        String responseStr = responseToStr(response);

        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public JSONObject sendHitcount(String url)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        HttpResponse response = requestGet(url);
        String responseStr = responseToStr(response);
        Log.i(TAG, "Response: " + responseStr);

        return new JSONObject(responseStr);
    }

    public JSONObject editPost(String url, JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        HttpResponse response = requestPut(url, jsonObject, true);

        String responseStr = responseToStr(response);
        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public JSONObject editProfile(String url, JSONObject jsonObject)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Sending request to: " + url);
        HttpResponse response = requestPut(url, jsonObject, true);

        String responseStr = responseToStr(response);
        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public JSONObject sendImage(String url, String image_path, boolean mode)
            throws ApiException, IOException, JSONException {

        Log.i(TAG, "Image path : " + image_path);

        Log.i(TAG, "Sending request to: " + url);
        HttpResponse response = multipart_request(url, image_path, mode);

        String responseStr = responseToStr(response);

        Log.i(TAG, "Response: " + responseStr);
        return new JSONObject(responseStr);
    }

    public static String responseToStr(HttpResponse response) throws IOException
    {
        //return EntityUtils.toString(response.getEntity());
        return EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
    }
    public static class ApiException extends Exception {

        public ApiException(String detailMessage) {
            super(detailMessage);
        }
    }

    public  HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(
                    SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public HttpResponse requestPost(String url, JSONObject json, boolean token_auth)
            throws IOException, IllegalStateException,
            JSONException {

        User user = AppController.getInstance().getUser();
        DefaultHttpClient client = (DefaultHttpClient) getNewHttpClient();

        HttpPost post = new HttpPost(url);
        StringEntity se = new StringEntity(json.toString(), HTTP.UTF_8);
        post.setEntity(se);
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        if(token_auth)
            post.setHeader("Authorization", user.getClient_key());

        HttpResponse response = client.execute(post);
        return response;
    }

    public HttpResponse requestPut(String url, JSONObject json, boolean token_auth)
            throws IOException, IllegalStateException,
            JSONException {

        User user = AppController.getInstance().getUser();
        DefaultHttpClient client = (DefaultHttpClient) getNewHttpClient();

        HttpPut putRequest = new HttpPut(url);
        StringEntity se = new StringEntity(json.toString(), HTTP.UTF_8);
        putRequest.setEntity(se);
        putRequest.setHeader("Accept", "application/json");
        putRequest.setHeader("Content-type", "application/json");
        if(token_auth)
            putRequest.setHeader("Authorization", user.getClient_key());

        HttpResponse response = client.execute(putRequest);
        return response;
    }

    public HttpResponse requestGet(String url)
            throws IOException, IllegalStateException,
            JSONException {

        DefaultHttpClient client = (DefaultHttpClient) getNewHttpClient();

        HttpGet get = new HttpGet(url);
        get.setHeader("Accept", "application/json");
        get.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(get);
        return response;
    }

    public HttpResponse multipart_request(String url, String path, boolean mode) {

        User user = AppController.getInstance().getUser();

        DefaultHttpClient client = (DefaultHttpClient) getNewHttpClient();
        try {
            FileBody bin = new FileBody(new File(path));
            //StringBody comment = new StringBody("A binary file of some kind", ContentType.TEXT_PLAIN);

            //for sending bitmap
            /*
            Bitmap bitmap = GlobalVar._bitmaps.get(0);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte[] data = bos.toByteArray();
            ByteArrayBody bab = new ByteArrayBody(data, "nokia.jpg");
            */
            //end bitmap
            HttpResponse response;
            if(mode)               // mode = true = Post
            {
                HttpPost httppost = new HttpPost(url);
                HttpEntity reqEntity = MultipartEntityBuilder.create()
                        .addPart("image", bin)
                                //.addPart("comment", comment)
                                //.addPart("original_image",bab)   //for sending bitmap
                        .build();

                httppost.setEntity(reqEntity);
                httppost.setHeader("Authorization", user.getClient_key());

                Log.d("executing request", httppost.getRequestLine().toString());

                response = client.execute(httppost);
            }
            else               // mode = false = Put
            {
                HttpPut httpput = new HttpPut(url);
                HttpEntity reqEntity = MultipartEntityBuilder.create()
                        .addPart("avatar_original_image", bin)
                                //.addPart("avatar_30", bin)
                        .build();

                httpput.setEntity(reqEntity);
                httpput.setHeader("Authorization", user.getClient_key());

                response = client.execute(httpput);
            }
            return response;
        }
        catch (Exception ex)
        {
            return null;
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