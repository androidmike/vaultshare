package com.vaultshare.play.model;

import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.cognito.Dataset;
import com.amazonaws.mobileconnectors.cognito.DefaultSyncCallback;
import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.mobileconnectors.s3.transfermanager.Upload;
import com.amazonaws.regions.Regions;
import com.vaultshare.play.App;
import com.vaultshare.play.Bus;
import com.vaultshare.play.TrackUploaded;

import java.io.File;
import java.util.List;

/**
 * Created by mchang on 7/2/15.
 */
public class AwsController {
    static AwsController instance;
    CognitoCachingCredentialsProvider credentialsProvider;
    TransferManager                   transferManager;

    public static AwsController getInstance() {
        if (instance == null) {
            instance = new AwsController();
        }
        return instance;
    }

    public AwsController() {
        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                App.getContext(), // Context
                "us-east-1:b5315915-1dd7-484c-b8e5-1e43cc4ff31b", // Identity Pool ID
                Regions.US_EAST_1 // Region
        );
        transferManager = new TransferManager(credentialsProvider);
    }

    public void download(String fileName, File outputFile) {
        Download download = transferManager.download("vsrecs", fileName, outputFile);
    }

    public void upload(String trackId , String fileName, File inputFile) {
        Upload upload = transferManager.upload("vsrecs", fileName, inputFile);
        while (!upload.isDone()) {
//            Toast.makeText(App.getContext(), "Uploading...", Toast.LENGTH_LONG).show();
        }
        Bus.getInstance().post(new TrackUploaded(trackId));
        Toast.makeText(App.getContext(), "Uploaded", Toast.LENGTH_LONG).show();
    }

    public void storeSample() {
        // Initialize the Cognito Sync client
        CognitoSyncManager syncClient = new CognitoSyncManager(
                App.getContext(),
                Regions.US_EAST_1, // Region
                credentialsProvider);

        // Create a record in a dataset and synchronize with the server
        Dataset dataset = syncClient.openOrCreateDataset("myDataset");
        dataset.put("myKey", "myValue");
        dataset.synchronize(new DefaultSyncCallback() {
            @Override
            public void onSuccess(Dataset dataset, List newRecords) {
                //Your handler code here
            }
        });
    }
}
