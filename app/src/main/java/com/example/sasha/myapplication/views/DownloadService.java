package com.example.sasha.myapplication.views;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import com.example.sasha.myapplication.R;
import com.example.sasha.myapplication.database.GeoPoint;
import com.example.sasha.myapplication.database.Guide;
import com.example.sasha.myapplication.database.HelperFactory;
import com.example.sasha.myapplication.mega.Mega;

import org.json.JSONException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by sasha on 3/15/15.
 */
public class DownloadService extends IntentService {
    private static final String SERVICE_TAG = "DOWNLOAD SERVICE";
    private String dataFolder;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    private NotificationManager mNotifyManager;
    private int id;

    public DownloadService() {
        super("UMapCashLoader");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataFolder = getString(R.string.data_cash);
        Log.d(SERVICE_TAG, "onCreate");
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        Guide city = (Guide) intent.getSerializableExtra(DetailCityInfoActivity.SER_KEY);
        Notification.Builder mBuilder = new Notification.Builder(getApplicationContext());
        mBuilder.setContentTitle("Picture Download")
                .setContentText("Download in progress")
                .setSmallIcon(R.drawable.outline_star_24)
                .setOngoing(true)
                .setProgress(0, 0, true);
        id++;
        mNotifyManager.notify(id, mBuilder.build());

        Log.d(MainActivity.LOG_TAG, "MEGA LINK " + city.getMapCash());

        //download maps cash from MEGA server
//        try {
//            downloadMegaFiles(city);
//        } catch (NoSuchPaddingException e) {
//            e.printStackTrace();
//        } catch (InvalidAlgorithmParameterException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        } catch (BadPaddingException e) {
//            e.printStackTrace();
//        } catch (IllegalBlockSizeException e) {
//            e.printStackTrace();
//        } catch (InvalidKeyException e) {
//            e.printStackTrace();
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
        RestTemplate restTemplate = new RestTemplate();

        try {
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            GeoPoint[] geoPoints = restTemplate.getForObject(DownloadListFragment.url + "points", GeoPoint[].class);
            Log.d(MainActivity.LOG_TAG, Arrays.toString(geoPoints));
            //download data structure
            for (GeoPoint point : geoPoints) {
                city.addPoint(point);
            }
            HelperFactory.getHelper().getGuideDAO().create(city);
            city.installed = true;

            //save data structure in database

        } catch (SQLException e) {
            e.printStackTrace();

            return;
        }
        mBuilder.setContentText("Download complete")
                .setProgress(0, 0, false)
                .setOngoing(false);
        mNotifyManager.notify(id, mBuilder.build());

        Log.d(SERVICE_TAG, "FNISH id " + city.getId());
        Intent intent1 = new Intent(DetailCityInfoActivity.BROADCAST_ACTION);
        intent.putExtra(DetailCityInfoActivity.FINISH, DetailCityInfoActivity.STATUS_FINISH);
        sendBroadcast(intent1);

    }

    private void downloadMegaFiles(Guide city) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, JSONException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, PackageManager.NameNotFoundException {
        Mega mega = new Mega();
        File folder = new File(getString(R.string.map_cash_path));
        if (!folder.exists()) folder.mkdir();
        mega.download(city.getMapCash(), getString(R.string.map_cash_path));
        String dirPath = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir;
        String name = mega.download(city.getDataCash(), dirPath);
        File zipFile = new File(dirPath + "/" + name);
        File unZipFolder = new File(dirPath + "/" + dataFolder);
        unZip(zipFile, unZipFolder);
        zipFile.delete();
    }

    private void unZip(File zipFile, File dest) throws FileNotFoundException {
        InputStream stream = new FileInputStream(zipFile);
        byte[] buffer = new byte[1024];

        try {

            //create output directory is not exists
            File folder = dest;
            if (!folder.exists()) {
                folder.mkdir();
            }

            //get the zip file content
            ZipInputStream zis =
                    new ZipInputStream(stream);
            //get the zipped file list entry
            ZipEntry ze = zis.getNextEntry();

            while (ze != null) {

                String fileName = ze.getName();
                File newFile = new File(dest + File.separator + fileName);

                System.out.println("file unzip : " + newFile.getAbsoluteFile());

                //create all non exists folders
                //else you will hit FileNotFoundException for compressed folder

                if (ze.isDirectory()) {
                    new File(newFile.getParent()).mkdirs();
                } else {
                    FileOutputStream fos = null;

                    new File(newFile.getParent()).mkdirs();

                    fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }

                ze = zis.getNextEntry();
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(SERVICE_TAG, "onDestroy");
    }
}
