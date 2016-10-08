package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.JWCTask;

public class Main {
	public static String dumpFilename;

	public static void main(String[] args) {
        dumpFilename = "D:\\passage_dump.txt";

        // leancloud sdk init.
        Config.initialize();

		TaskManager manager = TaskManager.instance();
		manager.delegateTask(new JWCTask());
		manager.run();
	}
}
