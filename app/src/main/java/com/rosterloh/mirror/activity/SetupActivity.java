package com.rosterloh.mirror.activity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.afollestad.assent.Assent;
import com.rosterloh.mirror.MirrorApplication;
import com.rosterloh.mirror.R;
import com.rosterloh.mirror.databinding.ActivitySetupBinding;
import com.rosterloh.mirror.models.Configuration;
import com.rosterloh.mirror.presenters.SetupPresenter;
import com.rosterloh.mirror.util.ASFObjectStore;
import com.rosterloh.mirror.views.SetupView;

import javax.inject.Inject;

public class SetupActivity extends AppCompatActivity implements SetupView,
            View.OnSystemUiVisibilityChangeListener, CompoundButton.OnCheckedChangeListener {

    private ActivitySetupBinding binding;

    @Inject
    SetupPresenter presenter;

    @Inject
    ASFObjectStore<Configuration> objectStore;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup);
        ((MirrorApplication) getApplication()).createSetupComponent(this).inject(this);
        Assent.setActivity(this, this);

        if (!Assent.isPermissionGranted(Assent.READ_CALENDAR)) {
            Assent.requestPermissions(result -> {
                // Permission granted or denied
                if (!result.allPermissionsGranted()) {
                    Toast.makeText(SetupActivity.this, getString(R.string.no_permission_for_calendar), Toast.LENGTH_SHORT).show();
                }
            }, 1, Assent.READ_CALENDAR);
        }

        binding.cbVoiceCommands.setOnCheckedChangeListener(this);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        decorView.setOnSystemUiVisibilityChangeListener(this);
    }

    public void launch(View v) {
        presenter.validate(
                binding.etLocation.getText().toString(),
                binding.etSubreddit.getText().toString(),
                binding.etPollingDelay.getText().toString(),
                binding.etServerAddress.getText().toString(),
                binding.etServerPort.getText().toString(),
                binding.cbVoiceCommands.isChecked());
    }

    @Override
    public void showLoading() {
        progressDialog = ProgressDialog.show(SetupActivity.this, "",
                getString(R.string.validating), true);
    }

    @Override
    public void hideLoading() {
        if (null != progressDialog && progressDialog.isShowing()) progressDialog.hide();
    }

    @Override
    public void navigateToMainActivity(Configuration configuration) {

        Intent intent = new Intent(this, MainActivity.class);
        objectStore.setObject(configuration);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSystemUI();
        Assent.setActivity(this, this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing())
            Assent.setActivity(this, null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Assent.handleResult(permissions, grantResults);
    }

    @Override
    public void onSystemUiVisibilityChange(int visibility) {
        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
            hideSystemUI();
        }
    }

    @Override
    public void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    @SuppressWarnings("all")
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.cb_voice_commands) {
            if (isChecked) {
                if (!Assent.isPermissionGranted(Assent.RECORD_AUDIO)) {
                    Assent.requestPermissions(result -> {
                        // Permission granted or denied
                        if (!result.allPermissionsGranted()) {
                            Toast.makeText(SetupActivity.this, getString(R.string.no_permission_for_voice), Toast.LENGTH_SHORT).show();
                            binding.cbVoiceCommands.setChecked(false);
                        }
                    }, 2, Assent.RECORD_AUDIO);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((MirrorApplication) getApplication()).releaseSetupComponent();
    }
}
