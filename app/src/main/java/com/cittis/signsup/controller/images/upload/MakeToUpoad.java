package com.cittis.signsup.controller.images.upload;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import com.cittis.signsup.actions.EndPoints;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MakeToUpoad {

    private static final String TAG = "Up-I";
    private Context mainActivity;
    private String selectedFilePath;

    private String serverUrl = "";

    public MakeToUpoad(Context mainActivity) {
        this.mainActivity = mainActivity;
        this.serverUrl = EndPoints.SERVER_URL;
        Log.e("URL", this.serverUrl);
    }

    private void uploadImagenMain() {
        //on upload button Click
        if (selectedFilePath != null) {
            //dialog = ProgressDialog.show(mainActivity,"","Uploading File...",true);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    //creating new thread to handle Http Operations
                    uploadFile(selectedFilePath);
                }
            }).start();
        } else {
            Toast.makeText(mainActivity, "Please choose a File First", Toast.LENGTH_SHORT).show();
        }
    }

    public void showFileChooser(Uri selectedFileUri) {
        selectedFilePath = FilePath.getPath(mainActivity, selectedFileUri);
        Log.i(TAG, "Selected File Path:" + selectedFilePath);

        if (selectedFilePath != null && !selectedFilePath.equals("")) {
            //tvFileName.setText(selectedFilePath);
            uploadImagenMain();
        } else {
            Toast.makeText(mainActivity, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
        }
    }


    //android upload file to server
    public int uploadFile(final String selectedFilePath) {

        int serverResponseCode = 0;

        HttpURLConnection connection;
        DataOutputStream dataOutputStream;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";


        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File selectedFile = new File(selectedFilePath);


        String[] parts = selectedFilePath.split("/");
        final String fileName = parts[parts.length - 1];

        if (!selectedFile.isFile()) {
            //dialog.dismiss();
            //tvFileName.setText("Source File Doesn't Exist: " + selectedFilePath);
            return 0;
        } else {
            try {
                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                URL url = new URL(serverUrl);
                connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);//Allow Inputs
                connection.setDoOutput(true);//Allow Outputs
                connection.setUseCaches(false);//Don't use a cached Copy
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("ENCTYPE", "multipart/form-data");
                connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                connection.setRequestProperty("uploaded_file", selectedFilePath);

                //creating new dataoutputstream
                dataOutputStream = new DataOutputStream(connection.getOutputStream());

                //writing bytes to data outputstream
                dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd);
                dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + selectedFilePath + "\"" + lineEnd);

                dataOutputStream.writeBytes(lineEnd);

                //returns no. of bytes present in fileInputStream
                bytesAvailable = fileInputStream.available();
                //selecting the buffer size as minimum of available bytes or 1 MB
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                //setting the buffer as byte array of size of bufferSize
                buffer = new byte[bufferSize];

                //reads bytes from FileInputStream(from 0th index of buffer to buffersize)
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                //loop repeats till bytesRead = -1, i.e., no bytes are left to read
                while (bytesRead > 0) {
                    //write the bytes read from inputstream
                    dataOutputStream.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                dataOutputStream.writeBytes(lineEnd);
                dataOutputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                serverResponseCode = connection.getResponseCode();
                String serverResponseMessage = connection.getResponseMessage();

                Log.i(TAG, "Server Response is: " + serverResponseMessage + ": " + serverResponseCode);

                //response code of 200 indicates the server status OK
                if (serverResponseCode == 200) {
                    /*mainActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //tvFileName.setText("File Upload completed.\n\n You can see the uploaded file here: \n\n" + SERVER_URL +"?see="ivAttachment + fileName);
                        }
                    });*/
                }

                //closing the input and output streams
                fileInputStream.close();
                dataOutputStream.flush();
                dataOutputStream.close();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
                /*mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mainActivity,"File Not Found",Toast.LENGTH_SHORT).show();
                    }
                });*/
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Toast.makeText(mainActivity, "URL error!", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(mainActivity, "Cannot Read/Write File!", Toast.LENGTH_SHORT).show();
            }
            //dialog.dismiss();
            return serverResponseCode;
        }

    }
}
