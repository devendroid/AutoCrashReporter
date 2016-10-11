package com.devs.acr;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

//http://www.iriphon.com/2011/06/23/how-do-i-get-android-crash-logs/

public class AutoErrorReporter implements Thread.UncaughtExceptionHandler {

	private static final String TAG = AutoErrorReporter.class.getSimpleName();
	private static String DEFAULT_EMAIL_SUBJECT = "ACR: New Crash Report Generated";

	private String[] recipients ;
	private boolean startAttempted = false;

	private String versionName;
	//private String buildNumber;
	private String packageName;
	private String filePath;
	private String phoneModel;
	private String androidVersion;
	private String board;
	private String brand;
	private String device;
	private String display;
	private String fingerPrint;
	private String host;
	private String id;
	private String manufacturer;
	private String model;
	private String product;
	private String tags;
	private long time;
	private String type;
	private String user;
	private HashMap<String, String> customParameters = new HashMap<String, String>();

	private Thread.UncaughtExceptionHandler previousHandler;
	private static AutoErrorReporter sInstance;
	private Application application;

	private AutoErrorReporter(Application application){
		this.application = application;
	}

	public static AutoErrorReporter get(Application application) {
		if (sInstance == null)
			sInstance = new AutoErrorReporter(application);
		return sInstance;
	}

	public void start() {

		if(startAttempted) {
			Log.i(TAG, "Already started");
			return;
		}
		previousHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);

