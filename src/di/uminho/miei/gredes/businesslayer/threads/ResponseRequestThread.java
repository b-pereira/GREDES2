package di.uminho.miei.gredes.businesslayer.threads;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;

/**
 * 
 * @author bpereira
 *
 */
public class ResponseRequestThread extends Thread implements ResponseListener {

	/**
	 * 
	 */
	private ManagerHelper manageHelper;
	private Timer timer;
	private TimerTask task;

	/**
	 * 
	 * @param managerHelper
	 */
	public ResponseRequestThread(ManagerHelper managerHelper) {
		super();
		this.manageHelper = managerHelper;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {

		timer = new Timer(true);

		task = new TimerTask() {

			@Override
			public void run() {
				try {
					ResponseRequestThread.this.manageHelper.bulkOctectsPollingRequest(ResponseRequestThread.this);
				} catch (Exception e) {

				}
			}

		};

		timer.schedule(task, 0, manageHelper.getPolltime());

	}

	/**
	 * 
	 */
	public void stopPolling() {
		this.timer.cancel();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.snmp4j.event.ResponseListener#onResponse(org.snmp4j.event.
	 * ResponseEvent)
	 */
	@Override
	public void onResponse(ResponseEvent event) {

		((Snmp) event.getSource()).cancel(event.getRequest(), this);
		try {
			manageHelper.bulkOctectsPollingResponse(event);
		} catch (IOException | InterruptedException e) {

		}

	}

}
