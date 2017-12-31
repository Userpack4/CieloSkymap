package sil.cielo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import sil.cielo.view.MyScreen;


public class CieloSkyMap extends Activity {

    private MyScreen skymap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainlayout);
        skymap = findViewById(R.id.idmayscreen);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        skymap.invalidate();

    }
}
