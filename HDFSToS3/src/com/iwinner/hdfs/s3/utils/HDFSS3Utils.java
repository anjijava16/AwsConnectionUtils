package com.iwinner.hdfs.s3.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Path;

import javax.security.auth.login.Configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerConfiguration;
import com.amazonaws.services.s3.transfer.Upload;
	
public class HDFSS3Utils {
	
		
	String awsKey="";
	String awsSecret="";
	
	public void uploadLargeFile(String path,String key,String bucketName)
	{

	        try
	{

	System.out.println("starting multi part upload…");
	long contentLenght=0;
	AWSCredentials myCredentials = new BasicAWSCredentials(awsKey, awsSecret);
	TransferManager tx = new TransferManager(myCredentials);
	TransferManagerConfiguration tConfig=new TransferManagerConfiguration();
	tConfig.setMinimumUploadPartSize(minBlockSize);
	tx.setConfiguration(tConfig);
	Configuration config=new Configuration();
	config.set("fs.default.name",nameNode);
	FileSystem fs=FileSystem.get(config);
	Path path2=new Path(path);
	if(fs.isFile(path2))
	{
	contentLenght=fs.getFileStatus(path2).getLen();
	System.out.println("Content Length "+contentLenght);
	}
	InputStream inputStream=new DataInputStream(fs.open(path2));
	BufferedInputStream bInputStream=new BufferedInputStream(inputStream);
	ObjectMetadata objectMeta=new ObjectMetadata();
	objectMeta.setContentLength(contentLenght);
	final Upload upload = tx.upload(bucketName,key,bInputStream,objectMeta);
	upload.waitForCompletion();

	}
	catch(Exception e)
	{
	e.printStackTrace();
	}

	}
	
	
}
