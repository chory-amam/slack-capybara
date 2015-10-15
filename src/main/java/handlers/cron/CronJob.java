package handlers.cron;

public class CronJob {
	public String schedule;
	public String to;
	public String message;

	public CronJob(final String schedule, final String to, final String message) {
		this.schedule = schedule;
		this.to = to;
		this.message = message;
	}
}
