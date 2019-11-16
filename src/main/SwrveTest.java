//**********************************************************************
//******************Swrve*Code*Assessment*******************************
//*********************Oliver*Altergott*********************************
//**********************************************************************
package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.ArrayList;

import org.joda.time.DateTime;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.io.BufferedReader;
import java.io.FileReader;

public class SwrveTest {

	static String csvFile = "data.csv";

	public static void main(String[] args) {
		if (args.length == 1) {
			String url = args[0];

			// if files could not be initialized
			if (!createNewFiles()) {
				System.exit(1);
			}
			// if file could not be downloaded
			if (!downloadGzipFile(url)) {
				System.exit(1);
			}
			// if file could not be unzipped
			if (!decompressGzipFile("data.gz", "data.csv")) {
				System.exit(1);
			}
			if (processCSV(csvFile)) {
				// success
				System.exit(0);
			} else {
				// something has gone wrong, exit with non zero
				System.exit(1);
			}
		} else {
			System.out.println("should only have 1 Input, no spaces");
			System.exit(1);
		}
	}

	// returns true if successful
	public static boolean processCSV(String fileName) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int index = 0;
		// All data is saved to this array list for further processing, not needed for
		// this assessment
		ArrayList<CsvRow> csvData = new ArrayList<CsvRow>();
		// init date for comparison
		DateTime earliestDt = new DateTime("2090-01-01T12:00:00+01:00");
		String requestedDisplayWidth = "640";
		String requestedDisplayHeight = "960";
		String expectedLabels[] = { "user_id", "date_joined", "spend", "milliseconds_played", "device_height",
				"device_width" };
		int countUsersRes = 0;
		int spendTotal = 0;
		String firstUser = "";

		try {

			br = new BufferedReader(new FileReader(fileName));

			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] lineArray = line.split(cvsSplitBy);
				// check the data for validity
				if (!checkRowLength(lineArray, 6)) {
					System.out.println("wrong csv format, should be exactly 6 columns each row");
					return false;
				}
				// this is data, not the heading/labels
				if (index != 0) {

					// make sure that last 4 items in row only contain digits
					if (!checkStringIsDigitsOnly(lineArray[2]) || !checkStringIsDigitsOnly(lineArray[3])
							|| !checkStringIsDigitsOnly(lineArray[4]) || !checkStringIsDigitsOnly(lineArray[5])) {
						System.out.println("last 4 items in row should only contain digits");
						return false;
					}

					// parse data
					String UserId = lineArray[0];
					String DateJoined = lineArray[1];
					int MoneySpent = Integer.parseInt(lineArray[2]);
					int MillisPlayed = Integer.parseInt(lineArray[3]);
					String DeviceHeight = lineArray[4];
					String DeviceWidth = lineArray[5];

					// all fetched data could be saved in this array, however for this assessment not
					// needed --> for further processing
					//CsvRow myCsvRow = new CsvRow(UserId, DateJoined, MoneySpent, MillisPlayed, DeviceHeight,
					//		DeviceWidth);
					//csvData.add(myCsvRow);

					DateTime currentDt = new DateTime(DateJoined);
					// if isCurrentDateEarlierThanLast is true current date is earlier than previous
					// one,
					// which means we save it for further comparison
					if (isCurrentDateEarlierThanLast(currentDt, earliestDt)) {
						earliestDt = currentDt;
						firstUser = UserId;
					}

					// compare the display size to the requested one and count users that fit
					// criteria
					if (compareDisplaySizeToRequested(DeviceWidth, DeviceHeight, requestedDisplayWidth,
							requestedDisplayHeight)) {
						countUsersRes++;
					}

					// total money spent
					spendTotal += MoneySpent;
					// index is 0, make sure that all headings match required template
				} else {
					// returns true if match
					if (!checkRowLabels(lineArray, expectedLabels)) {
						System.out.println("table headings do not match expected template");
						return false;
					}

				}
				index++;
			}

			int countUsersTotal = index - 1;

			System.out.println(countUsersTotal);
			System.out.println(countUsersRes);
			System.out.println(spendTotal);
			System.out.println(firstUser);
			return true;
			// successful

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					// Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	// returns true if currentDate is earlier than lastDate
	public static boolean isCurrentDateEarlierThanLast(DateTime currentDate, DateTime lastDate) {
		int c = currentDate.compareTo(lastDate);
		if (c == -1) {
			return true;
		} else {
			return false;
		}
	}

	// returns if string is digits only
	public static boolean checkStringIsDigitsOnly(String number) {
		if (number.matches("[0-9]+") && number.length() > 0) {
			return true;
		} else {
			return false;
		}
	}

	// returns true if match
	public static boolean checkRowLength(String rowArray[], int expectedLength) {
		int rowLength = rowArray.length;
		if (rowLength == expectedLength) {
			return true;
		} else {
			return false;
		}
	}

	// returns true if match
	public static boolean checkRowLabels(String rowArray[], String expectedLabels[]) {
		for (int i = 0; i < expectedLabels.length; i++) {
			if (!expectedLabels[i].equals(rowArray[i])) {
				return false;
			}
		}
		return true;
	}

	// returns true if match
	public static boolean compareDisplaySizeToRequested(String displayWidth, String displayHeight,
			String requestedWidth, String requestedHeight) {

		if (displayWidth.equals(requestedWidth) && displayHeight.equals(requestedHeight)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean createNewFiles() {
		File filegz = new File("data.gz");
		// if file doesn't exists, then create it
		if (!filegz.exists()) {
			try {
				filegz.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		File filecsv = new File("data.csv");
		// if file doesn't exists, then create it
		if (!filecsv.exists()) {
			try {
				filecsv.createNewFile();
			} catch (IOException e) {
				// Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static boolean downloadGzipFile(String URL) {

		try {
			URL website = new URL(URL);
			ReadableByteChannel rbc = Channels.newChannel(website.openStream());
			FileOutputStream fos = new FileOutputStream("data.gz");
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
			return true;

		} catch (MalformedURLException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (FileNotFoundException e1) {
			// Auto-generated catch block
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			// Auto-generated catch block
			e.printStackTrace();
			return false;
		}

	}

	public static boolean decompressGzipFile(String gzipFile, String newFile) {
		try {
			FileInputStream fis = new FileInputStream(gzipFile);
			GZIPInputStream gis = new GZIPInputStream(fis);
			FileOutputStream fos = new FileOutputStream(newFile);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = gis.read(buffer)) != -1) {
				fos.write(buffer, 0, len);
			}
			// close resources
			fos.close();
			gis.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	// not needed for this assessment
	public static void compressGzipFile(String file, String gzipFile) {
		try {
			FileInputStream fis = new FileInputStream(file);
			FileOutputStream fos = new FileOutputStream(gzipFile);
			GZIPOutputStream gzipOS = new GZIPOutputStream(fos);
			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) != -1) {
				gzipOS.write(buffer, 0, len);
			}
			// close resources
			gzipOS.close();
			fos.close();
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}
}