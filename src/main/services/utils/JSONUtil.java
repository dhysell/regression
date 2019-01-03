package services.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {

	public static String getURLResponseAsString(String urlString) throws IOException {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> Object convertJsonResponseToObj(String jsonResponseString, Object classToConvertTo) {
		Gson gsonObj = new GsonBuilder().create();
		return gsonObj.fromJson(jsonResponseString, (Class<T>) classToConvertTo);
	}

}
