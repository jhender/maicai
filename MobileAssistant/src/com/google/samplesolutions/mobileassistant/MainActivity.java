/* Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.samplesolutions.mobileassistant;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.samplesolutions.mobileassistant.checkinendpoint.Checkinendpoint;
import com.google.samplesolutions.mobileassistant.checkinendpoint.model.CheckIn;
import com.google.samplesolutions.mobileassistant.placeendpoint.Placeendpoint;
import com.google.samplesolutions.mobileassistant.placeendpoint.model.Place;
import com.google.samplesolutions.mobileassistant.placeendpoint.model.CollectionResponsePlace;

import android.app.Activity;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class MainActivity extends Activity {

  private ListView placesList;
  private List<Place> places = null;
    
  //Activity request codes
  private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
  public static final int MEDIA_TYPE_IMAGE = 1;

  // directory name to store captured images and videos
  private static final String IMAGE_DIRECTORY_NAME = "MaiCai";

  private Uri fileUri; // file url to store image/video

  private ImageView imgPreview;
  private Button btnCapturePicture;
  

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    placesList = (ListView) findViewById(R.id.PlacesList);

    new CheckInTask().execute();

    // start retrieving the list of nearby places
    new ListOfPlacesAsyncRetriever().execute();
    
    placesList.setOnItemClickListener(placesListClickListener);
    
    imgPreview = (ImageView) findViewById(R.id.imgPreview);
    btnCapturePicture = (Button) findViewById(R.id.TakePicture);
    /**
     * Capture image button click event
     */
    btnCapturePicture.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            // capture picture
            captureImage();
        }
    });
  }


  /**
   * AsyncTask for calling Mobile Assistant API for checking into a 
   * place (e.g., a store).
   */
  private class CheckInTask extends AsyncTask<Void, Void, Void> {

    /**
     * Calls appropriate CloudEndpoint to indicate that user checked into a place.
     *
     * @param params the place where the user is checking in.
     */
    @Override
    protected Void doInBackground(Void... params) {
      CheckIn checkin = new com.google.samplesolutions.mobileassistant.checkinendpoint.model.CheckIn();

      // Set the ID of the store where the user is.
      checkin.setPlaceId("StoreNo123");

      Checkinendpoint.Builder builder = new Checkinendpoint.Builder(
          AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);

      builder = CloudEndpointUtils.updateBuilder(builder);

      Checkinendpoint endpoint = builder.build();


      try {
        endpoint.insertCheckIn(checkin).execute();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

      return null;
    }
  }


  /**
   * AsyncTask for retrieving the list of places (e.g., stores) and updating the
   * corresponding results list.
   */
  private class ListOfPlacesAsyncRetriever extends AsyncTask<Void, Void, CollectionResponsePlace> {

    @Override
    protected CollectionResponsePlace doInBackground(Void... params) {


      Placeendpoint.Builder endpointBuilder = new Placeendpoint.Builder(
          AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
     
      endpointBuilder = CloudEndpointUtils.updateBuilder(endpointBuilder);


      CollectionResponsePlace result;

      Placeendpoint endpoint = endpointBuilder.build();

      try {
        result = endpoint.listPlace().execute();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        result = null;
      }
      return result;
    }

    @Override
    @SuppressWarnings("null")
    protected void onPostExecute(CollectionResponsePlace result) {
      ListAdapter placesListAdapter = createPlaceListAdapter(result.getItems());
      placesList.setAdapter(placesListAdapter);

      places = result.getItems();
    }
    
    private ListAdapter createPlaceListAdapter(List<Place> places) {
      final double kilometersInAMile = 1.60934;
      List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
      for (Place place : places) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("placeIcon", R.drawable.ic_launcher);
        map.put("placeName", place.getName());
        map.put("placeAddress", place.getAddress());
        String distance = "1.2";
        map.put("placeDistance", distance);
        data.add(map);
      }

      SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.place_item,
          new String[] {"placeIcon", "placeName", "placeAddress", "placeDistance"},
          new int[] {R.id.place_Icon, R.id.place_name, R.id.place_address, R.id.place_distance});

      return adapter;
    }
    
  }

  private OnItemClickListener placesListClickListener = new OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
      Place selectedPlace = places.get((int) arg3);

      new CheckInTask().execute();
      
      PlaceDetailsActivity.currentPlace = selectedPlace;
      Intent i = new Intent(MainActivity.this, PlaceDetailsActivity.class);
      startActivity(i);
      }
  };  
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }
  
  
  /**
   * Capturing Camera Image will launch camera app request image capture
   */
  private void captureImage() {
      Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

      intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

      // start the image capture Intent
      startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
  }

  /**
   * Here we store the file url as it will be null after returning from camera
   * app
   */
  @Override
  protected void onSaveInstanceState(Bundle outState) {
      super.onSaveInstanceState(outState);

      // save file url in bundle as it will be null on scren orientation
      // changes
      outState.putParcelable("file_uri", fileUri);
  }

  @Override
  protected void onRestoreInstanceState(Bundle savedInstanceState) {
      super.onRestoreInstanceState(savedInstanceState);

      // get the file url
      fileUri = savedInstanceState.getParcelable("file_uri");
  }
  
  /**
   * Receiving activity result method will be called after closing the camera
   * */
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // if the result is capturing Image
      if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
          if (resultCode == RESULT_OK) {
              // successfully captured the image
              // display it in image view
              previewCapturedImage();
          } else if (resultCode == RESULT_CANCELED) {
              // user cancelled Image capture
              Toast.makeText(getApplicationContext(),
                      "User cancelled image capture", Toast.LENGTH_SHORT)
                      .show();
          } else {
              // failed to capture image
              Toast.makeText(getApplicationContext(),
                      "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                      .show();
          }
      }       
  }
  
  /**
   * Display image from a path to ImageView
   */
  private void previewCapturedImage() {
      try {
          // hide video preview
          //videoPreview.setVisibility(View.GONE);

          imgPreview.setVisibility(View.VISIBLE);

          // bimatp factory
          BitmapFactory.Options options = new BitmapFactory.Options();

          // downsizing image as it throws OutOfMemory Exception for larger
          // images
          options.inSampleSize = 8;

          final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                  options);

          imgPreview.setImageBitmap(bitmap);
      } catch (NullPointerException e) {
          e.printStackTrace();
      }
  }
  
  
  
  
  /**
   * ------------ Helper Methods ---------------------- 
   * */

  /**
   * Creating file uri to store image/video
   */
  public Uri getOutputMediaFileUri(int type) {
      return Uri.fromFile(getOutputMediaFile(type));
  }

  /**
   * returning image / video
   */
  private static File getOutputMediaFile(int type) {

      // External sdcard location
      File mediaStorageDir = new File(
              Environment
                      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
              IMAGE_DIRECTORY_NAME);

      // Create the storage directory if it does not exist
      if (!mediaStorageDir.exists()) {
          if (!mediaStorageDir.mkdirs()) {
              Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                      + IMAGE_DIRECTORY_NAME + " directory");
              return null;
          }
      }

      // Create a media file name
      String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
              Locale.getDefault()).format(new Date(type));
      File mediaFile;
      if (type == MEDIA_TYPE_IMAGE) {
          mediaFile = new File(mediaStorageDir.getPath() + File.separator
                  + "IMG_" + timeStamp + ".jpg");
      } else {
          return null;
      }

      return mediaFile;
  }
  
  
}