		startAttempted = true;
	}

	/**
	 * (Required) Defines one or more email addresses to send bug reports to. This method MUST be
	 * called before calling start This method CANNOT be called after calling
	 * start.
	 *
	 * @param emailAddresses one or more email addresses
	 * @return the current AutoErrorReporterinstance (to allow for method chaining)
	 */

	public AutoErrorReporter setEmailAddresses(final String... emailAddresses) {
		if (startAttempted) {
			throw new IllegalStateException(
					"EmailAddresses must be set before start");
		}
		this.recipients = emailAddresses;
		return this;
	}

	/**
	 * (Optional) Defines a custom subject line to use for all bug reports. By default, reports will
	 * use the string defined in DEFAULT_EMAIL_SUBJECT This method CANNOT be called
	 * after calling start.
	 * @param emailSubject custom email subject line
	 * @return the current AutoErrorReporter instance (to allow for method chaining)
	 */
	public AutoErrorReporter setEmailSubject(final String emailSubject) {
		if (startAttempted) {
			throw new IllegalStateException("EmailSubject must be set before start");
		}

		this.DEFAULT_EMAIL_SUBJECT = emailSubject;
		return this;
	}

	public void addCustomData(String Key, String Value) {
		customParameters.put(Key, Value);
	}

	private String createCustomInfoString() {
		String CustomInfo = "";
		Iterator iterator = customParameters.keySet().iterator();
		while (iterator.hasNext()) {
			String CurrentKey = (String) iterator.next();
			String CurrentVal = customParameters.get(CurrentKey);
			CustomInfo += CurrentKey + " = " + CurrentVal + "\n";
		}
		return CustomInfo;
	}

	public long getAvailableInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return availableBlocks * blockSize;
	}

	public long getTotalInternalMemorySize() {
		File path = Environment.getDataDirectory();
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long totalBlocks = stat.getBlockCount();
		return totalBlocks * blockSize;
	}

	private void recordInformations(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			PackageInfo pi;
			// Version
			pi = pm.getPackageInfo(context.getPackageName(), 0);
			versionName = pi.versionName;
			//buildNumber = currentVersionNumber(context);
			// Package name
			packageName = pi.packageName;

			// Device model
			phoneModel = Build.MODEL;
			// Android version
			androidVersion = Build.VERSION.RELEASE;

			board = Build.BOARD;
			brand = Build.BRAND;
			device = Build.DEVICE;
			display = Build.DISPLAY;
			fingerPrint = Build.FINGERPRINT;
			host = Build.HOST;
			id = Build.ID;
			model = Build.MODEL;
			product = Build.PRODUCT;
			manufacturer = Build.MANUFACTURER;
			tags = Build.TAGS;
			time = Build.TIME;
			type = Build.TYPE;
			user = Build.USER;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String createInformationString() {
		recordInformations(application);

		String ReturnVal = "";
		ReturnVal += "Version : " + versionName;
		ReturnVal += "\n";
		//ReturnVal += "Build Number : " + buildNumber;
		ReturnVal += "\n";
		ReturnVal += "Package : " + packageName;
		ReturnVal += "\n";
		ReturnVal += "FilePath : " + filePath;
		ReturnVal += "\n";
		ReturnVal += "Phone Model" + phoneModel;
		ReturnVal += "\n";
		ReturnVal += "Android Version : " + androidVersion;
		ReturnVal += "\n";
		ReturnVal += "Board : " + board;
		ReturnVal += "\n";
		ReturnVal += "Brand : " + brand;
		ReturnVal += "\n";
		ReturnVal += "Device : " + device;
		ReturnVal += "\n";
		ReturnVal += "Display : " + display;
		ReturnVal += "\n";
		ReturnVal += "Finger Print : " + fingerPrint;
		ReturnVal += "\n";
		ReturnVal += "Host : " + host;
		ReturnVal += "\n";
		ReturnVal += "ID : " + id;
		ReturnVal += "\n";
		ReturnVal += "Model : " + model;
		ReturnVal += "\n";
		ReturnVal += "Product : " + product;
		ReturnVal += "\n";
		ReturnVal += "Manufacturer : " + manufacturer;
		ReturnVal += "\n";
		ReturnVal += "Tags : " + tags;
		ReturnVal += "\n";
		ReturnVal += "Time : " + time;
		ReturnVal += "\n";
		ReturnVal += "Type : " + type;
		ReturnVal += "\n";
		ReturnVal += "User : " + user;
		ReturnVal += "\n";
		ReturnVal += "Total Internal memory : " + getTotalInternalMemorySize();
		ReturnVal += "\n";
		ReturnVal += "Available Internal memory : "
				+ getAvailableInternalMemorySize();
		ReturnVal += "\n";

		return ReturnVal;
	}

	public void uncaughtException(Thread t, Throwable e) {
		Log.i(TAG, "====uncaughtException");

		String Report = "";
		Date CurDate = new Date();
		Report += "Error Report collected on : " + CurDate.toString();
		Report += "\n";
		Report += "\n";
		Report += "Informations :";
		Report += "\n";
		Report += "==============";
		Report += "\n";
		Report += "\n";
		Report += createInformationString();

		Report += "Custom Informations :\n";
		Report += "=====================\n";
		Report += createCustomInfoString();

		Report += "\n\n";
		Report += "Stack : \n";
		Report += "======= \n";
		final Writer result = new StringWriter();
		final PrintWriter printWriter = new PrintWriter(result);
		e.printStackTrace(printWriter);
		String stacktrace = result.toString();
		Report += stacktrace;

		Report += "\n";
		Report += "Cause : \n";
		Report += "======= \n";

		// If the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable cause = e.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			Report += result.toString();
			cause = cause.getCause();
		}
		printWriter.close();
		Report += "**** End of current Report ***";
		Log.i(TAG, "====uncaughtException \n Report: "+Report);
		saveAsFile(Report);
		//SendErrorMail(CurContext, Report );

		Intent intent = new Intent(application, ErrorReporterActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		application.startActivity(intent);

		previousHandler.uncaughtException(t, e);
	}

	private void sendErrorMail(Context _context, String ErrorContent) {
		Log.i(TAG, "====sendErrorMail");
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		String subject = DEFAULT_EMAIL_SUBJECT;
		String body = "\n\n" + ErrorContent + "\n\n";
		sendIntent.putExtra(Intent.EXTRA_EMAIL, recipients);
		sendIntent.putExtra(Intent.EXTRA_TEXT, body);
		sendIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sendIntent.setType("message/rfc822");
		sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(Intent.createChooser(sendIntent, "Title:"));
	}

	private void saveAsFile(String ErrorContent) {
		Log.i(TAG, "====SaveAsFile");
		try {
			Random generator = new Random();
			int random = generator.nextInt(99999);
			String FileName = "stack-" + random + ".stacktrace";
			FileOutputStream trace = application.openFileOutput(FileName,
					Context.MODE_PRIVATE);
			trace.write(ErrorContent.getBytes());
			trace.close();
		} catch (Exception e) {
			// ...
		}
	}

	private String[] getErrorFileList() {
		File dir = new File(filePath + "/");
		// Try to create the files folder if it doesn't exist
		dir.mkdir();
		// Filter for ".stacktrace" files
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".stacktrace");
			}
		};
		return dir.list(filter);
	}

	private boolean bIsThereAnyErrorFile() {
		return getErrorFileList().length > 0;
	}

	public void checkErrorAndSendMail(Context _context) {
		try {
			filePath = _context.getFilesDir().getAbsolutePath();
			if (bIsThereAnyErrorFile()) {
				String WholeErrorText = "";

				String[] ErrorFileList = getErrorFileList();
				int curIndex = 0;
				final int MaxSendMail = 5;
				for (String curString : ErrorFileList) {
					if (curIndex++ <= MaxSendMail) {
						WholeErrorText += "New Trace collected :\n";
						WholeErrorText += "=====================\n ";
						String filePathStr = filePath + "/" + curString;
						BufferedReader input = new BufferedReader(
								new FileReader(filePathStr));
						String line;
						while ((line = input.readLine()) != null) {
							WholeErrorText += line + "\n";
						}
						input.close();
					}

					// DELETE FILES !!!!
					File curFile = new File(filePath + "/" + curString);
					curFile.delete();
				}
				sendErrorMail(_context, WholeErrorText);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public static String currentVersionNumber(Context a) {
//		PackageManager pm = a.getPackageManager();
//		try {
//			PackageInfo pi = pm.getPackageInfo("de.gamedisk.app",
//					PackageManager.GET_SIGNATURES);
//			return pi.versionName
//					+ (pi.versionCode > 0 ? " (" + pi.versionCode + ")" : "");
//		} catch (NameNotFoundException e) {
//			return null;
//		}
//	}
}
