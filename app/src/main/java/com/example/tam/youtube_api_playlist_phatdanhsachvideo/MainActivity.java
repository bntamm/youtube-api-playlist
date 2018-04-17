package com.example.tam.youtube_api_playlist_phatdanhsachvideo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lvVideo;
    ArrayList<VideoYoutube> arrayVideo;
    VideoYoutubeAdapter adapter;

    public static String API_KEY = "AIzaSyD-oPYLqqtJym4SPW74JBT8QDEI7uHjjkM";
    String ID_PLAYLIST = "PLHg9cGx1zPXjpC0HGol1O1Wd0-ttq0B01";

 String url = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&playlistId="+ID_PLAYLIST+"&key="+API_KEY+"&maxResults=50";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        lvVideo = (ListView) findViewById(R.id.listviewVideo);
        arrayVideo = new ArrayList<>();
        adapter = new VideoYoutubeAdapter(this , R.layout.row_video_youtube , arrayVideo);
        lvVideo.setAdapter(adapter);


        GetJsonYoutube(url);

        BatSuKienKhiClickPlayVideo();



    }

    private void GetJsonYoutube(String url){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonItems = response.getJSONArray("items"); //mảng items nằm trong 1 JSONobject (response)
                            String title = " "; String url = " "; String idVideo = " ";

                            for(int i = 0 ; i < jsonItems.length() ; i++){
                                JSONObject jsonItem = jsonItems.getJSONObject(i); //Trong mảng items có nhiều JSONobject con
                                JSONObject jsonSnippet = jsonItem.getJSONObject("snippet");
                                //trong JSONobject con đó có object khác là snippet

                                //--------------------------------------------Lấy Title và hình Đại diện của Video
                                //Lấy title của video
                                title = jsonSnippet.getString("title");

                                //Lấy hình đại diện của video, 1 cái JsonObject "thumbnails" nằm trong JSONobject "Snippet"
                                JSONObject jsonThumbnail = jsonSnippet.getJSONObject("thumbnails");

                                //Lấy link và chất lượng hình ảnh , Trong jsonObject "thumbnails" có 1 jsonObject "medium"
                                JSONObject jsonMedium = jsonThumbnail.getJSONObject("medium");

                                //Lấy link hình ảnh "url" nằm trong Medium
                                url = jsonMedium.getString("url");

                                //---------------------------------------------Lấy ID của video
                                //JsonObject "resourceId" nằm trong JsonObject "Snippet"
                                JSONObject jsonResourceID = jsonSnippet.getJSONObject("resourceId");

                                //Lấy VideoID trong jsonObject "resourceId"
                                idVideo = jsonResourceID.getString("videoId");

                                arrayVideo.add(new VideoYoutube(title , url , idVideo));
                            }

                            adapter.notifyDataSetChanged();

                        } catch (JSONException e) {
                            e.printStackTrace();}}
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Lỗi!!", Toast.LENGTH_SHORT).show();
                    }
                });
        requestQueue.add(jsonObjectRequest);
    }

    private void BatSuKienKhiClickPlayVideo(){

        lvVideo.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                //Tạo code Play 1 video giống bài 180 & 181
                //Tạo 1 Activity (PlayVideoActivity) để khi click video thì chuyển sang màn hình khác, sử dụng Intent để chuyển
                //Id qua màn hình PlayVideoActivity
                Intent intent = new Intent(MainActivity.this , PlayVideoActivity.class);
                intent.putExtra("idVideoYoutube" , arrayVideo.get(i).getIdVideo());
                startActivity(intent);


            }
        });
    }


}
