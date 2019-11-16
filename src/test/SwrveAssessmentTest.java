package test;

import static org.junit.jupiter.api.Assertions.*;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import main.SwrveTest;

class SwrveAssessmentTest {

	@Test
	void testProcessCSVonUncorruptedData() {
		// should resolve to true
		boolean result = SwrveTest.processCSV("testfiles/test_data_uncorrupted.csv");
		assertEquals(true, result);
	}

	@Test
	void testProcessCSVonCorruptedData() {
		// should resolve to false
		boolean result = SwrveTest.processCSV("testfiles/test_data_corrupted.csv");
		assertEquals(false, result);
	}

	@Test
	void testCreateNewFiles() {
		// should resolve to true
		boolean result = SwrveTest.createNewFiles();
		assertEquals(true, result);
	}

	@Test
	void testUnzipGzipFile() {
		// should resolve to true
		boolean result = SwrveTest.decompressGzipFile("testfiles/test_gzip.gz", "testfiles/test_unzipped.csv");
		assertEquals(true, result);
	}

	@Test
	void testStringIsDigitsOnly() {
		// should resolve to true
		String number = "1234566";
		boolean result = SwrveTest.checkStringIsDigitsOnly(number);
		assertEquals(true, result);
	}

	@Test
	void testCheckRowLength() {
		// should resolve to true
		String rowArray[] = { "1", "2", "3", "4", "5", "6" };
		int expectedLength = 6;
		boolean result = SwrveTest.checkRowLength(rowArray, expectedLength);
		assertEquals(true, result);
	}

	@Test
	void testCheckRowLength2() {
		// should resolve to true
		String rowArray[] = { "1", "2", "3", "4", "5", "6", "7" };
		int expectedLength = 6;
		boolean result = SwrveTest.checkRowLength(rowArray, expectedLength);
		assertEquals(false, result);
	}

	@Test
	void testIsCurrentDateEarlierThanLast() {
		// should resolve to false
		DateTime currentDate = new DateTime("2019-11-05T12:00:00+01:00");
		DateTime earlierDate = new DateTime("2019-11-04T19:00:00+01:00");
		boolean result = SwrveTest.isCurrentDateEarlierThanLast(currentDate, earlierDate);
		assertEquals(false, result);
	}

	@Test
	void testCompareDisplaySizeToRequested() {
		// should resolve to true
		String displayWidth = "550";
		String displayHeight = "450";
		String requestedWidth = "550";
		String requestedHeight = "450";
		boolean result = SwrveTest.compareDisplaySizeToRequested(displayWidth, displayHeight, requestedWidth,
				requestedHeight);
		assertEquals(true, result);
	}

	@Test
	void testCheckRowLabels() {
		// should resolve to false, since labels do not match
		String expectedLabels[] = { "label1", "label2", "label3", "label4", "label5", "label6" };
		String rowArray[] = { "Label1", "Label2", "Label4", "Label3", "Babel5", "Label6" };
		boolean result = SwrveTest.checkRowLabels(rowArray, expectedLabels);
		assertEquals(false, result);
	}

}
