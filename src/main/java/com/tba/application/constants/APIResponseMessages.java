package com.tba.application.constants;

public class APIResponseMessages {
	
	public static final String MOVEMENT_NOT_PROCESSED = "Request for movement could not be processed";
	public static final String MOVEMENT_NOT_PROCESSED_DUE_TO_LOCK = "Movement of Crane failed because lock could not be acquired";
	public static final String MOVEMENT_PROCESSED = "Request for movement processed";
	public static final String MISSING_CRANE_MAPPING = "There is no mapping for corresponding crane";
	public static final String CRANE_ALREADY_PARKED = "Crane is already parked";
	public static final String CRANE_MOVEMENT_QUEUED = "Request for movement added to queue";
	public static final String CRANE_ALREADY_AT_DESIRED_POSITION = "Crane already at the desired position";
	public static final String MISSING_SECONDARY_CRANE_MAPPING = "There is no mapping for corresponding second crane. Hence exiting.";
	public static final String CRANE_MOVEMENT_QUEUED_POST_PARKING = "Request for movement of other crane and then this crane added to queue";
	

}
