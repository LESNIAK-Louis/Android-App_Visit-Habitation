package com.example.projetdevmobile.tools;

import static com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation.getOrientation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.example.projetdevmobile.R;
import com.example.projetdevmobile.projetdevmobile.Access;
import com.example.projetdevmobile.projetdevmobile.Enumeration.Orientation;
import com.example.projetdevmobile.projetdevmobile.Habitation;
import com.example.projetdevmobile.projetdevmobile.HabitationManager;
import com.example.projetdevmobile.projetdevmobile.Photo;
import com.example.projetdevmobile.projetdevmobile.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class Static {

    private static String FILENAME = "save.json";

    /* JSON SAVE */

    public static  void saveJson(Context ctx) {
        HabitationManager hm = HabitationManager.getInstance();
        try {
            JSONObject jsonObject = hm.toJson();
            FileOutputStream fos = ctx.openFileOutput(FILENAME, ctx.MODE_PRIVATE);
            if (jsonObject != null) {
                fos.write(jsonObject.toString().getBytes());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static boolean openJson(Context ctx){
        HabitationManager hm = HabitationManager.getInstance();

        FileInputStream fis = null;
        try { fis = ctx.openFileInput(FILENAME);} catch (FileNotFoundException e) {e.printStackTrace(); return false;}
        if (fis != null) {
            String json = getFileContent(fis);
            try {

                HashMap<Photo, JSONArray> accessHash = new HashMap<>();

                // Recreate Habitation, Rooms, Photos
                JSONObject save = new JSONObject(json);

                int nbHabitations = save.getInt("nbHabitations");
                if(nbHabitations > 0) {
                    JSONArray arrayHabitations = save.getJSONArray("habitations");
                    for (int i = 0; i < arrayHabitations.length(); i++) {
                        JSONObject jsonHabitation = (JSONObject) arrayHabitations.get(i);

                        String HabName = (String) jsonHabitation.get("name");
                        Habitation habitation = new Habitation(HabName);

                        int nbRooms = jsonHabitation.getInt("nbRooms");
                        if(nbRooms > 0) {
                            JSONArray arrayRooms = jsonHabitation.getJSONArray("rooms");
                            for (int j = 0; j < arrayRooms.length(); j++) {
                                JSONObject jsonRoom = (JSONObject) arrayRooms.get(j);

                                String RoomName = (String) jsonRoom.get("name");
                                Room room = new Room(RoomName, habitation.getName());

                                habitation.addRoom(room);

                                int nbPhotos = jsonRoom.getInt("nbPhotos");
                                if (nbPhotos > 0) {
                                    JSONArray arrayPhoto = jsonRoom.getJSONArray("photos");
                                    for (int k = 0; k < arrayPhoto.length(); k++) {
                                        JSONObject jsonPhoto = (JSONObject) arrayPhoto.get(k);

                                        String PhotoOrientation = (String) jsonPhoto.get("orientation");
                                        Orientation orientation = getOrientation(PhotoOrientation);
                                        String PhotoImgName = (String) jsonPhoto.get("imgName");

                                        Photo photo;
                                        if (PhotoImgName != "null") {
                                            Uri uri = Uri.parse(PhotoImgName);
                                            photo = new Photo(orientation, uri);
                                        } else
                                            photo = new Photo(orientation, null);

                                        room.setPhoto(photo);

                                        int nbAccess = jsonPhoto.getInt("nbAccess");
                                        if(nbAccess > 0) {
                                            JSONArray arrayAccess = jsonPhoto.getJSONArray("access");
                                            accessHash.put(photo, arrayAccess);
                                        }
                                    }
                                }
                            }

                            // Add access to photos
                            for (Photo photo : accessHash.keySet()) {
                                JSONArray arrayAccess = accessHash.get(photo);
                                for (int j = 0; j < arrayAccess.length(); j++) {
                                    JSONObject jsonAccess = (JSONObject) arrayAccess.get(j);

                                    Room room = (Room) habitation.getRoom((String) jsonAccess.get("room"));
                                    Rect rect = new Rect((int) jsonAccess.get("rectL"), (int) jsonAccess.get("rectT"), (int) jsonAccess.get("rectR"), (int) jsonAccess.get("rectB"));
                                    Access access = new Access(room, rect);
                                    photo.addAccess(access);
                                }
                            }

                        }

                        // Setting up the entrance
                        String HabRoomEntrance = (String) jsonHabitation.get("roomEntrance");
                        if (HabRoomEntrance != "null")
                            habitation.setRoomEntrance((Room) habitation.getRoom(HabRoomEntrance));
                        else
                            habitation.setRoomEntrance(null);

                        // Add habitation to the manager
                        hm.addHabitation(habitation);
                    }
                }



            } catch (JSONException e) {e.printStackTrace();}
            Log.i("testJSON", json);
        }
        else Log.i("testJSON", "pbm ouverture");
        return true;
    }

    /**
     * Get file content to String
     * @param fis
     * @return String of file content
     */
    public static String getFileContent(FileInputStream fis) {
        StringBuilder content = new StringBuilder();
        Reader r = null;
        try {
            r = new InputStreamReader(fis, "UTF-8");
            int ch = r.read();
            while(ch >= 0) {
                content.append((char)ch);
                ch = r.read();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content.toString();
    }





    /* Pictures */

    public static Bitmap handleSamplingAndRotationBitmap(Context context, Uri selectedImage) throws IOException {
        int MAX_HEIGHT = 1024;
        int MAX_WIDTH = 1024;

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream imageStream = context.getContentResolver().openInputStream(selectedImage);
        BitmapFactory.decodeStream(imageStream, null, options);
        imageStream.close();

        options.inSampleSize = calculateInSampleSize(options, MAX_WIDTH, MAX_HEIGHT);

        options.inJustDecodeBounds = false;
        imageStream = context.getContentResolver().openInputStream(selectedImage);
        Bitmap img = BitmapFactory.decodeStream(imageStream, null, options);

        img = rotateImageIfRequired(context, img, selectedImage);
        return img;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;


            final float totalPixels = width * height;
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = context.getContentResolver().openInputStream(selectedImage);
        File f = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), selectedImage.getLastPathSegment());
        ExifInterface ei = new ExifInterface(f.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }
}
