package net.hashcoding.scucrawler;

import net.hashcoding.scucrawler.task.JWCTask;

public class Main {
	public static String dumpFilename;
	public static int FactoryEmployeeSize = 5;
	public static Thread MainThread;

	public static void main(String[] args) {
        dumpFilename = "D:\\passage_dump.txt";

		MainThread = Thread.currentThread();

        // leancloud sdk init.
        Config.initialize();
        PageFactory.instance().start();

		TaskManager manager = TaskManager.instance();
		manager.delegateTask(new JWCTask());
		manager.run();
		PageFactory.instance().waitFactoryStop();

        System.out.println("All task is done!");

        // TODO: check all child thread is exit.
        System.exit(0);
	}
}
