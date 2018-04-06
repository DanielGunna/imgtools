package com.gustavogoma.utils.imgtools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.view.View.GONE;

/**
 * @author GustavoHGAraujo
 */

public class ImageTools extends AsyncTask<Void, Void, Bitmap> {

    private static final String TAG = ImageTools.class.getSimpleName();

    /**
     * Decode the byte array to a Bitmap.
     * @param byteArray Bitmap byte array
     * @return Bitmap
     */
    public static Bitmap byteArray2Bitmap(byte[] byteArray){
        if (byteArray != null) {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        } else {
            return null;
        }
    }

    /**
     * Encode the Bitmap to a byte array.
     * @param bitmap Bitmap byte array
     * @return byte[] Encoded bitmap
     */
    public static byte[] bitmap2ByteArray(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

    /**
     * Downloads a image through its url.
     * @param imgUrl Image URL
     * @param onImageDownloadListener Listener called when the download is done.
     */
    public static void download(String imgUrl, OnImageDownloadListener onImageDownloadListener) {
        ImageTools imgTools = new ImageTools();
        imgTools.imgUrl = imgUrl;
        imgTools.l = onImageDownloadListener;

        imgTools.execute();
        Log.d(TAG, "download: IMG_URL: " + imgUrl);
    }

    /**
     * Set the image into ImageView and, if the ProgressBar isn't null, set its visibility as GONE.
     * @param imgView ImageView where the image will be shown
     * @param progressBar ProgressBar that, if not null, will be hidden
     * @param bitmap Image to be shown
     */
    public static void setImage(ImageView imgView, ProgressBar progressBar, Bitmap bitmap) {
        imgView.setImageBitmap(bitmap);
        imgView.setAlpha(1f);
        imgView.setBackground(null);
        imgView.setPadding(0, 0, 0, 0);
        if (progressBar != null) progressBar.setVisibility(GONE);
    }

    /**
     * Returns a squared image
     * @param bmp Image to be cropped
     * @return Bitmap Cropped image
     */
    private static Bitmap getSquareBitmap(Bitmap bmp) {
        if (bmp == null) return null;

        Bitmap output;
        if (bmp.getWidth() >= bmp.getHeight()){
            output = Bitmap.createBitmap(
                    bmp,
                    bmp.getWidth()/2 - bmp.getHeight()/2,
                    0,
                    bmp.getHeight(),
                    bmp.getHeight()
            );

        }else{
            output = Bitmap.createBitmap(
                    bmp,
                    0,
                    bmp.getHeight()/2 - bmp.getWidth()/2,
                    bmp.getWidth(),
                    bmp.getWidth()
            );
        }

        return output;
    }

    /**
     * Returns a circled image
     * @param bitmap Image to be cropped
     * @return Bitmap Cropped image
     */
    public static Bitmap getCircularBitmap(Bitmap bitmap){
        return getRoundedCornerBitmap(getSquareBitmap(bitmap), bitmap.getWidth());
    }

    /**
     * * Returns a rounded corner image
     * @param bitmap Image to be cropped
     * @param pixels Corner radius
     * @return Bitmap Cropped image
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Log.v(TAG, "getRoundedCornerBitmap");

        if (bitmap == null) return null;

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    private String imgUrl;
    private OnImageDownloadListener l;

    private ImageTools() {
        this.imgUrl = "";
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {
        Log.v(TAG, "doInBackground");
        if (imgUrl == null) return null;

        Bitmap bmp = null;
        try{
            URL url = new URL(imgUrl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            InputStream is = con.getInputStream();
            bmp = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            Log.e(TAG, "doInBackground", e);
            if (l != null) l.onImageDownloadFailed(e);
        }

        return bmp;
    }

    @Override
    protected void onPostExecute(Bitmap bmp) {
        Log.v(TAG, "onPostExecute");
        super.onPostExecute(bmp);

        if (l != null) l.onImageDownloaded(bmp);
    }

    public interface OnImageDownloadListener {
        void onImageDownloaded(Bitmap bitmap);
        void onImageDownloadFailed(Exception e);
    }
}
