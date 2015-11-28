package rail.mini_project.projectrail;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Scheduledata extends AsyncTask<Void, Void, List<String>> {
    InputStream is = null;
    String result;
    String url;
    List<String> data = new ArrayList<>();
    int l =0;

    public Scheduledata(String source_code, String destination_code) {
        this.url = "http://erail.in/rail/getTrains.aspx?Station_From=" + source_code +
                "&Station_To=" + destination_code + "&DataSource=0&Language=0";
        Log.e("Url  ",url);
    }

    @Override
    protected List<String> doInBackground(Void... params) {

            try {
                URL train_url = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) train_url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                int response = conn.getResponseCode();
                if(response == 200) {
                    is = conn.getInputStream();
                    try {
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(is, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        sb.append(reader.readLine() + "\n");
                        String line = "";
                        while ((line = reader.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        result = sb.toString();
                        Log.e("Value result", result);
                        Matcher matcher = Pattern.compile("(\\^[A-Za-z0-9 ]+\\~[A-Za-z 0-9]+\\~)+")
                                .matcher(result);
                        if (matcher.find()) {
                            while (matcher.find()) {

                                result = matcher.group().replaceAll("\\~", " ");
                                result = result.replaceAll("\\^", "");
                                String[] value = result.split(" ");
                                String train_num = value[0];
                                String train_name = "";

                                for (int i = 1; i < value.length; i++) {
                                    train_name = train_name + value[i] + " ";
                                }
                                Log.e("Train_Number", train_num);
                                Log.e("Train_Name", train_name);
                                data.add("Train Info: "+train_num+" "+train_name);
                            }
                        }
                        else{
                            data.add("Not");
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        data.add("Unsuccessful");
                    }


                }
                else{
                    data.add("Unsuccessful");

                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
                data.add("Unsuccessful");
            } catch (ProtocolException e) {
                e.printStackTrace();
                data.add("Unsuccessful");
            } catch (IOException e) {
                e.printStackTrace();
                data.add("Unsuccessful");
            }     finally
             {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        data.add("Unsuccessful");
                    }
                }
            }
        return data;

    }

}
