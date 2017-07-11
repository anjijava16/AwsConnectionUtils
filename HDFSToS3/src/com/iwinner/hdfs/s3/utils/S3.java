package com.iwinner.hdfs.s3.utils;

import java.io.BufferedInputStream;
import java.util.List;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;

class S3 extends AmazonS3Client {

	final String bucket;

	S3(String u, String p, String Bucket) {
		super(new BasicAWSCredentials(u, p));
		bucket = Bucket;
	}

	String get(String k) {
		try {
			final S3Object f = getObject(bucket, k);
			final BufferedInputStream i = new BufferedInputStream(f.getObjectContent());
			final StringBuilder s = new StringBuilder();
			final byte[] b = new byte[1024];
			for (int n = i.read(b); n != -1; n = i.read(b)) {
				s.append(new String(b, 0, n));
			}
			return s.toString();
		} catch (Exception e) {
			log("Cannot get " + bucket + "/" + k + " from S3 because " + e);
		}
		return null;
	}

	String[] list(String d) {
		try {
			final ObjectListing l = listObjects(bucket, d);
			final List<S3ObjectSummary> L = l.getObjectSummaries();
			final int n = L.size();
			final String[] s = new String[n];
			for (int i = 0; i < n; ++i) {
				final S3ObjectSummary k = L.get(i);
				s[i] = k.getKey();
			}
			return s;
		} catch (Exception e) {
			log("Cannot list " + bucket + "/" + d + " on S3 because " + e);
		}
		return new String[] {};
	}
}