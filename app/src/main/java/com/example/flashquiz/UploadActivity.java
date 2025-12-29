package com.example.flashquiz;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class UploadActivity extends AppCompatActivity {

    public static ArrayList<Flashcard> flashcards = new ArrayList<>();

    private static final int PICK_PDF = 101;
    private static final int PICK_IMAGE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        EditText inputText = findViewById(R.id.inputText);
        Spinner difficultySpinner = findViewById(R.id.difficultySpinner);
        Button generateBtn = findViewById(R.id.generateBtn);

        Button pdfBtn = findViewById(R.id.pdfBtn);
        Button imageBtn = findViewById(R.id.imageBtn);
        Button stylusBtn = findViewById(R.id.stylusBtn);
        TextView selectedFileText = findViewById(R.id.selectedFileText);

        // PDF picker
        pdfBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("application/pdf");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            startActivityForResult(intent, PICK_PDF);
        });

        // Image picker
        imageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        // Stylus placeholder
        stylusBtn.setOnClickListener(v ->
                Toast.makeText(this, "Stylus input coming next", Toast.LENGTH_SHORT).show()
        );

        // Generate quiz from manual input
        generateBtn.setOnClickListener(v -> {
            flashcards.clear();

            String text = inputText.getText().toString().trim();
            String difficulty = difficultySpinner.getSelectedItem().toString();

            if (text.isEmpty()) {
                Toast.makeText(this, "Please enter content", Toast.LENGTH_SHORT).show();
                return;
            }

            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.contains(":")) {
                    String[] parts = line.split(":", 2);
                    if (parts.length == 2) {
                        flashcards.add(new Flashcard(
                                parts[0].trim(),
                                parts[1].trim(),
                                difficulty
                        ));
                    }
                }
            }

            if (flashcards.isEmpty()) {
                Toast.makeText(this, "Invalid format. Use Q:A format", Toast.LENGTH_LONG).show();
                return;
            }

            startActivity(new Intent(this, QuizActivity.class));
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri == null) return;

            String fileName = getFileName(uri);
            TextView selectedFileText = findViewById(R.id.selectedFileText);

            if (requestCode == PICK_PDF) {
                selectedFileText.setText("Selected PDF: " + fileName);
            } else if (requestCode == PICK_IMAGE) {
                selectedFileText.setText("Selected Image: " + fileName);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = "Unknown file";
        if ("content".equals(uri.getScheme())) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.moveToFirst()) {
                        int idx = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                        if (idx != -1) {
                            result = cursor.getString(idx);
                        }
                    }
                } finally {
                    cursor.close();
                }
            }
        }
        return result;
    }
}
