package ro.home.project.util;


import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DateUtils {

	public static LocalDateTime currentDateTime() {
		return LocalDateTime.ofInstant(Instant.now(), ZoneId.of("Europe/Bucharest"));
	}
}
