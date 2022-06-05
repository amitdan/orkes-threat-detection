package io.orkes.samples.utils;

public class Util {

    public static String raiseIncident(String fileName) {
        //Api call to raise an incident
    }

    public static String publishResultToDashboard(String fileName) {
        //Api call to publish result to Dashboard
    }

    public static Boolean threatScan(String fileName) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();


// Configure API key authorization: Apikey


        ApiKeyAuth Apikey = (ApiKeyAuth) defaultClient.getAuthentication("Apikey");


        Apikey.setApiKey("YOUR API KEY");


// Uncomment the following line to set a prefix for the API key, e.g. "Token" (defaults to null)


//Apikey.setApiKeyPrefix("Token");


        ScanCloudStorageApi apiInstance = new ScanCloudStorageApi();


        String accessKey = "accessKey_example"; // String | AWS S3 access key for the S3 bucket; you can get this from My Security Credentials in the AWS console


        String secretKey = "secretKey_example"; // String | AWS S3 secret key for the S3 bucket; you can get this from My Security Credentials in the AWS console


        String bucketRegion = "bucketRegion_example"; // String | Name of the region of the S3 bucket, such as 'US-East-1'


        String bucketName = "bucketName_example"; // String | Name of the S3 bucket


        String keyName = "keyName_example"; // String | Key name (also called file name) of the file in S3 that you wish to scan for viruses


        try {

            CloudStorageVirusScanResult result = apiInstance.scanCloudStorageScanAwsS3File(accessKey, secretKey, bucketRegion, bucketName, keyName);

            System.out.println(result);

        } catch (ApiException e) {

            System.err.println("Exception when calling ScanCloudStorageApi#scanCloudStorageScanAwsS3File");

            e.printStackTrace();

        }
    }

}
