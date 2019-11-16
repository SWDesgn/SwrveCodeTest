package main;

public class CsvRow {
	private String userId;
	private String dateJoined;
	private int spend;
	private int millisecondsPlayed;
	private String deviceHeight;
	private String deviceWidth;

	public CsvRow(String userId, String dateJoined, int spend, int millisecondsPlayed, String deviceHeight,
			String deviceWidth) {
		this.userId = userId;
		this.dateJoined = dateJoined;
		this.spend = spend;
		this.millisecondsPlayed = millisecondsPlayed;
		this.deviceHeight = deviceHeight;
		this.deviceWidth = deviceWidth;
	}

}
