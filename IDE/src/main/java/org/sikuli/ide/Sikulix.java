/*
 * Copyright (c) 2010-2018, sikuli.org, sikulix.com - MIT license
 */

package org.sikuli.ide;

import org.sikuli.script.SikulixStart;
import org.sikuli.util.ProcessRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Sikulix {

  static String start = String.format("%d", new Date().getTime());

  public static void main(String[] args) {

    if (args.length > 0 && args[0].equals("-v")) {
      SikulixStart.setVerbose(true);
      args[0] += start;
    }

    File runningJar = SikulixStart.getRunningJar();
    String jarName = runningJar.getName();
    File fAppData = SikulixStart.getAppPath();
    SikulixStart.log(1, "Running: %s", runningJar);
    SikulixStart.log(1, "AppData: %s", fAppData);

    int exitValue = 0;
    if (!jarName.endsWith(".jar")) {
      SikulixIDE.main(args);
      return;
    } else {
      while (true) {
        String classPath = SikulixStart.makeClassPath(runningJar);
        List<String> cmd = new ArrayList<>();
        cmd.add("java");
        if (!classPath.isEmpty()) {
          cmd.add("-cp");
          cmd.add(classPath);
        }
        cmd.add("org.sikuli.ide.SikulixIDE");
        cmd.addAll(Arrays.asList(args));
        exitValue = ProcessRunner.detach(cmd);
        if (exitValue < 255) {
          System.out.println(String.format("IDE terminated: returned: %d", exitValue));
        } else {
          System.out.println(String.format("IDE terminated: returned: %d --- trying to restart", exitValue));
          continue;
        }
        System.exit(exitValue);
      }
    }
  }
}
