package org.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class WritePrivateStorage
{
	private static final String LOG_TAG="WritePrivateStorage";
	public static  String writeFileToPrivateStorage(Context context,
												   int fromFile,
												   String toFile)
	{
		 InputStream is = context.getResources().openRawResource(fromFile);
		 int bytes_read;
		 byte[] buffer = new byte[4096];
		 try 
		 {
			FileOutputStream fos = context.openFileOutput(toFile,
									Context.MODE_PRIVATE);
			while ((bytes_read = is.read(buffer)) != -1)		    
		        fos.write(buffer, 0, bytes_read); // write

			fos.close();
			is.close();
			 String path=context.getApplicationContext()
											 .getFilesDir()
					 						 .toString();
			 path = path + "/" + toFile;
			 return path;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}		 		
	}
}
