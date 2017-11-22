package io.github.yhdesai.voice_library;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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

    private String token = getString(R.string.clientAccessToken);
    private String action1 = getString(R.string.action1);
    private String action2 = getString(R.string.action2);
    private AIService aiService;
    private static final String TAG = "MyActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Dialogflow config
        // set client access token of your agent in strings.xml
        final AIConfiguration config = new AIConfiguration(token,
                AIConfiguration.SupportedLanguages.English,
                AIConfiguration.RecognitionEngine.System);
        aiService = AIService.getService(this, config);
        aiService.setListener(this);


    }


    // When the listen button is clicked, start listening
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


        Log.d(TAG, "Query:" + result.getResolvedQuery() +
                "\nStatus code: " + status.getCode() +
                "\nStatus type: " + status.getErrorType() +
                "\nResolved Query: " + result.getResolvedQuery() +
                "\nAction: " + result.getAction() +
                "\nSpeech: " + speech +
                "\nMetadata Intent ID: " + metadata.getIntentId() +
                "\nMetadata Intent Name: " + metadata.getIntentName() +
                "\nParameters: " + parameterString);


        if (action.equals(action1)) {

            // code to be executed when action 1 matches
            Log.d(TAG, "Action1 matches");

        }



        else if (action.equals(action2)) {

            // code to be executed when action 2 matches
            Log.d(TAG, "Action2 matches");

        }



        else {

            // If no action matches, log action not found error
            Log.e(TAG, "Action not found");
            Log.e(TAG, "Action: " + result.getAction());

        }


    }

    @Override
    public void onError(final AIError error) {
        Log.e(TAG, error.toString());


    }

    @Override
    public void onListeningStarted() {
        Log.d(TAG, "Listening started");
    }

    @Override
    public void onListeningCanceled() {
        Log.d(TAG, "Listening cancelled");
    }

    @Override
    public void onListeningFinished() {
        Log.d(TAG, "Listening finished");
    }

    @Override
    public void onAudioLevel(final float level) {
    }


}