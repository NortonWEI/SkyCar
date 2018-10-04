package com.example.nortonwei.skycar;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.nortonwei.skycar.Customization.PaymentPopup;

import java.io.FileNotFoundException;

import me.shaohui.bottomdialog.BottomDialog;

public class LicenseTranslationActivity extends AppCompatActivity {
    private final static int FRONT_TAKE_REQUEST_CODE = 8001;
    private final static int FRONT_PICK_REQUEST_CODE = 8002;
    private final static int BACK_TAKE_REQUEST_CODE = 9001;
    private final static int BACK_PICK_REQUEST_CODE = 9002;
    private static final int TAKE_PERMISSIONS_REQUESTS_CODE = 1234;
    private static final int PICK_PERMISSIONS_REQUESTS_CODE = 1235;
    private ImageButton uploadFrontButton;
    private ImageButton uploadBackButton;
    private boolean isTakePermitted = false;
    private boolean isPickPermitted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license_translation);

        setUpActionBar();
        if (isServiceOK()) {
            setUpUIComponents();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setUpActionBar() {
        HomeActivity.setUpActionBar(this, getString(R.string.translate_licence));
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setUpUIComponents() {
        Button payButton = (Button) findViewById(R.id.pay_button);
        uploadFrontButton = (ImageButton) findViewById(R.id.upload_front_button);
        uploadBackButton = (ImageButton) findViewById(R.id.upload_back_button);

        payButton.setOnClickListener(view -> {
            PaymentPopup popup = new PaymentPopup(this);
            popup.setUp();
            popup.show();
        });

        uploadFrontButton.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);
                    Button takePhotoButton = (Button) v.findViewById(R.id.take_photo_button);
                    Button pickLibraryButton = (Button) v.findViewById(R.id.pick_library_button);

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    takePhotoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ContextCompat.checkSelfPermission(LicenseTranslationActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LicenseTranslationActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        TAKE_PERMISSIONS_REQUESTS_CODE);
                            } else {
                                bottomDialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, FRONT_TAKE_REQUEST_CODE);
                            }
                        }
                    });

                    pickLibraryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ContextCompat.checkSelfPermission(LicenseTranslationActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LicenseTranslationActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PICK_PERMISSIONS_REQUESTS_CODE);
                            } else {
                                bottomDialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, FRONT_PICK_REQUEST_CODE);
                            }
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_upload_license)
                    .show();
        });

        uploadBackButton.setOnClickListener(view -> {
            BottomDialog bottomDialog = BottomDialog.create(getSupportFragmentManager());

            bottomDialog.setViewListener(new BottomDialog.ViewListener() {
                @Override
                public void bindView(View v) {
                    ImageButton closeButton = (ImageButton) v.findViewById(R.id.cancel_button);
                    Button takePhotoButton = (Button) v.findViewById(R.id.take_photo_button);
                    Button pickLibraryButton = (Button) v.findViewById(R.id.pick_library_button);

                    closeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            bottomDialog.dismiss();
                        }
                    });

                    takePhotoButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ContextCompat.checkSelfPermission(LicenseTranslationActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LicenseTranslationActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        TAKE_PERMISSIONS_REQUESTS_CODE);
                            } else {
                                bottomDialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, BACK_TAKE_REQUEST_CODE);
                            }
                        }
                    });

                    pickLibraryButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (ContextCompat.checkSelfPermission(LicenseTranslationActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(LicenseTranslationActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        PICK_PERMISSIONS_REQUESTS_CODE);
                            } else {
                                bottomDialog.dismiss();
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                startActivityForResult(intent, BACK_PICK_REQUEST_CODE);
                            }
                        }
                    });
                }
            })
                    .setLayoutRes(R.layout.layout_upload_license)
                    .show();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FRONT_TAKE_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    uploadFrontButton.setImageBitmap(bitmap);
                } else {
                    uploadFrontButton.setImageDrawable(getResources().getDrawable(R.drawable.upload_image_button));
                }
                break;
            case FRONT_PICK_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        uploadFrontButton.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    uploadFrontButton.setImageDrawable(getResources().getDrawable(R.drawable.upload_image_button));
                }
                break;
            case BACK_TAKE_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    uploadBackButton.setImageBitmap(bitmap);
                } else {
                    uploadBackButton.setImageDrawable(getResources().getDrawable(R.drawable.upload_image_button));
                }
                break;
            case BACK_PICK_REQUEST_CODE:
                if (resultCode == RESULT_OK && data != null) {
                    Uri uri = data.getData();
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                        uploadBackButton.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    uploadBackButton.setImageDrawable(getResources().getDrawable(R.drawable.upload_image_button));
                }
                break;
            default:
                break;
        }
    }

    private boolean isServiceOK() {
        PackageManager packageManager = getPackageManager();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            Toast.makeText(this, "您的设备不支持相机及照片功能", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == TAKE_PERMISSIONS_REQUESTS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                isTakePermitted = true;
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, BACK_TAKE_REQUEST_CODE);
            } else {
                isTakePermitted = false;
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            }
        }


        if (requestCode == PICK_PERMISSIONS_REQUESTS_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                isPickPermitted = true;
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, FRONT_PICK_REQUEST_CODE);
            } else {
                isPickPermitted = false;
                Toast.makeText(this, "没有权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, LicenseTranslationActivity.class);
    }
}
