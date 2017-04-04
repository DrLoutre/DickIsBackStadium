package com.example.gecko.smartstadium.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.example.gecko.smartstadium.R;


/**
 * This activity handle the decoding of the QRcode
 *
 * @author Quentin Jacquemotte
 */
public class QRCodeActivity extends Activity implements QRCodeReaderView.OnQRCodeReadListener {

    Intent intent;
    private QRCodeReaderView qrCodeReaderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        qrCodeReaderView = (QRCodeReaderView) findViewById(R.id.qrdecoderview);
        qrCodeReaderView.setOnQRCodeReadListener(this);


        /**
         * Use this function to enable/disable decoding
         */
        qrCodeReaderView.setQRDecodingEnabled(true);

        /**
         * Use this function to change the autofocus interval (default is 5 secs)
         */
        qrCodeReaderView.setAutofocusInterval(2000L);

        /**
         * Use this function to enable/disable Torch
         */
        qrCodeReaderView.setTorchEnabled(true);

        /**
         *  Use this function to set front camera preview
         */
        qrCodeReaderView.setFrontCamera();

        /**
         * Use this function to set back camera preview
         */
        qrCodeReaderView.setBackCamera();

        Snackbar.make(findViewById(android.R.id.content), "Scannez un QR code...", Snackbar.LENGTH_LONG).show();

    }


    /**
     * Called when a QR is decoded
     *
     * @param text   the text encoded in QR
     * @param points points where QR control points are placed in View
     */
    public void onQRCodeRead(String text, PointF[] points) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", text);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    /**
     * Start the camera
     */
    @Override
    protected void onResume() {
        super.onResume();
        qrCodeReaderView.startCamera();
    }

    /**
     * Stop the camera
     */
    @Override
    protected void onPause() {
        super.onPause();
        qrCodeReaderView.stopCamera();
    }
}
