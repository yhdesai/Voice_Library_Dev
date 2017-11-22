package io.github.yhdesai.voice_library;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonElement;

import java.util.Map;

import ai.api.android.AIConfiguration;
import ai.api.AIListener;
import ai.api.android.AIService;
import ai.api.model.AIError;
import ai.api.model.AIResponse;
import ai.api.model.Metadata;
import ai.api.model.Result;
import ai.api.model.Status;

public class MainActivity extends AppCompatActivity implements AIListener {


    private Button listenButton;
    private AIService aiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listenButton = findViewById(R.id.listenButton);
        final AIConfiguration config = new AIConfiguration("1af76d68a4b44d79a538d45900c6444f",
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);


    }

    public void listenButtonOnClick(final View view) {
        aiService.startListening();
    }

    public void onResult(final AIResponse response) {
        Status status = response.getStatus();
        Result result = response.getResult();
        String action = result.getAction();
        final String speech = result.getFulfillment().getSpeech();
        final Metadata metadata = result.getMetadata();

        // Get parameters
        String parameterString = "";
        if (result.getParameters() != null && !result.getParameters().isEmpty()) {
            for (final Map.Entry<String, JsonElement> entry : result.getParameters().entrySet()) {
                parameterString += "(" + entry.getKey() + ", " + entry.getValue() + ") ";
            }
        }

        // Show results in TextView.
        resultTextView.setText("Query:" + result.getResolvedQuery() +
                "\nStatus code: " + status.getCode() +
                "\nStatus type: " + status.getErrorType() +
                "\nResolved Query: " + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nSpeech: " + speech +
                "\nMetadata Intent ID: " + metadata.getIntentId() +
                "\nMetadata Intent Name: " + metadata.getIntentName() +
                "\nParameters: " + parameterString

        );

        if (action.equals("google")) {
            goToUrl("https://www.google.com");
            actionTextView.setText("Action found");

        } else {
            actionTextView.setText("Action not found");
        }


    }

    @Override
    public void onError(final AIError error) {
        resultTextView.setText(error.toString());
    }

    @Override
    public void onListeningStarted() {
        status.setText("listening started");
    }

    @Override
    public void onListeningCanceled() {
        status.setText("listening cancelled");
    }

    @Override
    public void onListeningFinished() {
        status.setText("listening finished");
    }

    @Override
    public void onAudioLevel(final float level) {
    }

    private void goToUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
        startActivity(launchBrowser);
    }


}